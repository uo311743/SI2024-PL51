package model;

import java.util.List;

import DTOs.SponsorContactsDTO;
import util.Database;
import util.SemanticValidations;

public class SponsorContactsModel {

    private Database db = new Database();

	// GETTERS
    
    public List<Object[]> getContactsBySponshorArray(String sponshor) {
		String sql = "SELECT id || ' - ' || name AS item FROM SponsorContacts WHERE idSponsorOrganization == ?";
	    return db.executeQueryArray(sql, sponshor);
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

	// INSERTIONS
    
    public void insertContact(String idSponsorOrganization, String name, String email, String phone) {
        String query = "INSERT INTO SponsorContacts (idSponsorOrganization, name, email, phone) VALUES"
        		+ "(?, ?, ?, ?)";
		db.executeUpdate(query, idSponsorOrganization, name, email, phone);
    }
    
    public void updateContact(String idSponsorOrganization, String name, String email, String phone) {
    	SemanticValidations.validateIdForTable(idSponsorOrganization, "SponsorOrganizations", "Not valid ID");
		SemanticValidations.validateName(name);
		
		String sql = "UPDATE SponsorContacts SET name = ?, email = ?, phone = ? WHERE idSponsorOrganization = ?;";
		db.executeUpdate(sql, name, email, phone, idSponsorOrganization);
	}
}
