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
	
	public List<ActivitiesDTO> getActivitiesFromCurrentYear(String startDate, String endDate) {
		String sql ="SELECT id, name, status, dateStart, dateEnd FROM Activities WHERE dateStart >= ? AND dateEnd <= ?;";
		return db.executeQueryPojo(ActivitiesDTO.class, sql, startDate, endDate);
	}
    
    public MovementsDTO getAmountEstimatedIncomeByActivityId(String activityId) {
    	SemanticValidations.validateIdForTable(activityId, "Activities", "Not valid ID");
    	String sql = "SELECT amount FROM Movements WHERE status = 'estimated' AND type = 'income' AND idActivity = ?;";
        List<MovementsDTO> aei = db.executeQueryPojo(MovementsDTO.class, sql, activityId);
        return aei.get(0);
    }
    
    public MovementsDTO getAmountEstimatedExpenseByActivityId(String activityId) {
    	SemanticValidations.validateIdForTable(activityId, "Activities", "Not valid ID");
    	String sql = "SELECT amount FROM Movements WHERE status = 'estimated' AND type = 'expense AND idActivity = ?;";
        List<MovementsDTO> aee = db.executeQueryPojo(MovementsDTO.class, sql, activityId);
        return aee.get(0);
    }
    
    public MovementsDTO getAmountIncomeByActivityId(String activityId) {
    	SemanticValidations.validateIdForTable(activityId, "Activities", "Not valid ID");
    	String sql = "SELECT amount FROM Movements WHERE status = 'paid' AND type = 'income' AND idActivity = ?;";
        List<MovementsDTO> api = db.executeQueryPojo(MovementsDTO.class, sql, activityId);
        return api.get(0);
    }
    
    public MovementsDTO getAmountExpenseByActivityId(String activityId) {
    	SemanticValidations.validateIdForTable(activityId, "Activities", "Not valid ID");
    	String sql = "SELECT amount FROM Movements WHERE status = 'paid' AND type = 'expense' AND idActivity = ?;";
        List<MovementsDTO> ape = db.executeQueryPojo(MovementsDTO.class, sql, activityId);
        return ape.get(0);
    }
    
    public List<ActivitiesDTO> getFilteredActivities(Date startDate, Date endDate, String status) {
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
}
