package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.sql.*;

import util.ApplicationException;
import util.Database;

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
	public boolean validateNIF(String nif, String activity) {
        String query = "SELECT * FROM Companies WHERE NIF = ? AND Activity = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, nif);
            pstmt.setString(2, activity);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
	public boolean validateDate(String date, String nif, String activity) {
        String query = "SELECT AgreementDate FROM Agreements WHERE NIF = ? AND Activity = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, nif);
            pstmt.setString(2, activity);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Date agreementDate = rs.getDate("AgreementDate");
                Date paymentDate = Date.valueOf(date); // Assuming date is in YYYY-MM-DD format
                return !paymentDate.before(agreementDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
	public String getInvoiceId(String nif, String activity) {
        String query = "SELECT InvoiceID FROM Invoices WHERE NIF = ? AND Activity = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, nif);
            pstmt.setString(2, activity);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("InvoiceID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Register Payment
    public void registerPayment(String activity, double amount, String nif, String isbn, String date, String invoiceId) {
        String query = "INSERT INTO Payments (Activity, Amount, NIF, ISBN, Date, InvoiceID) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, activity);
            pstmt.setDouble(2, amount);
            pstmt.setString(3, nif);
            pstmt.setString(4, isbn);
            pstmt.setString(5, date);
            pstmt.setString(6, invoiceId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
