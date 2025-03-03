package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import util.ApplicationException;
import util.Database;

import DTOs.*;

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
	public Integer getSponsorshipAgreementId(String nifOrVat, String activityName) throws SQLException {
	    String sql = "SELECT sa.id " +
	                 "FROM SponsorshipAgreements sa " +
	                 "JOIN SponsorContacts sc ON sa.idSponsorContact = sc.id " +
	                 "JOIN SponsorOrganizations so ON sc.idSponsorOrganization = so.id " +
	                 "JOIN Activities a ON a.name = ? " +
	                 "WHERE (so.nif = ? OR so.vat = ?)";

	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        // Execute query with parameters: activityName, nifOrVat, nifOrVat (for both NIF and VAT check)
	    	pstmt.setString(1, activityName);
	    	pstmt.setString(2, nifOrVat);
	    	pstmt.setString(3, nifOrVat);
	        ResultSet results = pstmt.executeQuery();

	        if (results.next()) {
	            Integer id = results.getInt("id");
	            validateNotNull(id, "No SponsorshipAgreement found");
	        }
	    } catch (Exception e) {
	    	throw new ApplicationException("Unexpected error while retrieving SponsorshipAgreement ID: " + e.getMessage());
	    }

	    return null;
	}
	
	public Integer getInvoiceId(Integer idSponsorshipAgreement) {
		String sql = "SELECT i.id FROM Invoices WHERE i.idSponsorshipAgreement = ?";
		
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        // Execute query with parameters: activityName, nifOrVat, nifOrVat (for both NIF and VAT check)
	        pstmt.setInt(1, idSponsorshipAgreement);
	        ResultSet results = pstmt.executeQuery();

	        if (results.next()) {
	        	Integer id = results.getInt("id");
	            validateNotNull(id, "No Invoice found");
	        }
	    } catch (Exception e) {
	    	throw new ApplicationException("Unexpected error while retrieving Invoice ID: " + e.getMessage());
	    }

	    return null;
	}
	
	/**
	 * Semantic Validation of Date a Payment was received according to business logic
	 * RULE: Never before Invoice generation
	 * @param date the payment was made
	 * @param invoiceId invoice the payment belongs to
	 * @return whether the payment was previous to invoice
	 */
	public boolean validateDate(String date, Integer invoiceId) {
        String query = "SELECT sa.date FROM SponsorshipAgreements sa JOIN Invoices i ON i.idSponsorshipAgreement == sa.id WHERE i.id = ?";;
        
        this.validateDate(date, "Payment Date not valid");
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, invoiceId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Date agreementDate = Date.valueOf(rs.getString("date"));
                Date paymentDate = Date.valueOf(date);
                return !paymentDate.before(agreementDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // Register Payment
    public void registerPayment(Integer idInvoice, String dateSponsorshipPayment, double amountSponsorshipPayments) {
        String query = "INSERT INTO SponsorshipPayments (idInvoice, date, amount) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, idInvoice);
            pstmt.setString(2, dateSponsorshipPayment);
            pstmt.setDouble(3, amountSponsorshipPayments);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public String[] getListActivities() {
    	String query = "SELECT * FROM Activities";
    	
    	List<ActivitiesDTO> activities = db.executeQueryPojo(ActivitiesDTO.class, query);
    	List<String> names = new ArrayList<>();
    	
    	for (ActivitiesDTO a : activities) {
    		if (a.getStatus() != "cancelled" || a.getStatus() != "closed")
    		names.add(a.getName());
    	}
    	
    	return names.toArray(new String[0]);
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
