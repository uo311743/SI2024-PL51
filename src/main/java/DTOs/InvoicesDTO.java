package DTOs;

public class InvoicesDTO {
    private int id;
    private int idSponsorshipAgreement;
    private String dateIssued;
    private String dateSent;
    private String dateExpiration;
    private double totalAmount;
    private double taxRate;
    private String status;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdSponsorshipAgreement() {
		return idSponsorshipAgreement;
	}
	public void setIdSponsorshipAgreement(int idSponsorshipAgreement) {
		this.idSponsorshipAgreement = idSponsorshipAgreement;
	}
	public String getDateIssued() {
		return dateIssued;
	}
	public void setDateIssued(String dateIssued) {
		this.dateIssued = dateIssued;
	}
	public String getDateSent() {
		return dateSent;
	}
	public void setDateSent(String dateSent) {
		this.dateSent = dateSent;
	}
	public String getDateExpiration() {
		return dateExpiration;
	}
	public void setDateExpiration(String dateExpiration) {
		this.dateExpiration = dateExpiration;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public double getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	} 
}