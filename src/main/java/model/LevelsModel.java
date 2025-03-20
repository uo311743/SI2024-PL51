package model;

import java.util.List;
import DTOs.LevelsDTO;
import util.ApplicationException;
import util.Database;
import util.SemanticValidations;

public class LevelsModel {
	
	public static final String SQL_LEVELS = "SELECT * FROM Levels L"
			+ "JOIN Activities A "
			+ "ON L.idActivity == A.id "
			+ "WHERE A.id == ?;";
	
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

    // INSERTIONS
    
    public void insertNewLevel(String idActivity, String name, String amount) {
		SemanticValidations.validateIdForTable(idActivity, "Activities", "Not valid ID");
		SemanticValidations.validateName(name);
		SemanticValidations.validatePositiveNumberOrZero(amount, "Not valid number");
		
		if(getNumberLevelsByActivityId(idActivity) != 0)
			throw new ApplicationException("ERROR");
		
		String sql = "INSERT INTO Levels"
				+ "(idActivity, name, fee) VALUES "
				+ "(?, ?, ?)";
		db.executeUpdate(sql, idActivity, name, amount);
	}
}
