package model;

import java.util.List;
import util.Database;

public class GBMembersModel {
    
    private Database db = new Database();

	// GETTERS

    public List<Object[]> getAllGBMembersArray() {
		String sql = "SELECT id || ' - ' || name AS item FROM GBMembers";
	    return db.executeQueryArray(sql);
	}

	// INSERTIONS
}
