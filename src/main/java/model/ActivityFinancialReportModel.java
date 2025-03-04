package model;

import java.util.Date;
import java.util.List;
import DTOs.ActivitiesDTO;
import DTOs.MovementsDTO;
import util.Database;
import util.SemanticValidations;

public class ActivityFinancialReportModel {
	
	private Database db = new Database();
    
	/* ================================================================================
     * 
     *     METHODS
     * 
     */
	
	public List<Object[]> getStatusListArray() {
		String sql = "SELECT name FROM SponsorOrganizations;";
		return db.executeQueryArray(sql);
	}
	
	public List<ActivitiesDTO> getActivitiesFromCurrentYear(String startDate, String endDate) {
		String sql ="SELECT name, status, dateStart, dateEnd FROM Activities WHERE dateStart >= ? AND dateEnd <= ?;";
		return db.executeQueryPojo(ActivitiesDTO.class, sql, startDate, endDate);
	}
    
    public MovementsDTO getAmountEstimatedIncomeByActivityId(String activityId) {
    	SemanticValidations.validateIdForTable(activityId, "Activities", "Not valid ID");
    	String sql = "SELECT m.amount FROM Movements m JOIN Activities a ON m.idActivity == a.id WHERE m.status == 'estimated' AND m.type == 'income' AND m.idActivity == ?;";
        List<MovementsDTO> aei = db.executeQueryPojo(MovementsDTO.class, sql, activityId);
        return aei.get(0);
    }
    
    public MovementsDTO getAmountEstimatedExpenseByActivityId(String activityId) {
    	SemanticValidations.validateIdForTable(activityId, "Activities", "Not valid ID");
    	String sql = "SELECT m.amount FROM Movements m JOIN Activities a ON m.idActivity == a.id WHERE m.status == 'estimated' AND m.type == 'expense' AND m.idActivity == ?;";
        List<MovementsDTO> aee = db.executeQueryPojo(MovementsDTO.class, sql, activityId);
        return aee.get(0);
    }
    
    public MovementsDTO getAmountIncomeByActivityId(String activityId) {
    	SemanticValidations.validateIdForTable(activityId, "Activities", "Not valid ID");
    	String sql = "SELECT m.amount FROM Movements m JOIN Activities a ON m.idActivity == a.id WHERE m.status == 'paid' AND m.type == 'income' AND m.idActivity == ?;";
        List<MovementsDTO> api = db.executeQueryPojo(MovementsDTO.class, sql, activityId);
        return api.get(0);
    }
    
    public MovementsDTO getAmountExpenseByActivityId(String activityId) {
    	SemanticValidations.validateIdForTable(activityId, "Activities", "Not valid ID");
    	String sql = "SELECT m.amount FROM Movements m JOIN Activities a ON m.idActivity == a.id WHERE m.status == 'paid' AND m.type == 'expense' AND m.idActivity == ?;";
        List<MovementsDTO> ape = db.executeQueryPojo(MovementsDTO.class, sql, activityId);
        return ape.get(0);
    }
    
    public List<ActivitiesDTO> getFilteredActivities(Date startDate, Date endDate, String status) {
    	SemanticValidations.validateStatus(status);
        String sql = "SELECT name, status, dateStart, dateEnd FROM Activities WHERE dateStart >= ? AND dateEnd <= ?";
        
        if (status == null) {
        	sql += ";";
        	return db.executeQueryPojo(ActivitiesDTO.class, sql, startDate, endDate);
        }
        else {
        	sql += " AND status == ?;";
        	return db.executeQueryPojo(ActivitiesDTO.class, sql, startDate, endDate, status);
        }
    }
}
