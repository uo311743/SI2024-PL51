package model;

import util.ApplicationException;
import util.Database;
import util.SemanticValidations;
import util.UnexpectedException;
import util.Util;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import DTOs.ActivitiesDTO;

public class RegisterSponsorshipModel
{
	// Instance that allows the connection to the DB and execution of queries
	private Database db = new Database();

	public List<ActivitiesDTO> getActivitiesbyStatus(String... status)
	{
	    // Construcción de los placeholders en función del número de elementos en 'status'
	    String placeholders = String.join(",", Collections.nCopies(status.length, "?"));
	    String sql = "SELECT * FROM Activities WHERE status IN (" + placeholders + ")";

	    return db.executeQueryPojo(ActivitiesDTO.class, sql, (Object[]) status);
	}


	public List<Object[]> getAllSponsorsArray()
	{
	    String sql = "SELECT id || ' - ' || name AS item FROM SponsorOrganizations";
	    return db.executeQueryArray(sql);
	}

	public List<Object[]> getContactsBySponshorArray(String sponshor)
	{
		String sql = "SELECT id || ' - ' || name AS item FROM SponsorContacts WHERE idSponsorOrganization == ?";
	    return db.executeQueryArray(sql, sponshor);
	}

	public List<Object[]> getAllGBMembersArray()
	{
		String sql = "SELECT id || ' - ' || name AS item FROM GBMembers";
	    return db.executeQueryArray(sql);
	}

	public void insertNewSponsorshipAgreement(String idSponsorContact, String idGBMember, String idActivity, String amount, String date)
	{
		SemanticValidations.validateIdForTable(idSponsorContact, "SponsorContacts",
				"ERROR. Tried to insert a Sponsorship agreement with an unexisting idSponsorContact.");
		
		SemanticValidations.validateIdForTable(idGBMember, "GBMembers",
				"ERROR. Tried to insert a Sponsorship agreement with an unexisting idGBMember.");
		
		SemanticValidations.validateIdForTable(idActivity, "Activities",
				"ERROR. Tried to insert a Sponsorship agreement with an unexisting idActivity.");
		
		SemanticValidations.validatePositiveNumber(amount,
				"ERROR. Tried to insert a Sponsorship agreement with a non-positive amount.");
		
		SemanticValidations.validateDateInPast(date, true,
				"ERROR. Tried to insert a Sponsorship agreement with a future date.");
		
		if(getNumberOldSponsorshipAgreements(idSponsorContact, idActivity) != 0)
			throw new UnexpectedException("Args provided to insertNewSponsorshipAgreement do not correspond to a new Agreement but an old one.");
		
		String sql = "INSERT INTO SponsorshipAgreements"
				+ "(idSponsorContact, idGBMember, idActivity, amount, date, status) VALUES "
				+ "(?, ?, ?, ?, ?, 'signed')";
		db.executeUpdate(sql, idSponsorContact, idGBMember, idActivity, amount, date);
	}

	public void insertUpdateSponsorshipAgreement(String idSponsorContact, String idGBMember, String idActivity, String amount, String date) {
		
		SemanticValidations.validateIdForTable(idSponsorContact, "SponsorContacts",
				"ERROR. Tried to insert a Sponsorship agreement with an unexisting idSponsorContact.");
		
		SemanticValidations.validateIdForTable(idGBMember, "GBMembers",
				"ERROR. Tried to insert a Sponsorship agreement with an unexisting idGBMember.");
		
		SemanticValidations.validateIdForTable(idActivity, "Activities",
				"ERROR. Tried to insert a Sponsorship agreement with an unexisting idActivity.");
		
		SemanticValidations.validatePositiveNumber(amount,
				"ERROR. Tried to insert a Sponsorship agreement with a non-positive amount.");
		
		SemanticValidations.validateDateInPast(date, true,
				"ERROR. Tried to insert a Sponsorship agreement with a future date.");
		
		this.validateDateForUpdateSponsorshipAgreement(idSponsorContact, idActivity, date,
				"ERROR. There already exists a sponsorship after this one's date.");
		
		String sql = "INSERT INTO SponsorshipAgreements"
				+ "(idSponsorContact, idGBMember, idActivity, amount, date, status) VALUES "
				+ "(?, ?, ?, ?, ?, 'signed')";
		db.executeUpdate(sql, idSponsorContact, idGBMember, idActivity, amount, date);
		
		sql = "UPDATE SponsorshipAgreements "
				+ "SET status = 'modified' "
				+ "WHERE idActivity = ? "
				+ "AND idSponsorContact IN ("
				+ "    SELECT id "
				+ "    FROM SponsorContacts "
				+ "    WHERE idSponsorOrganization = ("
				+ "        SELECT idSponsorOrganization "
				+ "        FROM SponsorContacts "
				+ "        WHERE id = ?"
				+ "    )"
				+ ")";
		db.executeUpdate(sql, idActivity, idSponsorContact);
	}

	// ================================================================================

	public int getNumberOldSponsorshipAgreements(String idSponsorContact, String idActivity)
	{
		String sql = "SELECT COUNT(sa.id) AS total_agreements "
				+ "FROM SponsorshipAgreements sa "
				+ "JOIN SponsorContacts sc ON sa.idSponsorContact = sc.id "
				+ "WHERE sc.idSponsorOrganization = ( "
				+ "    SELECT idSponsorOrganization "
				+ "    FROM SponsorContacts "
				+ "    WHERE id = ? "
				+ ") "
				+ "AND sa.idActivity = ?;";
		List<Object[]> result = db.executeQueryArray(sql, idSponsorContact, idActivity);
		if (result == null || result.isEmpty()) {
			return 0;
		}
		return (int) result.get(0)[0];
	}

	private void validateDateForUpdateSponsorshipAgreement(String idSponsorContact, String idActivity, String date, String message)
	{
		String sql = "SELECT MAX(sa.date) AS last_agreement_date "
				+ "FROM SponsorshipAgreements sa "
				+ "JOIN SponsorContacts sc ON sa.idSponsorContact = sc.id "
				+ "WHERE sc.idSponsorOrganization = ( "
				+ "    SELECT idSponsorOrganization "
				+ "    FROM SponsorContacts "
				+ "    WHERE id = ? "
				+ ") "
				+ "AND sa.idActivity = ?;";
		List<Object[]> result = db.executeQueryArray(sql, idSponsorContact, idActivity);
		
		if (result == null || result.isEmpty() || result.get(0)[0] == null) {
			return;
		}
		
		Date lastSponsorshipAgreementDate = Util.isoStringToDate((String) result.get(0)[0]);
		Date newSponsorshipAgreementDate = Util.isoStringToDate(date);
		
		if (newSponsorshipAgreementDate.before(lastSponsorshipAgreementDate)) {
			throw new ApplicationException(message);
		}
	}
}