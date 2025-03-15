package model;

import java.util.List;
import DTOs.SponsorOrganizationsDTO;
import util.Database;
import util.SemanticValidations;

public class SponsorOrganizationsModel {

	public static final String SQL_SO_INFO = "SELECT SO.* FROM Invoices I "
			+ "JOIN SponsorshipAgreements SA ON SA.idSponsorContact = SA.id "
			+ "JOIN SponsorContacts SC ON SC.idSponsorOrganization = SO.id "
			+ "JOIN SponsorOrganizations SO ON SC.idSponsorOrganization = SO.id "
			+ "WHERE I.id = ?;";

	public static final String SQL_SO_ID_SC = "SELECT SO.* FROM SponsorOrganizations SO "
			+ "JOIN SponsorContacts SC ON SO.id = SC.idSponsorOrganization "
			+ "WHERE SC.id = ?;";
	
	public static final String SQL_SO_ID_SA = "SELECT SO.* FROM SponsorOrganizations SO "
			+ "JOIN SponsorContacts SC ON SC.idSponsorOrganization == SO.id "
			+ "JOIN SponsorshipAgreements SA ON SA.idSponsorContact == SC.id "
			+ "WHERE SA.id ==?;";

    private Database db = new Database();

	// GETTERS

	public List<Object[]> getAllSponsorsArray() {
	    String sql = "SELECT id || ' - ' || name AS item FROM SponsorOrganizations";
	    return db.executeQueryArray(sql);
	}

	public List<Object[]> getSponsorListArray() {
		String sql = "SELECT name FROM SponsorOrganizations;";
		return db.executeQueryArray(sql);
	}
    
	public SponsorOrganizationsDTO getSponsorOrganizationByInvoiceId(String invoiceId) {
		SemanticValidations.validateIdForTable(invoiceId, "Invoices", "Not valid ID");
		List<SponsorOrganizationsDTO> data = db.executeQueryPojo(SponsorOrganizationsDTO.class, SQL_SO_INFO, invoiceId);
		return data.get(0);
	}
	
	// Review
    public SponsorOrganizationsDTO getSponsorOrganizationByIdSponsorContact(String idSponsorContact) {
		SemanticValidations.validateIdForTable(idSponsorContact, "SponsorContacts", "ERROR. Provided idSponsorContact for getSponsorOrganizationByID does not exist.");
	    return db.executeQueryPojo(SponsorOrganizationsDTO.class, SQL_SO_ID_SC, idSponsorContact).get(0);
	}

	public SponsorOrganizationsDTO getSponsorByName(String nameSponsor) {
		SemanticValidations.validateName(nameSponsor);
		String sql = "SELECT id FROM SponsorOrganizations WHERE name == ?;";
		List<SponsorOrganizationsDTO> sponsors = db.executeQueryPojo(SponsorOrganizationsDTO.class, sql, nameSponsor);
		return sponsors.get(0);
	}

	public SponsorOrganizationsDTO getSponsorOrganizationByAgreementId(String idSA) {
		List<SponsorOrganizationsDTO> sponsors = db.executeQueryPojo(SponsorOrganizationsDTO.class, SQL_SO_ID_SA, idSA);
		return sponsors.get(0);
	}

	// INSERTIONS
}
