package DTOs;

public class SponsorshipPaymentsDTO {
	private String id;
	private String idInvoice;
	private String date;
	private String amount;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInvoiceId() {
		return idInvoice;
	}
	public void setInvoiceId(String invoiceId) {
		this.idInvoice = invoiceId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
}
