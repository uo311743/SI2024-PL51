package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import DTOs.ActivitiesDTO;
import DTOs.InvoicesDTO;
import DTOs.SponsorOrganizationsDTO;

public class InvoiceInstance {
    private String invoiceId, activity, issueDate;
    private String sponsorName, sponsorAddress, sponsorVAT;
    private String recipientName, recipientAddress, recipientVAT;
    private double subtotal, taxRate, taxAmount, total;

    // Constructor
    public InvoiceInstance(InvoicesDTO invoice, SponsorOrganizationsDTO sponsor, ActivitiesDTO activity) {
    	Params params = new Params();
    	
        this.invoiceId = invoice.getId();
        this.activity = activity.getName() + " Edition " + activity.getEdition();
        this.issueDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);

        this.sponsorName = sponsor.getName();
        this.sponsorAddress = sponsor.getAddress();
        this.sponsorVAT = sponsor.getVat();

        this.recipientName = params.getTaxName();
        this.recipientAddress = params.getTaxAddress();
        this.recipientVAT = params.getTaxNif();

        
        this.taxRate = Double.parseDouble(invoice.getTaxRate());
        this.total = Double.parseDouble(invoice.getTotalAmount());
        this.subtotal = total / (1 + taxRate);
        this.taxAmount = total - subtotal;
    }

    // Getters
    public String getInvoiceId() { return invoiceId; }
    public String getActivity() { return activity; }
    public String getIssueDate() { return issueDate; }
    public String getSponsorName() { return sponsorName; }
    public String getSponsorAddress() { return sponsorAddress; }
    public String getSponsorVAT() { return sponsorVAT; }
    public String getRecipientName() { return recipientName; }
    public String getRecipientAddress() { return recipientAddress; }
    public String getRecipientVAT() { return recipientVAT; }
    public double getSubtotal() { return subtotal; }
    public double getTaxRate() { return taxRate; }
    public double getTaxAmount() { return taxAmount; }
    public double getTotal() { return total; }
}
