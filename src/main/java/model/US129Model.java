package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import DTOs.ActivitiesDTO;
import util.ApplicationException;
import util.Database;

public class US129Model {
	
	public static final String SQL_SUM_INCOMES = "SELECT COALESCE(SUM(amount), 0) FROM Movements WHERE amount >= 0;";
	public static final String SQL_SUM_EXPENSES = "SELECT COALESCE(SUM(amount), 0) FROM Movements WHERE amount < 0;";
	
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
    
    public List<ActivitiesDTO> getActivitiesFromCurrentYear(String startDate, String endDate) {
		String sql ="SELECT name, status, dateStart, dateEnd FROM Activities WHERE dateStart >= ? AND dateEnd <= ?;";
		return db.executeQueryPojo(ActivitiesDTO.class, sql, startDate, endDate);
	}
    
    /* ================================================================================
     * 
     *     GENERIC VALIDATIONS
     * 
     */
    
    private void validateDate(String date, String message) {
    	try
    	{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate.parse(date, formatter);
        }
    	catch (DateTimeParseException e) { throw new ApplicationException(message); }
    }
    
    private void validateNumber(Object number, String message) {
    	try { Integer.parseInt(number.toString()); }
    	catch (NumberFormatException e)
    	{ throw new ApplicationException(message); }
    }
    
    private void validateNotNull(Object obj, String message) {
		if (obj==null) throw new ApplicationException(message);
	}
    
	private void validateCondition(boolean condition, String message) {
		if (!condition) throw new ApplicationException(message);
	}
}
