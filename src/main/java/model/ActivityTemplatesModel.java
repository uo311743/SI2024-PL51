package model;

import java.util.List;
import util.ApplicationException;
import util.Database;
import util.SemanticValidations;

public class ActivityTemplatesModel {

    private Database db = new Database();

	// GETTERS
    
    public List<Object[]> getTemplatesListArray() {
    	String sql = "SELECT name FROM ActivityTemplates;";
		return db.executeQueryArray(sql);
	}
    
    public int getNumberTemplates() {
    	String sql = "SELECT COUNT(id) FROM ActivityTemplates;";
		List<Object[]> result = db.executeQueryArray(sql);
		if (result == null || result.isEmpty()) {
			return 0;
		}
		return (int) result.get(0)[0];
	}

    // INSERTIONS
    
    public void insertNewTemplate(String name) {
		SemanticValidations.validateName(name);
		
		if(getNumberTemplates() != 0)
			throw new ApplicationException("ERROR");
		
		String sql = "INSERT INTO Templates"
				+ "(name) VALUES "
				+ "(?)";
		db.executeUpdate(sql, name);
	}
}
