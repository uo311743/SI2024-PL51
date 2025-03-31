package model;

import java.util.List;
import DTOs.LevelsDTO;
import util.Database;
import util.SemanticValidations;

public class LevelsModel {
	
	public static final String SQL_LEVELS = "SELECT * FROM Levels "
			+ "WHERE idActivity == ?;";
	
	public static final String SQL_NUMBER_LEVELS_ACTIVITY = "SELECT COUNT(L.id) FROM Levels L "
			+ "JOIN Activities A "
			+ "ON L.idActivity == A.id "
			+ "WHERE A.id == ?;";

    private Database db = new Database();

	// GETTERS
    
    public List<LevelsDTO> getLevelsByActivityId(String activityId) {
		SemanticValidations.validateIdForTable(activityId, "Levels", "Not valid ID");
		return db.executeQueryPojo(LevelsDTO.class, SQL_LEVELS, activityId);
	}
    
    public int getNumberLevelsByActivityId(String idActivity) {
		List<Object[]> result = db.executeQueryArray(SQL_NUMBER_LEVELS_ACTIVITY, idActivity);
		if (result == null || result.isEmpty()) {
			return 0;
		}
		return (int) result.get(0)[0];
	}
    
    public List<Object[]> getLevelsListArray(String activityId) {
    	String sql = "SELECT name FROM Levels WHERE idActivity = ?;";
		return db.executeQueryArray(sql, activityId);
	}
    
    public LevelsDTO getLevelsByActivityIdAndLevelName(String activityId, String levelName) {
    	SemanticValidations.validateIdForTable(activityId, "Activities", "Not valid ID");
    	SemanticValidations.validateName(levelName);
    	String sql = "SELECT * FROM Levels WHERE idActivity = ? AND name = ?;";
    	List<LevelsDTO> level = db.executeQueryPojo(LevelsDTO.class, sql, activityId, levelName);
    	return level.get(0);
    }

    // INSERTIONS
    
    public void insertNewLevel(String idActivity, String name, String fee) {
		SemanticValidations.validateIdForTable(idActivity, "Activities", "Not valid ID");
		SemanticValidations.validateName(name);
		SemanticValidations.validatePositiveNumberOrZero(fee, "Not valid number");
		
		String sql = "INSERT INTO Levels"
				+ "(idActivity, name, fee) VALUES "
				+ "(?, ?, ?)";
		db.executeUpdate(sql, idActivity, name, fee);
	}
    
    public void removeLevels(String idActivity) {
        String sql = "DELETE FROM Levels WHERE idActivity = ?;";
        db.executeUpdate(sql, idActivity); // Ensure executeUpdate() is used for DELETE queries
    }

}
