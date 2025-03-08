package model;

import java.rmi.UnexpectedException;
import java.sql.Date;
import java.util.List;
import DTOs.InvoicesDTO;
import util.ApplicationException;
import util.Database;
import util.SemanticValidations;

public class InvoicesModel {
	
	public static final String SQL_FILTERED_INVOICES = "SELECT I.id, I.dateIssued, I.totalAmount FROM Invoices I "
			+ "JOIN SponsorshipAgreements SA ON I.idSponsorshipAgreement = SA.id "
			+ "JOIN SponsorContacts SC ON SA.idSponsorContact == SC.id "
			+ "WHERE SC.idSponsorOrganization == ? AND SA.idActivity == ?;";
	
	public static final String SQL_NUMBER_OLD_INVOICES = "SELECT COUNT(I.id) FROM Invoices I "
			+ "JOIN SponsorshipAgreements SA ON I.idSponsorshipAgreement == SA.id "
			+ "JOIN Activities A ON SA.idActivity == A.id "
			+ "WHERE A.name == ?;";
	
    private Database db = new Database();

	// GETTERS

    public List<InvoicesDTO> getInvoices() {
		String sql ="SELECT id, dateIssued, totalAmount FROM Invoices;";
		return db.executeQueryPojo(InvoicesDTO.class, sql);
	}

    public List<InvoicesDTO> getInvoicesBySponsorAndActivity(String sponsorId, String activityId) {
		SemanticValidations.validateIdForTable(sponsorId, "SponsorOrganizations", "Not valid ID");
		SemanticValidations.validateIdForTable(activityId, "Activities", "Not valid ID");
        return db.executeQueryPojo(InvoicesDTO.class, SQL_FILTERED_INVOICES, sponsorId, activityId);
    }

    public String getInvoiceId(Integer idSponsorshipAgreement) {
		String sql = "SELECT i.id FROM Invoices i WHERE i.idSponsorshipAgreement = ?;";
		try {
			List<Object[]> result = db.executeQueryArray(sql, idSponsorshipAgreement);
			if (result.isEmpty()) {
				throw new ApplicationException("No Invoice Found");
			} else {
				return result.get(0)[0].toString();
			}
		} catch (Exception e) {
			throw new ApplicationException("Unexpected error while retrieving Invoice ID: " + e.getMessage());
		}
	}
    
    public int getNumberOldInvoicesByActivityName(String nameActivity) {
		List<Object[]> result = db.executeQueryArray(SQL_NUMBER_OLD_INVOICES, nameActivity);
		if (result == null || result.isEmpty()) {
			return 0;
		}
		return (int) result.get(0)[0];
	}

	// INSERTIONS
    
    public void insertNewSponsorshipAgreement(String idSponsorshipAgreement, String dateIssued, String dateSent, String dateExpiration, String totalAmount, String taxRate) throws UnexpectedException {
		SemanticValidations.validateIdForTable(idSponsorContact, "SponsorContacts", "Not valid");
		SemanticValidations.validateIdForTable(idGBMember, "GBMembers", "ERROR. Tried to insert a Sponsorship agreement with an unexisting idGBMember.");
		SemanticValidations.validateIdForTable(idActivity, "Activities", "ERROR. Tried to insert a Sponsorship agreement with an unexisting idActivity.");
		SemanticValidations.validatePositiveNumber(amount, "ERROR. Tried to insert a Sponsorship agreement with a non-positive amount.");
		SemanticValidations.validateDateInPast(date, true, "ERROR. Tried to insert a Sponsorship agreement with a future date.");
		
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
    
    public void validatePaymentDate(String date, Integer invoiceId) {
        String query = "SELECT i.dateIssued FROM Invoices i WHERE i.id = ?;";
        try {
        	List<Object[]> result = db.executeQueryArray(query, invoiceId);
            if (result.isEmpty()) {
            	throw new ApplicationException("No Invoice Found");
            } else {
            	Date invoiceIssuedDate = Date.valueOf(result.get(0)[0].toString());
            	Date paymentDate = Date.valueOf(date);
            	validateCondition(invoiceIssuedDate.before(paymentDate), "Payment cannot be made before Invoice generation");
            }
        } catch (Exception e) {
        	e.printStackTrace();
        	throw new ApplicationException(e.getMessage());
        }
    }
    
    private void validateCondition(boolean condition, String message) {
		if (!condition) throw new ApplicationException(message);
	}
}
