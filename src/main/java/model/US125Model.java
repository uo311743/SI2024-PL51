package model;

import java.util.List;
import DTOs.InvoicesDTO;
import util.Database;

public class US125Model {
	
	public static final String SQL_FILTERED_INVOICES = "SELECT i.id, i.dateIssued, i.totalAmount FROM Invoices i JOIN SponsorshipAgreements sa ON i.id = sa.id JOIN SponsorContacts sc ON sa.id = sc.id JOIN SponsorOrganizations so ON sc.id = so.id JOIN Activities a ON sa.id = a.id WHERE so.id = ? AND a.id = ?;";

	private Database db = new Database();
		
	/* ================================================================================
     * 
     *     METHODS
     * 
     */
	
	public List<Object[]> getSponsorListArray() {
		String sql = "SELECT name FROM SponsorOrganizations;";
		return db.executeQueryArray(sql);
	}
    
	public List<Object[]> getActivityListArray() {
		String sql = "SELECT name FROM Activities;";
		return db.executeQueryArray(sql);
	}
	
	public List<InvoicesDTO> getInvoices() {
		String sql ="SELECT id, dateIssued, totalAmount FROM Invoices;";
		return db.executeQueryPojo(InvoicesDTO.class, sql);
	}

	public String getSponsorIdByName(String nameSponsor) {
		String sql = "SELECT id FROM SponsorOrganizations WHERE name = ?;";
		List<String> sponsors = db.executeQueryPojo(String.class, sql, nameSponsor);
		return sponsors.get(0);
	}
	
	public String getActivityIdByName(String nameActivity) {
		String sql = "SELECT id FROM Activities WHERE name = ?;";
		List<String> activities = db.executeQueryPojo(String.class, sql, nameActivity);
		return activities.get(0);
	}
	
	public List<InvoicesDTO> getInvoicesBySponsorAndActivity(String sponsorId, String activityId) {
        return db.executeQueryPojo(InvoicesDTO.class, SQL_FILTERED_INVOICES, sponsorId, activityId);
    }
}
