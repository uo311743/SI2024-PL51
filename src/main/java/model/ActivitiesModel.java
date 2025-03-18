package model;

import java.util.Collections;
import java.util.List;
import DTOs.ActivitiesDTO;
import util.ApplicationException;
import util.Database;
import util.SemanticValidations;

public class ActivitiesModel {

	public static final String SQL_ACTIVITIES_FILTERED = "SELECT * FROM Activities "
			+ "WHERE name == ? "
			+ "AND edition == ? "
			+ "AND dateStart == ? "
			+ "AND dateEnd == ? "
			+ "AND place == ?;";
	
    private Database db = new Database();

	// GETTERS

    public List<ActivitiesDTO> getActivitiesFromCurrentYear(String startDate, String endDate) {
		String sql ="SELECT id, name, status, dateStart, dateEnd FROM Activities WHERE dateStart >= ? AND dateEnd <= ?;";
		return db.executeQueryPojo(ActivitiesDTO.class, sql, startDate, endDate);
	}

	public String getActivityId(String name) {
    	String sql = "SELECT a.id FROM Activities a WHERE a.name = ?;";
    	return db.executeQueryArray(sql, name).get(0)[0].toString();
    }

	public List<ActivitiesDTO> getAllActivities() {
		String sql = "SELECT * FROM Activities;";
	    return db.executeQueryPojo(ActivitiesDTO.class, sql);
	}

	// getListActivities()
    public List<Object[]> getActiveActivityListArray() {
		String sql = "SELECT name FROM Activities WHERE status IN ('planned', 'done');";
		return db.executeQueryArray(sql);
	}
    
    public List<ActivitiesDTO> getFilteredActivities(String startDate, String endDate, String status) {
    	SemanticValidations.validateStatus(status);
        String sql = "SELECT id, name, status, dateStart, dateEnd FROM Activities WHERE dateStart >= ? AND dateEnd <= ?";
        if (status == "all") {
        	sql += ";";
        	return db.executeQueryPojo(ActivitiesDTO.class, sql, startDate, endDate);
        }
        else {
        	sql += " AND status = ?;";
        	return db.executeQueryPojo(ActivitiesDTO.class, sql, startDate, endDate, status);
        }
    }

    public ActivitiesDTO getActivityByName(String nameActivity) {
		SemanticValidations.validateName(nameActivity);
		String sql = "SELECT id FROM Activities WHERE name == ?;";
		List<ActivitiesDTO> activities = db.executeQueryPojo(ActivitiesDTO.class, sql, nameActivity);
		return activities.get(0);
	}

    public List<ActivitiesDTO> getActivitiesbyStatus(String... status) {
	    String placeholders = String.join(",", Collections.nCopies(status.length, "?"));
	    String sql = "SELECT * FROM Activities WHERE status IN (" + placeholders + ")";
	    return db.executeQueryPojo(ActivitiesDTO.class, sql, (Object[]) status);
	}
    
    public int getNumberActivities() {
    	String sql = "SELECT COUNT(id) FROM Activities;";
		List<Object[]> result = db.executeQueryArray(sql);
		if (result == null || result.isEmpty()) {
			return 0;
		}
		return (int) result.get(0)[0];
	}
    
    public ActivitiesDTO getActivityByFilters(String name, String edition, String dateStart, String dateEnd, String place) {
		return db.executeQueryPojo(ActivitiesDTO.class, SQL_ACTIVITIES_FILTERED, name, edition, dateStart, dateEnd, place).get(0);
	}
   
	// INSERTIONS
    
    public void insertNewActivity(String name, String edition, String dateStart, String dateEnd, String place) {
		SemanticValidations.validateName(name);
		SemanticValidations.validatePositiveNumber(edition, "It is not a valid number");
		SemanticValidations.validateDateBeforeTo(dateStart, dateEnd, true, "Incompatible dates");
		SemanticValidations.validateDateAfterTo(dateEnd, dateStart, true, "Incompatible dates");
		SemanticValidations.validateName(place);
		
		if(getNumberActivities() != 0)
			throw new ApplicationException("ERROR");
		
		String sql = "INSERT INTO Activities"
				+ "(name, edition, status, dateStart, dateEnd, place) VALUES "
				+ "(?, ?, 'planned', ?, ?, ?)";
		db.executeUpdate(sql, name, edition, dateStart, dateEnd, place);
	}
}
