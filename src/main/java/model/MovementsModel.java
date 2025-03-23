package model;

import java.util.List;
import DTOs.MovementsDTO;
import util.Database;
import util.SemanticValidations;

public class MovementsModel {
	
	private Database db = new Database();

	// GETTERS
	public List<MovementsDTO> getMovementsByActivity(String idActivity)
	{
		SemanticValidations.validateIdForTable(idActivity, "Activities", "ERROR. Provided idActivity for getMovementsByActivity does not exist.");
		String sql = "SELECT * FROM Movements WHERE idActivity = ?;";
	    return db.executeQueryPojo(MovementsDTO.class, sql, idActivity);
	}

    public List<MovementsDTO> getIncomeByActivity(String idActivity) {
		SemanticValidations.validateIdForTable(idActivity, "Activities", "ERROR. Provided idActivity for getIncomeByActivity does not exist.");
		String sql = "SELECT * FROM Movements WHERE type = 'income' AND idActivity = ?;";
	    return db.executeQueryPojo(MovementsDTO.class, sql, idActivity);
	}
	
	public List<MovementsDTO> getExpensesByActivity(String idActivity) {
		SemanticValidations.validateIdForTable(idActivity, "Activities", "ERROR. Provided idActivity for getExpensesByActivity does not exist.");
		String sql = "SELECT * FROM Movements WHERE type = 'expense' AND idActivity = ?;";
	    return db.executeQueryPojo(MovementsDTO.class, sql, idActivity);
	}

    public double getEstimatedIncome(String idActivity) {
		SemanticValidations.validateIdForTable(idActivity, "Movements", "ERROR. Provided idActivity for getEstimatedIncome does not exist.");
		String sql = "SELECT SUM(amount) FROM Movements WHERE type = 'income' AND status IN ('estimated', 'paid') AND idActivity = ?;";
	    Object result = db.executeQueryArray(sql, idActivity).get(0)[0];
		if (result == null) {
			return 0.0;
		}
		return (double) result;
	}
	
	public double getActualIncome(String idActivity) {
		SemanticValidations.validateIdForTable(idActivity, "Movements", "ERROR. Provided idActivity for getEstimatedIncome does not exist.");
		String sql = "SELECT SUM(amount) FROM Movements WHERE type = 'income' AND status = 'paid' AND idActivity = ?;";
	    Object result = db.executeQueryArray(sql, idActivity).get(0)[0];
		if (result == null) {
			return 0.0;
		}
		return (double) result;
	}

    public double getEstimatedExpenses(String idActivity) {
		SemanticValidations.validateIdForTable(idActivity, "Movements", "ERROR. Provided idActivity for getEstimatedIncome does not exist.");
		String sql = "SELECT SUM(amount) FROM Movements WHERE type = 'expense' AND status IN ('estimated', 'paid') AND idActivity = ?;";
	    Object result = db.executeQueryArray(sql, idActivity).get(0)[0];
		if (result == null) {
			return 0.0;
		}
		return (double) result;
	}
	
	public double getActualExpenses(String idActivity) {
		SemanticValidations.validateIdForTable(idActivity, "Movements", "ERROR. Provided idActivity for getEstimatedIncome does not exist.");
		String sql = "SELECT SUM(amount) FROM Movements WHERE type = 'expense' AND status = 'paid' AND idActivity = ?;";
	    Object result = db.executeQueryArray(sql, idActivity).get(0)[0];
		if (result == null) {
			return 0.0;
		}
		return (double) result;
	}
	
	// Function to get unique 'type' values from Movements table
    public List<Object[]> getUniqueTypes() {
        String query = "SELECT DISTINCT type FROM Movements";

        return db.executeQueryArray(query);
    }
    
    // Function to get unique 'type' values from Movements table
    public List<Object[]> getUniqueStatus() {
        String query = "SELECT DISTINCT status FROM Movements WHERE status != 'cancelled'";

        return db.executeQueryArray(query);
    }

	// INSERTIONS

	public void registerMovement(String idActivity, String amount, String date, String type, String concept, String status) {
    	String sql = "INSERT INTO Movements (idActivity, type, concept, amount, date, status) VALUES (?, ?, ?, ?, ?, ?)";
    	db.executeUpdate(sql, idActivity, amount, date, type, concept, status);
    }
}
