package model;

import java.util.List;
import DTOs.ActivitiesDTO;
import DTOs.InvoicesDTO;
import DTOs.SponsorOrganizationsDTO;
import util.Database;
import util.SemanticValidations;

public class InvoiceManagementModel {
	
	public static final String SQL_FILTERED_INVOICES = "SELECT I.id, I.dateIssued, I.totalAmount FROM Invoices I "
			+ "JOIN SponsorshipAgreements SA ON I.idSponsorshipAgreement = SA.id "
			+ "JOIN SponsorContacts SC ON SA.idSponsorContact == SC.id "
			+ "WHERE SC.idSponsorOrganization == ? AND SA.idActivity == ?;";

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

	public SponsorOrganizationsDTO getSponsorByName(String nameSponsor) {
		SemanticValidations.validateName(nameSponsor);
		String sql = "SELECT id FROM SponsorOrganizations WHERE name == ?;";
		List<SponsorOrganizationsDTO> sponsors = db.executeQueryPojo(SponsorOrganizationsDTO.class, sql, nameSponsor);
		return sponsors.get(0);
	}
	
	public ActivitiesDTO getActivityByName(String nameActivity) {
		SemanticValidations.validateName(nameActivity);
		String sql = "SELECT id FROM Activities WHERE name == ?;";
		List<ActivitiesDTO> activities = db.executeQueryPojo(ActivitiesDTO.class, sql, nameActivity);
		return activities.get(0);
	}
	
	public List<InvoicesDTO> getInvoicesBySponsorAndActivity(String sponsorId, String activityId) {
		SemanticValidations.validateIdForTable(sponsorId, "SponsorOrganizations", "Not valid ID");
		SemanticValidations.validateIdForTable(activityId, "Activities", "Not valid ID");
        return db.executeQueryPojo(InvoicesDTO.class, SQL_FILTERED_INVOICES, sponsorId, activityId);
    }
}
