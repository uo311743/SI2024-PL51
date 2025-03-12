package model;

import java.util.List;

import DTOs.*;

import java.sql.*;

import util.ApplicationException;
import util.Database;

public class RegisterPaymentModel {
	
	// Instance that allows the connection to the DB and execution of queries
	private Database db = new Database();
	private Connection connection; 
    
	/* ================================================================================
     * 
     *     METHODS
     * 
     */
	
	public RegisterPaymentModel() {
        try {
            connection = DriverManager.getConnection(db.getUrl());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
	// Registration of Payments
	/**
	 * Fetch SponsorshipAgreement in DB corresponding to Company identified by it NIF/VAT and name of activity selected with a valid status
	 * @param nifOrVat
	 * @param activity
	 * @return id of the SponsorshipAgreement
	 */
	public Integer getSponsorshipAgreementId(String nifOrVat, String activity) {
	    String sql = "SELECT sa.id " +
	                 "FROM SponsorshipAgreements sa " +
	                 "JOIN SponsorContacts sc ON sa.idSponsorContact = sc.id " +
	                 "JOIN SponsorOrganizations so ON sc.idSponsorOrganization = so.id " +
	                 "JOIN Activities a ON a.id = sa.idActivity " +
	                 "WHERE (so.nif = ? OR so.vat = ?) AND (a.name = ?) AND (sa.status <> 'cancelled');";

	    try {
	    	List<Object[]> result = db.executeQueryArray(sql, nifOrVat, nifOrVat, activity);
		    
		    if (result.isEmpty()) {
		    	throw new ApplicationException("No Prevailing SponsorshipAgreement found");
		    } else {
		    	return Integer.parseInt(result.get(0)[0].toString());
		    }
	    } catch (Exception e) {
	    	throw new ApplicationException("Unexpected error while retrieving SponsorshipAgreement ID: " + e.getMessage());
	    }
	}
	
	/**
	 * Fetch Invoice in DB corresponding to SponsorshipAgreement identified by its ID with a valid status
	 * @param idSponsorshipAgreement
	 * @return id of Invoice found
	 */
	public String getInvoiceId(Integer idSponsorshipAgreement) {
		String sql = "SELECT i.id FROM Invoices i WHERE i.idSponsorshipAgreement = ? AND i.status <> 'cancelled';";
		
		try {
			List<Object[]> result = db.executeQueryArray(sql, idSponsorshipAgreement);
			
			if (result.isEmpty()) {
				throw new ApplicationException("No Prevailing Invoice Found");
			} else {
				return result.get(0)[0].toString();
			}
		} catch (Exception e) {
			throw new ApplicationException("Unexpected error while retrieving Invoice ID: " + e.getMessage());
		}
	}

	/**
	 * Register Payment in DB
	 * @param idInvoice
	 * @param dateSponsorshipPayment
	 * @param amountSponsorshipPayments
	 */
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
    
    /**
     * Fetch in DB a Payment already made for the invoice identified by its ID
     * @param idInvoice ID of the Invoice that corresponds to the payment
     * @return SponsorhipPaymentDTO Object if there is a Payment registered; null otherwise
     */
    public SponsorshipPaymentsDTO getSponsorshipPayment(String idInvoice) {
    	String query = "SELECT * FROM SponsorshipPayments sp WHERE sp.idInvoice = ?;";
    	
    	List<SponsorshipPaymentsDTO> results = db.executeQueryPojo(SponsorshipPaymentsDTO.class, query, idInvoice);
    	
    	if (!results.isEmpty()) {
    		return results.get(0);
    	}
    	
    	return null;
    }
    
    /**
     * Fetch name of all current Activities in DB available for sponsorship
     * @return list of arrays of Objects with the name of the activities in first position of each array
     */
    public List<Object[]> getListActivities() {
    	String query = "SELECT name FROM Activities WHERE status <> 'cancelled';";
    	
    	return db.executeQueryArray(query);
    }

    
    /* ================================================================================
     * 
     *     SPECIFIC VALIDATIONS
     * 
     */
    
    /**
	 * Semantic Validation of Date a Payment was received according to business logic
	 * RULE: Never before Invoice generation
	 * @param date the payment was made
	 * @param invoiceId invoice the payment belongs to
	 */
	public void validatePaymentDate(String date, Integer invoiceId) {
        String query = "SELECT i.dateIssued FROM Invoices i WHERE i.id = ?;";

        try {
        	List<Object[]> result = db.executeQueryArray(query, invoiceId);
            
            if (result.isEmpty()) {
            	throw new ApplicationException("No Invoice Found");
            } else {
            	Date invoiceIssuedDate = Date.valueOf(result.get(0)[0].toString());
            	Date paymentDate = Date.valueOf(date);
            	validateCondition(invoiceIssuedDate.before(paymentDate), "Payment cannot be made before Invoice generation");
            }
        } catch (Exception e) {
        	e.printStackTrace();
        	throw new ApplicationException(e.getMessage());
        }
    }
    
    
	private void validateCondition(boolean condition, String message)
	{
		if (!condition) throw new ApplicationException(message);
	}
}