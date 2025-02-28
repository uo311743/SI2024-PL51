package model;

import java.util.List;
import DTOs.InvoicesDTO;
import util.Database;

public class US125Model {
	
	public static final String SQL_FILTERED_INVOICES = "SELECT i.id, i.date_issue, i.date_send, sa.amount FROM Invoices i JOIN SponsorshipAgreements sa ON i.sponsorship_agreement_id = sa.id JOIN SponsorOrganizations so ON sa.sponsor_org_id = so.id JOIN Activities a ON a.id = (SELECT activity_id FROM ActivityLevel WHERE level_id IN (SELECT id FROM Levels WHERE name = ?)) WHERE so.name = ?;";

	private Database db = new Database();
		
	/* ================================================================================
     * 
     *     METHODS
     * 
     */
	
	public List<Object[]> getSponsorListArray() {
		String sql = "SELECT Name FROM SponsorOrganizations;";
		return db.executeQueryArray(sql);
	}
    
	public List<Object[]> getActivityListArray() {
		String sql = "SELECT Name FROM Activities;";
		return db.executeQueryArray(sql);
	}
	
    public List<InvoicesDTO> getDateIssueInvoices() {
        String sql = "SELECT DateIssue FROM Invoices;";
        return db.executeQueryPojo(InvoicesDTO.class, sql);
    }
    
    public List<InvoicesDTO> getIdInvoices() {
        String sql = "SELECT id FROM Invoices;";
        return db.executeQueryPojo(InvoicesDTO.class, sql);
    }

    public List<InvoicesDTO> getInvoicesBySponsorAndActivity(String sponsor, String activity) {
        return db.executeQueryPojo(InvoicesDTO.class, SQL_FILTERED_INVOICES, activity, sponsor);
    }
    
    public InvoicesDTO getAmountByIdInvoices(String invoiceId) {
    	String sql = "SELECT amount FROM SponsorshipPayments WHERE invoice_id = ?";
        List<InvoicesDTO> invoices = db.executeQueryPojo(InvoicesDTO.class, sql, invoiceId);
        return invoices.get(0);
    }
}
