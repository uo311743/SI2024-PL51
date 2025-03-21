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
