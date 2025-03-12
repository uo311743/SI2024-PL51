package model;

import java.util.List;
import DTOs.SponsorshipPaymentsDTO;
import util.Database;

public class SponsorshipPaymentsModel {
    
    private Database db = new Database();
    
    // GETTERS
    
    public SponsorshipPaymentsDTO getSponsorshipPayment(String idInvoice) {
    	String query = "SELECT * FROM SponsorshipPayments sp WHERE sp.idInvoice = ?;";
    	List<SponsorshipPaymentsDTO> results = db.executeQueryPojo(SponsorshipPaymentsDTO.class, query, idInvoice);
    	if (!results.isEmpty()) {
    		return results.get(0);
    	}
    	return null;
    }
    
    // INSERTIONS

    public void registerPayment(Integer idInvoice, String dateSponsorshipPayment, double amountSponsorshipPayments) {
        String query = "INSERT INTO SponsorshipPayments (idInvoice, date, amount) VALUES (?, ?, ?);";
		db.executeUpdate(query, idInvoice, dateSponsorshipPayment);
    }
}
