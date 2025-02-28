package model;

import java.util.List;
import DTOs.InvoicesDTO;
import util.Database;

public class US125Model {
	
	// Instance that allows the connection to the DB and execution of queries
	private Database db = new Database();
	
	public static final String SQL_LIST_SPONSORS = "SELECT so.id AS sponsor_id, so.name AS sponsor_name, so.type AS sponsor_type, so.invoice_address, so.nif_vat, sc.id AS contact_id, sc.name AS contact_name, sc.email AS contact_email, sc.phone AS contact_phone, sa.id AS agreement_id, sa.amount AS agreement_amount, sa.date_agreement, sa.status AS agreement_status, i.id AS invoice_id, i.date_issue, i.date_send, sp.id AS payment_id, sp.date_received, sp.amount AS payment_amount FROM SponsorOrganizations so LEFT JOIN SponsorContact sc ON so.id = sc.sponsor_org_id LEFT JOIN SponsorshipAgreements sa ON so.id = sa.sponsor_org_id LEFT JOIN Invoices i ON sa.id = i.sponsorship_agreement_id LEFT JOIN SponsorshipPayments sp ON i.id = sp.invoice_id ORDER BY so.id, sa.id, i.id, sp.id;";
	public static final String SQL_LIST_ACTIVITIES = "SELECT a.id AS activity_id, a.name AS activity_name, a.state AS activity_state, a.date_celebration, a.place_celebration, a.estimated_income, a.estimated_expenses, l.id AS level_id, l.name AS level_name, l.fee AS level_fee, sa.id AS agreement_id, sa.amount AS sponsorship_amount, sa.date_agreement, sa.status AS sponsorship_status FROM Activities a LEFT JOIN ActivityLevel al ON a.id = al.activity_id LEFT JOIN Levels l ON al.level_id = l.id LEFT JOIN SponsorshipAgreements sa ON sa.id IN (SELECT sponsorship_agreement_id FROM Invoices WHERE id IN (SELECT invoice_id FROM SponsorshipPayments)) ORDER BY a.id, l.id, sa.id;";
    public static final String SQL_FILTERED_INVOICES = "SELECT i.id, i.date_issue, i.date_send, sa.amount FROM Invoices i JOIN SponsorshipAgreements sa ON i.sponsorship_agreement_id = sa.id JOIN SponsorOrganizations so ON sa.sponsor_org_id = so.id JOIN Activities a ON a.id = (SELECT activity_id FROM ActivityLevel WHERE level_id IN (SELECT id FROM Levels WHERE name = ?)) WHERE so.name = ?;";
	
	/* ================================================================================
     * 
     *     METHODS
     * 
     */
	

	
    /*
	public String getInvoiceRecipientCountry() {
        String sql = "SELECT country FROM Recipients WHERE id = (SELECT recipient_id FROM Invoices ORDER BY date_issue DESC LIMIT 1);";
        return db.executeQuerySingle(String.class, sql);
    }
    */
	
	public List<Object[]> getSponsorListArray() {
		String sql = SQL_LIST_SPONSORS;
		return db.executeQueryArray(sql);
	}
    
	public List<Object[]> getActivityListArray() {
		String sql = SQL_LIST_ACTIVITIES;
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
