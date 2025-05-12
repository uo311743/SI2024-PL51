package model;

import java.util.List;
import DTOs.SponsorshipPaymentsDTO;
import util.Database;
import util.SemanticValidations;

public class SponsorshipPaymentsModel {
    
    private static Database db = new Database();
    
    // GETTERS
    
    public static List<SponsorshipPaymentsDTO> getSponsorshipPayment(String idInvoice) {
    	String query = "SELECT * FROM SponsorshipPayments sp WHERE sp.idInvoice = ?;";
    	
    	return db.executeQueryPojo(SponsorshipPaymentsDTO.class, query, idInvoice);
    }
    
    public double getTotalAmountPaid(String idInvoice) {
		SemanticValidations.validateIdForTable(idInvoice, "SponsorshipPayments", "ERROR. Provided idInvoice for getTotalAmountPaid does not exist.");
		String sql = "SELECT SUM(p.amount) FROM SponsorshipPayments p WHERE p.idInvoice = ?;";
	    Object result = db.executeQueryArray(sql, idInvoice).get(0)[0];
		return (result == null) ? 0.0 : (double) result;
	}
    
    // INSERTIONS

    public void registerPayment(String idInvoice, String dateSponsorshipPayment, double amountSponsorshipPayments) {
    	SemanticValidations.validateIdForTable(idInvoice, "SponsorshipPayments", "ERROR. Provided idInvoice for registerPayment does not exist.");
        String query = "INSERT INTO SponsorshipPayments (idInvoice, date, amount) VALUES (?, ?, ?);";
		db.executeInsertion(query, idInvoice, dateSponsorshipPayment, amountSponsorshipPayments);
    }
    
    public void updatePaymentsInvoiceId(String id, String idAgreement) {
    	SemanticValidations.validateIdForTable(id, "Invoices", "Not valid ID");
    	String sql = "UPDATE SponsorshipPayments SET idInvoice = ? WHERE idInvoice IN (SELECT id FROM Invoices WHERE status = 'rectified' AND idSponsorshipAgreement = ?);";
		db.executeUpdate(sql, id, idAgreement);
    }
}
