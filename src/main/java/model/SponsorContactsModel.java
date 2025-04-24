package model;

import java.util.List;
import DTOs.SponsorContactsDTO;
import util.Database;
import util.SemanticValidations;

public class SponsorContactsModel {

    private Database db = new Database();

	// GETTERS
    
    public List<Object[]> getValidContactsBySponsorOrganizationArray(String sponsorOrganizationId) {
    	SemanticValidations.validateIdForTable(sponsorOrganizationId, "SponsorOrganizations", "Not valid ID");
		String sql = "SELECT id || ' - ' || name AS item FROM SponsorContacts WHERE idSponsorOrganization == ? AND name != '<<Removed data>>' AND email != '<<Removed data>>' AND phone != '<<Removed data>>';";
	    return db.executeQueryArray(sql, sponsorOrganizationId);
	}
    
    public SponsorContactsDTO getContactById(String contactId) {
		SemanticValidations.validateIdForTable(contactId, "SponsorContacts", "ERROR. Provided contactId for getContactsById does not exist.");
		String sql = "SELECT * FROM SponsorContacts WHERE id = ?";
	    return db.executeQueryPojo(SponsorContactsDTO.class, sql, contactId).get(0);
	}
    
    public List<SponsorContactsDTO> getAllContacts() {
    	String sql = "SELECT * FROM SponsorContacts;";
	    return db.executeQueryPojo(SponsorContactsDTO.class, sql);
    }
    
    public SponsorContactsDTO getContactByFilters(String idSponsorOrganization, String name, String email, String phone) {
    	String sql = "SELECT * FROM SponsorContacts WHERE idSponsorOrganization = ? AND name = ? AND email = ? AND phone = ?;";
	    List<SponsorContactsDTO> sol = db.executeQueryPojo(SponsorContactsDTO.class, sql, idSponsorOrganization, name, email, phone);
	    return sol.get(0);
    }
    
    public List<SponsorContactsDTO> getAllValidContacts() {
    	String sql = "SELECT * FROM SponsorContacts WHERE name != '<<Removed data>>' AND email != '<<Removed data>>' AND phone != '<<Removed data>>';";
	    return db.executeQueryPojo(SponsorContactsDTO.class, sql);
    }
  
    public List<SponsorContactsDTO> getContactsBySponsorId(String sponsor) {
		String sql = "SELECT * FROM SponsorContacts WHERE idSponsorOrganization = ?;";
		return db.executeQueryPojo(SponsorContactsDTO.class, sql, sponsor);
	}

	// INSERTIONS
    
    public void insertContact(String idSponsorOrganization, String name, String email, String phone) {
        String query = "INSERT INTO SponsorContacts (idSponsorOrganization, name, email, phone) VALUES"
        		+ "(?, ?, ?, ?)";
		db.executeUpdate(query, idSponsorOrganization, name, email, phone);
    }

	public SponsorContactsDTO getContactByInvoiceId(String id) {
		SemanticValidations.validateIdForTable(id, "Invoices", "ERROR. Provided id for getContactByInvoiceId does not exist.");
		String sql = "SELECT SC.* FROM Invoices I"
				+ " JOIN SponsorshipAgreements SA ON I.idSponsorshipAgreement = SA.id"
				+ " JOIN SponsorContacts SC ON SA.idSponsorContact = SC.id"
				+ " WHERE I.id = ?";
	    return db.executeQueryPojo(SponsorContactsDTO.class, sql, id).get(0);
	}
	
    public void updateContact(String id, String name, String email, String phone) {
    	SemanticValidations.validateIdForTable(id, "SponsorContacts", "Not valid ID");
		SemanticValidations.validateName(name);
		
		String sql = "UPDATE SponsorContacts SET name = ?, email = ?, phone = ? WHERE id = ?;";
		db.executeUpdate(sql, name, email, phone, id);
	}
    
    public void removeContact(String id) {
    	SemanticValidations.validateIdForTable(id, "SponsorContacts", "Not valid ID");
		
		String sql = "UPDATE SponsorContacts SET name = '<<Removed data>>', email = '<<Removed data>>', phone = '<<Removed data>>' WHERE id = ?;";
		db.executeUpdate(sql, id);
	}
}
