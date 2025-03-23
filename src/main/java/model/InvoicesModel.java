package model;

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
	public static final String SQL_FILTERED_INVOICES_BY_SPONSOR = "SELECT I.* FROM Invoices I "
			+ "JOIN SponsorshipAgreements SA ON I.idSponsorshipAgreement = SA.id "
			+ "JOIN SponsorContacts SC ON SA.idSponsorContact == SC.id "
			+ "WHERE SC.idSponsorOrganization == ?;";
	
	public static final String SQL_NUMBER_INVOICES_ACTIVITY = "SELECT COUNT(I.id) FROM Invoices I "
			+ "JOIN SponsorshipAgreements SA ON I.idSponsorshipAgreement == SA.id "
			+ "JOIN Activities A ON SA.idActivity == A.id "
			+ "WHERE A.name == ?;";
	
	public static final String SQL_NUMBER_INVOICES_SA = "SELECT COUNT(I.id) FROM Invoices I "
			+ "JOIN SponsorshipAgreements SA ON I.idSponsorshipAgreement == SA.id "
			+ "WHERE SA.id == ?;";
	
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
		List<Object[]> result = db.executeQueryArray(SQL_NUMBER_INVOICES_ACTIVITY, nameActivity);
		if (result == null || result.isEmpty()) {
			return 0;
		}
		return (int) result.get(0)[0];
	}
    
    public int getNumberOldInvoicesBySponsorshipAgreementsId(String idSA) {
		List<Object[]> result = db.executeQueryArray(SQL_NUMBER_INVOICES_ACTIVITY, idSA);
		if (result == null || result.isEmpty()) {
			return 0;
		}
		return (int) result.get(0)[0];
	}
    
    public List<InvoicesDTO> getInvoicesBySponsor(String sponsorId) {
		SemanticValidations.validateIdForTable(sponsorId, "SponsorOrganizations", "Not valid ID");
        return db.executeQueryPojo(InvoicesDTO.class, SQL_FILTERED_INVOICES_BY_SPONSOR, sponsorId);
    }
    
    // SETTERS - UPDATES
    public void setInvoiceStatus(String invoiceId, String status)
    {
    	// List of allowed status
        String[] validStatus = { "draft", "issued", "paid", "rectified", "cancelled" };
        
        String sql = "UPDATE Invoices SET status = ? WHERE id = ?";
        
    	try {
    		SemanticValidations.validateStatus(status);
    		SemanticValidations.validateAllowedValues(status, validStatus, "Not a valid status");
    		db.executeUpdate(sql, status, Integer.parseInt(invoiceId));
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

	// INSERTIONS
    
    public void insertNewInvoice(String idSponsorshipAgreement, String dateIssued, String dateSent, String dateExpiration, String totalAmount, String taxRate) {
		SemanticValidations.validateIdForTable(idSponsorshipAgreement, "SponsorshipAgreements", "Not valid ID");
		SemanticValidations.validatePositiveNumberOrZero(totalAmount, "Not valid number");
		SemanticValidations.validateNumberInRange(taxRate, "0.0", "100.0", "Not valid number (0-100)");
		
		if(getNumberOldInvoicesBySponsorshipAgreementsId(idSponsorshipAgreement) != 0)
			throw new ApplicationException("Args are from an old sponsorship agreement");
		
		String sql = "INSERT INTO Invoices"
				+ "(idSponsorshipAgreement, dateIssued, dateSent, dateExpiration, totalAmount, taxRate, status) VALUES "
				+ "(?, ?, ?, ?, ?, ?, 'issued')";
		db.executeUpdate(sql, idSponsorshipAgreement, dateIssued, dateSent, dateExpiration, totalAmount, taxRate);
	}

    public void insertUpdateInvoice(String idSponsorshipAgreement, String dateIssued, String dateSent, String dateExpiration, String totalAmount, String taxRate) {
    	SemanticValidations.validateIdForTable(idSponsorshipAgreement, "SponsorshipAgreements", "Not valid ID");
		SemanticValidations.validatePositiveNumberOrZero(totalAmount, "Not valid number");
		SemanticValidations.validateNumberInRange(taxRate, "0.0", "1.0", "Not valid number (0-1)");
		
		this.validateDateForUpdateInvoices(dateIssued, idSponsorshipAgreement);
		
		String sql = "INSERT INTO Invoices"
				+ "(idSponsorshipAgreement, dateIssued, dateSent, dateExpiration, totalAmount, taxRate, status) VALUES "
				+ "(?, ?, ?, ?, ?, ?, 'issued')";
		db.executeUpdate(sql, idSponsorshipAgreement, dateIssued, dateSent, dateExpiration, totalAmount, taxRate);
		
		sql = "UPDATE Invoices "
				+ "SET status = 'rectified' "
				+ "WHERE idSponsorshipAgreement = ?;";
		db.executeUpdate(sql, idSponsorshipAgreement);
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
    
    public void validateDateForUpdateInvoices(String date, String idAgreement) {
    	String query = "SELECT date FROM SponsorshipAgreements WHERE id = ?;";
        try {
        	List<Object[]> result = db.executeQueryArray(query, idAgreement);
            if (result.isEmpty()) {
            	throw new ApplicationException("No Agreement Found");
            } 
            else {
            	Date agreementDate = Date.valueOf(result.get(0)[0].toString());
            	Date invoiceDate = Date.valueOf(date);
            	validateCondition(agreementDate.before(invoiceDate), "Invoice cannot be made before the agreement generation");
            }
        } 
        catch (Exception e) {
        	e.printStackTrace();
        	throw new ApplicationException(e.getMessage());
        }
    }
}
