package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/yourdatabase");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
	// Registration of Payments
	public Integer getSponsorshipAgreementId(String nifOrVat, String activityName) throws SQLException {
	    String sql = "SELECT sa.idSponsorshipAgreement " +
	                 "FROM SponsorshipAgreements sa " +
	                 "JOIN SponsorContacts sc ON sa.idSponsorContact = sc.idSponsorContact " +
	                 "JOIN SponsorOrganizations so ON sc.idSponsorOrganization = so.idSponsorOrganization " +
	                 "JOIN Activities a ON a.nameActivity = ? " +
	                 "WHERE (so.nifSponsorOrganization = ? OR so.vatSponsorOrganization = ?)";

	    try {
	        // Execute query with parameters: activityName, nifOrVat, nifOrVat (for both NIF and VAT check)
	        List<SponsorshipAgreementDTO> results = db.executeQueryPojo(SponsorshipAgreementDTO.class, sql, activityName, nifOrVat, nifOrVat);

	        if (results != null && !results.isEmpty()) {
	            return results.get(0).getIdSponsorshipAgreement();
	        } else {
	            System.err.println("No SponsorshipAgreement found for sponsor with NIF/VAT: " + nifOrVat + " and activity: " + activityName);
	        }
	    } catch (Exception e) {
	        System.err.println("Unexpected error while retrieving SponsorshipAgreement ID: " + e.getMessage());
	    }

	    return null;
	}
	
	public Integer getInvoiceId(Integer idSponsorshipAgreement) {
		String sql = "SELECT i.idInvoice FROM Invoices WHERE i.idSponsorshipAgreement = ?";
		
		try {
	        // Execute query with parameters: activityName, nifOrVat, nifOrVat (for both NIF and VAT check)
	        List<InvoicesDTO> results = db.executeQueryPojo(InvoicesDTO.class, sql, idSponsorshipAgreement);

	        if (results != null && !results.isEmpty()) {
	            return results.get(0).getIdInvoice();
	        } else {
	            System.err.println("No Invoice found for Agreement: " + idSponsorshipAgreement);
	        }
	    } catch (Exception e) {
	        System.err.println("Unexpected error while retrieving Invoice ID: " + e.getMessage());
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
        String query = "SELECT sa.dateSponsorshipAgreement FROM SponsorshipAgreements sa JOIN Invoices i ON i.idSponsorshipAgreement == sa.idSponsorshipAgreement WHERE i.idInvoice = ?";;
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, invoiceId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Date agreementDate = rs.getDate("dateSponsorshipAgreement");
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
        String query = "INSERT INTO dateSponsorshipAgreement (idInvoice, dateSponsorshipPayment, amountSponsorshipPayments) VALUES (?, ?, ?)";
        
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
    	String query = "SELECT a.name FROM Activities";
    	
    	List<String> activities = db.executeQueryPojo(String.class, query);
    	
    	return activities.toArray(new String[0]);
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
