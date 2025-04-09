package model;

import java.util.Collections;
import java.util.List;
import DTOs.ActivitiesDTO;
import util.Database;
import util.SemanticValidations;

public class ActivitiesModel {

	public static final String SQL_ACTIVITIES_FILTERED = "SELECT * FROM Activities "
			+ "WHERE name == ? "
			+ "AND edition == ?;";
	
	public static final String SQL_NUM_ACTIVITIES_FILTERED = "SELECT COUNT(id) FROM Activities "
			+ "WHERE name == ? "
			+ "AND edition == ?;";
	
    private Database db = new Database();

	// GETTERS
    public ActivitiesDTO getActivityByInvoice(String idInvoice) {
    	String sql = "SELECT a.* FROM Invoices i "
    			+ "JOIN SponsorshipAgreements sa ON i.idSponsorshipAgreement = sa.id "
    			+ "JOIN Activities a ON a.id = sa.idActivity "
    			+ "WHERE i.id = ?";
    	List<ActivitiesDTO> activities = db.executeQueryPojo(ActivitiesDTO.class, sql, idInvoice);
    	
    	return activities.get(0);
    }

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
    	String sql = "SELECT name || '-' || edition FROM Activities WHERE status IN ('planned', 'done');";
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
		String sql = "SELECT * FROM Activities WHERE name == ?;";
		List<ActivitiesDTO> activities = db.executeQueryPojo(ActivitiesDTO.class, sql, nameActivity);
		return activities.get(0);
	}

    public List<ActivitiesDTO> getActivitiesbyStatus(String... status) {
	    return getActivitiesbyStatus(true, status);
	}
    
    public List<ActivitiesDTO> getActivitiesbyStatus(boolean includeNull, String... status) {
	    String placeholders = String.join(",", Collections.nCopies(status.length, "?"));
	    String sql = "SELECT * FROM Activities WHERE status IN (" + placeholders + ")";
	    
	    if(!includeNull)
	    	sql += " AND dateStart != '' AND dateEnd != '' AND place != '';";
	    else sql += ";";
	    
	    return db.executeQueryPojo(ActivitiesDTO.class, sql, (Object[]) status);
	}
  
    public ActivitiesDTO getActivityByFilters(String name, String edition) {
    	SemanticValidations.validateName(name);
		SemanticValidations.validatePositiveNumberOrZero(edition, "It is not a valid number");
    	List<ActivitiesDTO> sol = db.executeQueryPojo(ActivitiesDTO.class, SQL_ACTIVITIES_FILTERED, name, edition);
		return sol.get(0);
	}
    
    public int getNumberActivitiesByFilters(String name, String edition) {
    	SemanticValidations.validateName(name);
		SemanticValidations.validatePositiveNumberOrZero(edition, "It is not a valid number");
		List<Object[]> result = db.executeQueryArray(SQL_NUM_ACTIVITIES_FILTERED, name, edition);
		if (result == null || result.isEmpty()) {
			return 0;
		}
		return (int) result.get(0)[0];
	}

	// INSERTIONS
    
    public void insertNewActivity(String name, String edition, String dateStart, String dateEnd, String place) {
		SemanticValidations.validateName(name);
		SemanticValidations.validatePositiveNumberOrZero(edition, "It is not a valid number");
		if (!dateStart.isBlank()) {
			SemanticValidations.validateDateBeforeTo(dateStart, dateEnd, true, "Incompatible dates");
		}
		if (!dateEnd.isBlank()) {
			SemanticValidations.validateDateAfterTo(dateEnd, dateStart, true, "Incompatible dates");
		}
		if (!place.isBlank()) {
			SemanticValidations.validateName(place);
		}
		
		String sql = "INSERT INTO Activities"
				+ "(name, edition, status, dateStart, dateEnd, place) VALUES "
				+ "(?, ?, 'planned', ?, ?, ?)";
		db.executeUpdate(sql, name, edition, dateStart, dateEnd, place);
	}
    
    public void closeActivity(ActivitiesDTO activity) {
		SemanticValidations.validateIdForTable(activity.getId(), "Activities", "ERROR. Provided idActivity for closeActivityById does not exist.");
		SemanticValidations.validateDateInPast(activity.getDateEnd(), true, "ERROR. It is not possible to close activities that were not celebrated.");
		
		String sql;
		
		sql = "UPDATE Activities SET status = 'closed' WHERE id = ?;";
		db.executeUpdate(sql, activity.getId());
	}
}
