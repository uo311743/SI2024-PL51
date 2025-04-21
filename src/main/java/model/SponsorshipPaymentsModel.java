package model;

import java.util.List;
import DTOs.SponsorshipPaymentsDTO;
import util.Database;
import util.SemanticValidations;

public class SponsorshipPaymentsModel {
    
    private Database db = new Database();
    
    // GETTERS
    
    public List<SponsorshipPaymentsDTO> getSponsorshipPayment(String idInvoice) {
    	String query = "SELECT * FROM SponsorshipPayments sp WHERE sp.idInvoice = ?;";
    	
    	return db.executeQueryPojo(SponsorshipPaymentsDTO.class, query, idInvoice);
    }
    
    // INSERTIONS

    public void registerPayment(String idInvoice, String dateSponsorshipPayment, double amountSponsorshipPayments) {
        String query = "INSERT INTO SponsorshipPayments (idInvoice, date, amount) VALUES (?, ?, ?);";
		db.executeUpdate(query, idInvoice, dateSponsorshipPayment, amountSponsorshipPayments);
    }
    
    public void updatePaymentsInvoiceId(String id, String idAgreement) {
    	SemanticValidations.validateIdForTable(id, "Invoices", "Not valid ID");
    	String sql = "UPDATE SponsorshipPayments SET idInvoice = ? WHERE idInvoice IN (SELECT id FROM Invoices WHERE status = 'rectified' AND idSponsorshipAgreement = ?);";
		db.executeUpdate(sql, id, idAgreement);
    }
}
