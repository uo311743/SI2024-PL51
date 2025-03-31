package model;

import java.util.List;

import DTOs.IncomesExpensesDTO;
import DTOs.MovementsDTO;
import util.Database;
import util.SemanticValidations;

public class MovementsModel {
	
	private Database db = new Database();

	// GETTERS
	public List<MovementsDTO> getMovementsByActivity(String idActivity) {
		SemanticValidations.validateIdForTable(idActivity, "Activities", "ERROR. Provided idActivity for getMovementsByActivity does not exist.");
		String sql = " SELECT m.* FROM Movements m " +
			"JOIN IncomesExpenses ie ON m.idType = ie.id " +
			"WHERE ie.idActivity = ?;";
	    return db.executeQueryPojo(MovementsDTO.class, sql, idActivity);
	}
	
	public List<MovementsDTO> getMovementsByIncomeExpense(String idType) {
		SemanticValidations.validateIdForTable(idType, "IncomesExpenses", "ERROR. Provided idType for getMovementsByIncomeExpense does not exist.");
		String sql = " SELECT * FROM Movements WHERE idType = ?";
	    return db.executeQueryPojo(MovementsDTO.class, sql, idType);
	}
	
	public List<MovementsDTO> getMovementsByTypeAndActivity(String type, String idActivity) {
		String sql = " SELECT mo.* FROM Movements mo "
				+ "JOIN IncomesExpenses ie ON ie.id = mo.idType AND ie.type = ? AND ie.idActivity = ?;";
	    return db.executeQueryPojo(MovementsDTO.class, sql, type, idActivity);
	}

	public List<IncomesExpensesDTO> getIncomeByActivity(String idActivity) {
		SemanticValidations.validateIdForTable(idActivity, "Activities", "ERROR. Provided idActivity for getIncomeByActivity does not exist.");
		String sql = "SELECT ie.* FROM IncomesExpenses ie " +
			"WHERE ie.type = 'income' AND ie.idActivity = ?;";
	    return db.executeQueryPojo(IncomesExpensesDTO.class, sql, idActivity);
	}
	
	public IncomesExpensesDTO getIncomeExpenseById(String id) {
		String sql = "SELECT * FROM IncomesExpenses WHERE id = ?;";
        List<IncomesExpensesDTO> result = db.executeQueryPojo(IncomesExpensesDTO.class, sql, id);
        
        return result.get(0);
	}
	
	public List<IncomesExpensesDTO> getExpensesByActivity(String idActivity) {
		SemanticValidations.validateIdForTable(idActivity, "Activities", "ERROR. Provided idActivity for getExpensesByActivity does not exist.");
		String sql = "SELECT ie.* FROM IncomesExpenses ie " +
			"WHERE ie.type = 'expense' AND ie.idActivity = ?;";
	    return db.executeQueryPojo(IncomesExpensesDTO.class, sql, idActivity);
	}

	public double getEstimatedIncome(String idActivity) {
		SemanticValidations.validateIdForTable(idActivity, "IncomesExpenses", "ERROR. Provided idActivity for getEstimatedIncome does not exist.");
		String sql = "SELECT SUM(amountEstimated) FROM IncomesExpenses " +
			"WHERE type = 'income' AND idActivity = ?;";
	    Object result = db.executeQueryArray(sql, idActivity).get(0)[0];
		return (result == null) ? 0.0 : (double) result;
	}
	
	public double getActualIncome(String idActivity) {
		SemanticValidations.validateIdForTable(idActivity, "IncomesExpenses", "ERROR. Provided idActivity for getActualIncome does not exist.");
		String sql = "SELECT SUM(m.amount) FROM Movements m " +
			"JOIN IncomesExpenses ie ON m.idType = ie.id " +
			"WHERE ie.type = 'income' AND ie.idActivity = ?;";
	    Object result = db.executeQueryArray(sql, idActivity).get(0)[0];
		return (result == null) ? 0.0 : (double) result;
	}
	
	public double getTotalAmountPaid(String idType) {
		SemanticValidations.validateIdForTable(idType, "IncomesExpenses", "ERROR. Provided idType for getTotalAmountPaid does not exist.");
		String sql = "SELECT SUM(m.amount) FROM Movements m WHERE m.idType = ?;";
	    Object result = db.executeQueryArray(sql, idType).get(0)[0];
		return (result == null) ? 0.0 : (double) result;
	}

	public double getEstimatedExpenses(String idActivity) {
		SemanticValidations.validateIdForTable(idActivity, "IncomesExpenses", "ERROR. Provided idActivity for getEstimatedExpenses does not exist.");
		String sql = "SELECT SUM(amountEstimated) FROM IncomesExpenses " +
			"WHERE type = 'expense' AND idActivity = ?;";
	    Object result = db.executeQueryArray(sql, idActivity).get(0)[0];
		return (result == null) ? 0.0 : (double) result;
	}
	
	public double getActualExpenses(String idActivity) {
		SemanticValidations.validateIdForTable(idActivity, "IncomesExpenses", "ERROR. Provided idActivity for getActualExpenses does not exist.");
		String sql = "SELECT SUM(m.amount) FROM Movements m " +
			"JOIN IncomesExpenses ie ON m.idType = ie.id " +
			"WHERE ie.type = 'expense' AND ie.idActivity = ?;";
	    Object result = db.executeQueryArray(sql, idActivity).get(0)[0];
		return (result == null) ? 0.0 : (double) result;
	}
	
	// Function to get unique 'type' values from IncomesExpenses table
    public List<Object[]> getUniqueTypes() {
        String query = "SELECT DISTINCT type FROM IncomesExpenses;";
        return db.executeQueryArray(query);
    }
    
    // SETTERS
    public void setIncomeExpenseStatus(String id, String newStatus) {
        // Validate that the ID exists in the table
        SemanticValidations.validateIdForTable(id, "IncomesExpenses", 
            "ERROR: Provided id for updateStatus does not exist.");

        // Validate that the new status is one of the allowed values
        if (!newStatus.equalsIgnoreCase("estimated") && !newStatus.equalsIgnoreCase("paid")) {
            throw new IllegalArgumentException("ERROR: Invalid status. Allowed values: 'estimated', 'paid'.");
        }

        // Update the status in the database
        String sql = "UPDATE IncomesExpenses SET status = ? WHERE id = ?";
        db.executeUpdate(sql, newStatus.toLowerCase(), id);
    }

	// INSERTIONS
    public String registerIncomeExpense(String idActivity, String type, String status, String amountEstimated, String dateEstimated, String concept) {
    	String sql = "INSERT INTO IncomesExpenses (idActivity, type, status, amountEstimated, dateEstimated, concept) " +
			"VALUES (?, ?, ?, ?, ?, ?);";
    	return db.executeInsertion(sql, idActivity, type, status, amountEstimated, dateEstimated, concept);
    }

	public void registerMovement(String idType, String amount, String date, String concept) {
    	String sql = "INSERT INTO Movements (idType, concept, amount, date) " +
			"VALUES (?, ?, ?, ?);";
    	db.executeUpdate(sql, idType, concept, amount, date);
    }
}
