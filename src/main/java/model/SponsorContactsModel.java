package model;

import java.util.List;
import util.Database;

public class SponsorContactsModel {

    private Database db = new Database();

	// GETTERS
    
    public List<Object[]> getContactsBySponshorArray(String sponshor) {
		String sql = "SELECT id || ' - ' || name AS item FROM SponsorContacts WHERE idSponsorOrganization == ?";
	    return db.executeQueryArray(sql, sponshor);
	}

	// INSERTIONS
}
