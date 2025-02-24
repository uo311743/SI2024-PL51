package model;

public class Payment {
	private String nif;
	private String date;
	private String activity;
	private String amount;
	private String isbn;
	private String internationalType;
	private String internationalInfo;
	private String invoiceId;
	private String id;
	
	public String getNif() {
		return nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getInternationalType() {
		return internationalType;
	}
	public void setInternationalType(String internationalType) {
		this.internationalType = internationalType;
	}
	public String getInternationalInfo() {
		return internationalInfo;
	}
	public void setInternationalInfo(String internationalInfo) {
		this.internationalInfo = internationalInfo;
	}
	public String getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
