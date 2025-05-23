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
	
	public static final String SQL_NUMBER_INVOICES_ACTIVITY = "SELECT COUNT(id) FROM Invoices "
			+ "WHERE idSponsorshipAgreement == ?;";
	
	public static final String SQL_NUMBER_INVOICES = "SELECT COUNT(id) FROM Invoices "
			+ "WHERE id == ?;";
	
	private Database db = new Database();

	// GETTERS

    public List<InvoicesDTO> getInvoices() {
		String sql ="SELECT id, dateIssued, totalAmount FROM Invoices;";
		return db.executeQueryPojo(InvoicesDTO.class, sql);
	}
    
    public InvoicesDTO getInvoiceById(String id) {
		SemanticValidations.validateIdForTable(id, "Invoices", "Arg id for getInvoiceById is a non-valid or non-existing id for Invoices table");
		String sql ="SELECT * FROM Invoices WHERE id == ?;";
		
		return db.executeQueryPojo(InvoicesDTO.class, sql, id).get(0);
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
    
    public int getNumberInvoicesByAgreement(String idActivity) {
    	SemanticValidations.validateIdForTable(idActivity, "Activities", "Not valid ID");
		List<Object[]> result = db.executeQueryArray(SQL_NUMBER_INVOICES_ACTIVITY, idActivity);
		if (result == null || result.isEmpty()) {
			return 0;
		}
		return (int) result.get(0)[0];
	}
    
    public List<InvoicesDTO> getInvoicesBySponsor(String sponsorId) {
		SemanticValidations.validateIdForTable(sponsorId, "SponsorOrganizations", "Not valid ID");
        return db.executeQueryPojo(InvoicesDTO.class, SQL_FILTERED_INVOICES_BY_SPONSOR, sponsorId);
    }
    
    public double getTotalAmount(String idInvoice) {
    	SemanticValidations.validateIdForTable(idInvoice, "Invoices", "ERROR. Provided idInvoice for getTotalAmount does not exist.");
		String sql = "SELECT totalAmount FROM Invoices WHERE id = ?;";
	    Object result = db.executeQueryArray(sql, idInvoice).get(0)[0];
		return (result == null) ? 0.0 : (double) result;
    }
    
    public int getNumberInvoices(String id) {
		List<Object[]> result = db.executeQueryArray(SQL_NUMBER_INVOICES, id);
		if (result == null || result.isEmpty()) {
			return 0;
		}
		return (int) result.get(0)[0];
    }
    
    // SETTERS - UPDATES
    public void setInvoiceStatus(String invoiceId, String status)
    {
    	// List of allowed status
        String[] validStatus = { "issued", "paid", "rectified", "cancelled" };
        
        String sql = "UPDATE Invoices SET status = ? WHERE id = ?";
        
    	try {
    		SemanticValidations.validateStatus(status);
    		SemanticValidations.validateAllowedValues(status, validStatus, "Not a valid status");
    		db.executeUpdate(sql, status, invoiceId);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

	// INSERTIONS    
    public void insertNewInvoice(String id, String idSponsorshipAgreement, String dateIssued, String totalAmount, String taxRate, String dateAgreement) {
		SemanticValidations.validateIdForTable(idSponsorshipAgreement, "SponsorshipAgreements", "Not valid ID");
		SemanticValidations.validateDateAfterTo(dateIssued, dateAgreement, true, "Not valid date");
		SemanticValidations.validateDateInFuture(dateIssued, false, "Not valid date");
		SemanticValidations.validatePositiveNumberOrZero(totalAmount, "Not valid number");
		SemanticValidations.validateNumberInRange(taxRate, "0.0", "100.0", "Not valid number (0-100)");
		
		String sql = "INSERT INTO Invoices"
				+ "(id, idSponsorshipAgreement, dateIssued, totalAmount, taxRate, status) VALUES "
				+ "(?, ?, ?, ?, ?, 'issued')";
		db.executeUpdate(sql, id, idSponsorshipAgreement, dateIssued, totalAmount, taxRate);
	}

    public void updateInsertInvoice(String id, String idSponsorshipAgreement, String dateIssued, String totalAmount, String taxRate, String dateAgreement) {
    	SemanticValidations.validateIdForTable(idSponsorshipAgreement, "SponsorshipAgreements", "Not valid ID");
		SemanticValidations.validateDateAfterTo(dateIssued, dateAgreement, true, "Not valid date");
		SemanticValidations.validateDateInFuture(dateIssued, false, "Not valid date");
		SemanticValidations.validatePositiveNumberOrZero(totalAmount, "Not valid number");
		SemanticValidations.validateNumberInRange(taxRate, "0.0", "100.0", "Not valid number (0-100)");
				
		String sql = "UPDATE Invoices "
				+ "SET status = 'rectified' "
				+ "WHERE idSponsorshipAgreement = ?;";
		db.executeUpdate(sql, idSponsorshipAgreement);
		
		sql = "INSERT INTO Invoices"
				+ "(id, idSponsorshipAgreement, dateIssued, totalAmount, taxRate, status) VALUES "
				+ "(?, ?, ?, ?, ?, 'issued')";
		db.executeUpdate(sql, id, idSponsorshipAgreement, dateIssued, totalAmount, taxRate);
	}
    
    // SPECIFIC VALIDATIONS
    
    public void validatePaymentDate(String date, String invoiceId) {
        String query = "SELECT i.dateIssued FROM Invoices i WHERE i.id = ?;";
        try {
        	List<Object[]> result = db.executeQueryArray(query, invoiceId);
            if (result.isEmpty()) {
            	throw new ApplicationException("No Invoice Found");
            } else {
            	String invoiceIssuedDate = result.get(0)[0].toString();
            	String paymentDate = date;
            	SemanticValidations.validateDateAfterTo(paymentDate, invoiceIssuedDate, true, "Payment cannot be made before Invoice generation");
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
