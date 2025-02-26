package DTOs;

public class InvoicesDTO {
	private String idInvoices;
	private String dateIssueInvoices;
	private String dateSendInvoices;

	public String getIdInvoices() { return idInvoices; }
    public String getDateIssueInvoices() { return dateIssueInvoices; }
    public String getDateSendInvoices() { return dateSendInvoices; } 

    public void setIdInvoices(String idInvoices) { this.idInvoices = idInvoices; }
    public void setDateIssueInvoices(String dateIssueInvoices) { this.dateIssueInvoices = dateIssueInvoices; }
    public void setDateSendInvoices(String dateSendInvoices) { this.dateSendInvoices = dateSendInvoices; }
}
