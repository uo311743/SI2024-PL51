package model;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import DTOs.ActivitiesDTO;
import DTOs.InvoicesDTO;
import util.ApplicationException;
import util.Database;

public class Model {
	
	// Instance that allows the connection to the DB and execution of queries
	private Database db = new Database();
	
	public static final String SQL_LIST_SPONSORS=
			"SELECT id,descr,"
			+" case when ?<inicio then ''"
			+"   when ?<=fin then '(Abierta)'"
			+"   when ?<fecha then '(Abierta)'"
			+"   when ?=fecha then '(Abierta)'"
			+"   else '' "
			+" end as abierta"
			+" from carreras  where fecha>=? order by id";
	
	public static final String SQL_LIST_ACTIVITIES=
			"SELECT id,descr,"
			+" case when ?<inicio then ''"
			+"   when ?<=fin then '(Abierta)'"
			+"   when ?<fecha then '(Abierta)'"
			+"   when ?=fecha then '(Abierta)'"
			+"   else '' "
			+" end as abierta"
			+" from carreras  where fecha>=? order by id";
    
	/* ================================================================================
     * 
     *     METHODS
     * 
     */
	

	
    /*
	public String getInvoiceRecipientCountry() {
        String sql = "SELECT country FROM Recipients WHERE id = (SELECT recipient_id FROM Invoices ORDER BY date_issue DESC LIMIT 1);";
        return db.executeQuerySingle(String.class, sql);
    }
    */
	
	public List<Object[]> getSponsorListArray() {
		String sql = "SELECT id || '-' || descr || ' ' || abierta as item from (" + SQL_LIST_SPONSORS + ")";
		return db.executeQueryArray(sql);
	}
    
	public List<Object[]> getActivityListArray() {
		String sql = "SELECT id || '-' || descr || ' ' || abierta as item from (" + SQL_LIST_ACTIVITIES + ")";
		return db.executeQueryArray(sql);
	}
	
    public List<InvoicesDTO> getDateIssueInvoices() {
        String sql = "SELECT dateIssueInvoices FROM Invoices;";
        return db.executeQueryPojo(InvoicesDTO.class, sql);
    }
    
    public List<InvoicesDTO> getIdInvoices() {
        String sql = "SELECT idInvoices FROM Invoices;";
        return db.executeQueryPojo(InvoicesDTO.class, sql);
    }
    
    public List<ActivitiesDTO> getFilteredActivities(Date startDate, Date endDate, String status) {
        String sql = "SELECT date, name, status, estimatedIncome, paidIncome, estimatedExpenses, paidExpenses " +
                     "FROM Activities " +
                     "WHERE date BETWEEN ? AND ? " +
                     (status.equals("All") ? "" : "AND status = ?");

        if (status.equals("All")) {
            return db.executeQueryPojo(ActivitiesDTO.class, sql, startDate, endDate);
        } else {
            return db.executeQueryPojo(ActivitiesDTO.class, sql, startDate, endDate, status);
        }
    }


    
    /* ================================================================================
     * 
     *     SPECIFIC VALIDATIONS
     * 
     */
    
    
    
    // TODO
    
    
    
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
    
    private void validateNumber(Object number, String message)
    {
    	try { Integer.parseInt(number.toString()); }
    	catch (NumberFormatException e)
    	{ throw new ApplicationException(message); }
    }
    
    private void validateNotNull(Object obj, String message)
    {
		if (obj==null) throw new ApplicationException(message);
	}
    
	private void validateCondition(boolean condition, String message)
	{
		if (!condition) throw new ApplicationException(message);
	}
}
