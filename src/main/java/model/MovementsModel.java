package model;

import java.util.List;
import DTOs.MovementsDTO;
import util.Database;
import util.SemanticValidations;

public class MovementsModel {
	
	private Database db = new Database();

	// GETTERS

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
	
	public List<MovementsDTO> getEstimatedMovementsbyTypeAndActivity(String type, String idActivity)
	{
		String sql = "SELECT mo.* FROM Movements mo WHERE mo.status = 'estimated' AND mo.type = ? AND mo.idActivity = ?";

	    return db.executeQueryPojo(MovementsDTO.class, sql, type, idActivity);
	}

	// INSERTIONS

	public void registerMovement(String idActivity, String amount, String date, String type, String concept, String status) {
    	String sql = "INSERT INTO Movements (idActivity, type, concept, amount, date, status) VALUES (?, ?, ?, ?, ?, ?)";
    	db.executeUpdate(sql, idActivity, amount, date, type, concept, status);
    }
}
