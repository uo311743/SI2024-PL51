package DTOs;

public class InvoicesDTO {
    private String id;
    private String idSponsorshipAgreement;
    private String dateIssued;
    private String dateSent;
    private String dateExpiration;
    private String totalAmount;
    private String taxRate;
    private String status;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdSponsorshipAgreement() {
		return idSponsorshipAgreement;
	}
	public void setIdSponsorshipAgreement(String idSponsorshipAgreement) {
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
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	} 
}