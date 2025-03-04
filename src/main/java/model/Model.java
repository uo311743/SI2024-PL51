package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import DTOs.*;

import java.sql.*;

import util.ApplicationException;
import util.Database;
import util.SemanticValidations;
import util.SwingMain;

public class Model {
	
	// Instance that allows the connection to the DB and execution of queries
	private Database db = new Database();
	private Connection connection; 
    
	/* ================================================================================
     * 
     *     METHODS
     * 
     */
	
	public Model() {
        try {
            connection = DriverManager.getConnection(db.getUrl());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
	// Registration of Payments
	public Integer getSponsorshipAgreementId(String nifOrVat, String activity) {
	    String sql = "SELECT sa.id " +
	                 "FROM SponsorshipAgreements sa " +
	                 "JOIN SponsorContacts sc ON sa.idSponsorContact = sc.id " +
	                 "JOIN SponsorOrganizations so ON sc.idSponsorOrganization = so.id " +
	                 "JOIN Activities a ON a.id = sa.idActivity " +
	                 "WHERE (so.nif = ? OR so.vat = ?) AND (a.name = ?);";

	    try {
	    	List<Object[]> result = db.executeQueryArray(sql, nifOrVat, nifOrVat, activity);
		    
		    if (result.isEmpty()) {
		    	throw new ApplicationException("No SponsorshipAgreement found");
		    } else {
		    	return Integer.parseInt(result.get(0)[0].toString());
		    }
	    } catch (Exception e) {
	    	throw new ApplicationException("Unexpected error while retrieving SponsorshipAgreement ID: " + e.getMessage());
	    }
	}
	
	public Integer getActivityId(String name) {
		String sql = "SELECT a.id FROM Activities a WHERE a.name = ?;";
		
		try {
	    	List<Object[]> result = db.executeQueryArray(sql, name);
		    
		    if (result.isEmpty()) {
		    	throw new ApplicationException("No Activity found");
		    } else {
		    	return Integer.parseInt(result.get(0)[0].toString());
		    }
	    } catch (Exception e) {
	    	throw new ApplicationException("Unexpected error while retrieving Activity ID: " + e.getMessage());
	    }
	}
	
	public void validateActivity(Integer idSponsorshipAgreement, String activity) {
		String sql = "SELECT a.name FROM Activity a JOIN SponsorshipAgreement sa ON a.id = sa.isActivity;";
		
		try {
	    	List<Object[]> result = db.executeQueryArray(sql);
		    
		    if (result.isEmpty()) {
		    	throw new ApplicationException("No Agreement found for that Activity");
		    } else {
		    	validateCondition(result.get(0)[0].toString() == activity, "There is no agreement for that activity");
		    }
	    } catch (Exception e) {
	    	throw new ApplicationException("Unexpected error while retrieving Activity: " + e.getMessage());
	    }
	}
	
	public void getInvoiceId(Integer invoiceId, Integer idSponsorshipAgreement) {
		String sql = "SELECT i.id FROM Invoices i WHERE i.idSponsorshipAgreement = ?;";
		
		try {
			List<Object[]> result = db.executeQueryArray(sql, idSponsorshipAgreement);
			
			if (result.isEmpty()) {
				throw new ApplicationException("No Invoice Found");
			} else {
				try {
	    			validateNumber(invoiceId, "Invalid Invoice ID provided");
	    			validateCondition((Integer.parseInt(result.get(0)[0].toString()) == invoiceId), "Incorrect Invoice ID provided");
	    		} catch (Exception e) {
	    			throw new ApplicationException(e.getMessage());
	    		}
			}
		} catch (Exception e) {
			throw new ApplicationException("Unexpected error while retrieving Invoice ID: " + e.getMessage());
		}
	}
	
	/**
	 * Semantic Validation of Date a Payment was received according to business logic
	 * RULE #1: Date cannot be in the future
	 * RULE #2: Never before Invoice generation
	 * @param date the payment was made
	 * @param invoiceId invoice the payment belongs to
	 */
	public void validateDate(String date, Integer invoiceId) {
        String query = "SELECT i.dateIssued FROM Invoices i WHERE i.id = ?;";

        try {
        	List<Object[]> result = db.executeQueryArray(query, invoiceId);
            
            if (result.isEmpty()) {
            	throw new ApplicationException("No Invoice Found");
            } else {
            	Date invoiceIssuedDate = Date.valueOf(result.get(0)[0].toString());
            	Date paymentDate = Date.valueOf(date);
            	SemanticValidations.validateDateInPast(paymentDate, SwingMain.getTodayDate(), true, "Invalid date of payment: cannot be after today's date");
            	validateCondition(invoiceIssuedDate.before(paymentDate), "Payment cannot be made before Invoice generation");
            }
        } catch (Exception e) {
        	e.printStackTrace();
        	throw new ApplicationException(e.getMessage());
        }
    }

    public void registerPayment(Integer idInvoice, String dateSponsorshipPayment, double amountSponsorshipPayments) {
        String query = "INSERT INTO SponsorshipPayments (idInvoice, date, amount) VALUES (?, ?, ?);";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, idInvoice);
            pstmt.setString(2, dateSponsorshipPayment);
            pstmt.setDouble(3, amountSponsorshipPayments);
            pstmt.executeUpdate();
        } catch (SQLException e) {
        	e.printStackTrace();
        	throw new ApplicationException(e.getMessage());
        }
    }
    
    public List<Object[]> getListActivities() {
    	String query = "SELECT name FROM Activities;";
    	
    	return db.executeQueryArray(query);
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
