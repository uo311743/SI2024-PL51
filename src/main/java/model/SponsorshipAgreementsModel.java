package model;

import java.rmi.UnexpectedException;
import java.util.Date;
import java.util.List;
import DTOs.SponsorshipAgreementsDTO;
import util.ApplicationException;
import util.Database;
import util.SemanticValidations;
import util.Util;

public class SponsorshipAgreementsModel {

	public static final String SQL_NUMBER_OLD_SA = "SELECT COUNT(sa.id) AS total_agreements "
				+ "FROM SponsorshipAgreements sa "
				+ "JOIN SponsorContacts sc ON sa.idSponsorContact = sc.id "
				+ "WHERE sc.idSponsorOrganization = ( "
				+ "SELECT idSponsorOrganization "
				+ "FROM SponsorContacts "
				+ "WHERE id = ? ) AND sa.idActivity = ?;";
	
	public static final String SQL_SA_ID = "SELECT sa.id "
				+ "FROM SponsorshipAgreements sa "
				+ "JOIN SponsorContacts sc ON sa.idSponsorContact = sc.id "
				+ "JOIN SponsorOrganizations so ON sc.idSponsorOrganization = so.id "
				+ "JOIN Activities a ON a.id = sa.idActivity "
				+ "WHERE (so.nif = ? OR so.vat = ?) AND (a.name = ?);";
    
	private Database db = new Database();

	// GETTERS
	
	public List<SponsorshipAgreementsDTO> getApplicableSponsorshipAgreementsByActivity(String idActivity) {
		SemanticValidations.validateIdForTable(idActivity, "Activities", "ERROR. Provided idActivity for getSponsorshipAgreementsByActivity does not exist.");
	    String sql = "SELECT * FROM SponsorshipAgreements WHERE status IN ('signed', 'closed') AND idActivity = ?;";
	    return db.executeQueryPojo(SponsorshipAgreementsDTO.class, sql, idActivity);
	}

    public double getEstimatedSponshorships(String idActivity) {
		SemanticValidations.validateIdForTable(idActivity, "Activities", "ERROR. Provided idActivity for getEstimatedSponshorships does not exist.");
		String sql = "SELECT SUM(amount) FROM SponsorshipAgreements WHERE status IN ('signed', 'closed') AND idActivity = ?;";
	    Object result = db.executeQueryArray(sql, idActivity).get(0)[0];
		if (result == null) {
			return 0.0;
		}
		return (double) result;
	}
	
	public double getActualSponshorships(String idActivity) {
		SemanticValidations.validateIdForTable(idActivity, "Activities", "ERROR. Provided idActivity for getActualSponshorships does not exist.");
		String sql = "SELECT SUM(amount) FROM SponsorshipAgreements WHERE status = 'closed' AND idActivity = ?;";
	    Object result = db.executeQueryArray(sql, idActivity).get(0)[0];
		if (result == null) {
			return 0.0;
		}
		return (double) result;
	}

    public int getNumberOldSponsorshipAgreements(String idSponsorContact, String idActivity) {
		List<Object[]> result = db.executeQueryArray(SQL_NUMBER_OLD_SA, idSponsorContact, idActivity);
		if (result == null || result.isEmpty()) {
			return 0;
		}
		return (int) result.get(0)[0];
	}

    public Integer getSponsorshipAgreementId(String nifOrVat, String activity) {
	    try {
	    	List<Object[]> result = db.executeQueryArray(SQL_SA_ID, nifOrVat, nifOrVat, activity);
		    if (result.isEmpty()) {
		    	throw new ApplicationException("No SponsorshipAgreement found");
		    } else {
		    	return Integer.parseInt(result.get(0)[0].toString());
		    }
	    } catch (Exception e) {
	    	throw new ApplicationException("Unexpected error while retrieving SponsorshipAgreement ID: " + e.getMessage());
	    }
	}
    
    public List<SponsorshipAgreementsDTO> getAgreementsByActivityName(String activityName) {
		SemanticValidations.validateName(activityName);
		String sql = "SELECT SA.* FROM SponsorshipAgreements SA JOIN Activities A ON SA.idActivity == A.id WHERE SA.status == 'signed' AND A.name == ?;";
		return db.executeQueryPojo(SponsorshipAgreementsDTO.class, sql, activityName);
	}

    // INSERTIONS

    public void insertNewSponsorshipAgreement(String idSponsorContact, String idGBMember, String idActivity, String amount, String date) throws UnexpectedException {
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

	// SPECIFIC VALIDATIONS

	private void validateDateForUpdateSponsorshipAgreement(String idSponsorContact, String idActivity, String date, String message) {
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
