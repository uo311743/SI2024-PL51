package model;

import java.util.Collections;
import java.util.List;
import DTOs.ActivitiesDTO;
import util.Database;
import util.SemanticValidations;

public class ActivitiesModel {

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
		String sql = "SELECT name FROM Activities WHERE status IN ('planned', 'done')";
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

	public void closeActivityById(String idActivity) {
		SemanticValidations.validateIdForTable(idActivity, "Activities", "ERROR. Provided idActivity for closeActivityById does not exist.");
		
		String sql;
		
		sql = "UPDATE Activities SET status = 'closed' WHERE id = ?;";
		db.executeUpdate(sql, idActivity);
		
		sql = "UPDATE Movements SET status = 'cancelled' WHERE idActivity = ? AND status = 'estimated';";
		db.executeUpdate(sql, idActivity);
		
		sql = "UPDATE SponsorshipAgreements SET status = 'cancelled' WHERE idActivity = ? AND status = 'signed';";
		db.executeUpdate(sql, idActivity);
		
		sql = "UPDATE Invoices SET status = 'cancelled'"
				+ "WHERE idSponsorshipAgreement IN ("
				+ "    SELECT id "
				+ "    FROM SponsorshipAgreements "
				+ "    WHERE idActivity = ?"
				+ ") AND status = 'signed';";
		db.executeUpdate(sql, idActivity);
	}

	// INSERTIONS
}
