package model;

import java.util.List;
import util.Database;
import util.SemanticValidations;

public class ActivityTemplatesModel {
	
	private Database db = new Database();

	// GETTERS
    
    public List<Object[]> getTemplatesListArray() {
    	String sql = "SELECT name FROM ActivityTemplates;";
		return db.executeQueryArray(sql);
	}
    
    public int getNumberTemplatesByName(String nameActivity) {
		SemanticValidations.validateName(nameActivity);
		String sql = "SELECT COUNT(id) FROM ActivityTemplates WHERE name == ?;";
		List<Object[]> result = db.executeQueryArray(sql, nameActivity);
		if (result == null || result.isEmpty()) {
			return 0;
		}
		return (int) result.get(0)[0];
	}
    
    // INSERTIONS
    
    public void insertNewTemplate(String name) {
		SemanticValidations.validateName(name);
		
		String sql = "INSERT INTO ActivityTemplates"
				+ "(name) VALUES "
				+ "(?)";
		db.executeUpdate(sql, name);
	}
}
