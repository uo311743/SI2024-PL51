package model;

import java.util.Date;
import java.util.List;
import DTOs.ActivitiesDTO;
import util.Database;

public class US129Model {
	
	// Instance that allows the connection to the DB and execution of queries
	private Database db = new Database();
	
	public static final String SQL_SUM_INCOMES = "SELECT COALESCE(SUM(amount), 0) FROM IncomesExpenses WHERE amount >= 0;";
	public static final String SQL_SUM_EXPENSES = "SELECT COALESCE(SUM(amount), 0) FROM IncomesExpenses WHERE amount < 0;";
    
	/* ================================================================================
     * 
     *     METHODS
     * 
     */
	
	public List<Object[]> getStatusListArray() {
		String sql = "SELECT Name FROM SponsorOrganizations;";
		return db.executeQueryArray(sql);
	}
	
	public List<ActivitiesDTO> getFilteredActivities(Date startDate, Date endDate, String status) {
        String sql = "SELECT date, name, status, estimatedIncome, paidIncome, estimatedExpenses, paidExpenses FROM Activities WHERE date BETWEEN ? AND ? ";
        
        if (status == null) {
        	sql += ";";
        	return db.executeQueryPojo(ActivitiesDTO.class, sql, startDate, endDate);
        }
        else {
        	sql += " AND status == ?;";
        	return db.executeQueryPojo(ActivitiesDTO.class, sql, startDate, endDate, status);
        }
    }
    
    public int getAmountIncome() {
        List<Integer> income = db.executeQueryPojo(Integer.class, SQL_SUM_INCOMES);
        return income.get(0);
    }
    
    public int getAmountExpense() {
    	List<Integer> expense = db.executeQueryPojo(Integer.class, SQL_SUM_EXPENSES);
		return expense.get(0);
	}
}
