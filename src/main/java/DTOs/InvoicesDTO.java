package DTOs;

public class InvoicesDTO {
    private Integer idInvoice;
    private Integer idSponsorshipAgreement;
    private String dateIssueInvoice;
    private String dateSentInvoice;
    private String dateExpirationInvoice;
    private Double totalAmountInvoice;
    private Double taxRateInvoice;
    private String statusInvoice;

    // Default constructor
    public InvoicesDTO() {
    }

    // Parameterized constructor
    public InvoicesDTO(Integer idInvoice, Integer idSponsorshipAgreement, String dateIssueInvoice, 
                       String dateSentInvoice, String dateExpirationInvoice, Double totalAmountInvoice, 
                       Double taxRateInvoice, String statusInvoice) {
        this.idInvoice = idInvoice;
        this.idSponsorshipAgreement = idSponsorshipAgreement;
        this.dateIssueInvoice = dateIssueInvoice;
        this.dateSentInvoice = dateSentInvoice;
        this.dateExpirationInvoice = dateExpirationInvoice;
        this.totalAmountInvoice = totalAmountInvoice;
        this.taxRateInvoice = taxRateInvoice;
        this.statusInvoice = statusInvoice;
    }

    // Getters and Setters
    public Integer getIdInvoice() {
        return idInvoice;
    }

    public void setIdInvoice(Integer idInvoice) {
        this.idInvoice = idInvoice;
    }

    public Integer getIdSponsorshipAgreement() {
        return idSponsorshipAgreement;
    }

    public void setIdSponsorshipAgreement(Integer idSponsorshipAgreement) {
        this.idSponsorshipAgreement = idSponsorshipAgreement;
    }

    public String getDateIssueInvoice() {
        return dateIssueInvoice;
    }

    public void setDateIssueInvoice(String dateIssueInvoice) {
        this.dateIssueInvoice = dateIssueInvoice;
    }

    public String getDateSentInvoice() {
        return dateSentInvoice;
    }

    public void setDateSentInvoice(String dateSentInvoice) {
        this.dateSentInvoice = dateSentInvoice;
    }

    public String getDateExpirationInvoice() {
        return dateExpirationInvoice;
    }

    public void setDateExpirationInvoice(String dateExpirationInvoice) {
        this.dateExpirationInvoice = dateExpirationInvoice;
    }

    public Double getTotalAmountInvoice() {
        return totalAmountInvoice;
    }

    public void setTotalAmountInvoice(Double totalAmountInvoice) {
        this.totalAmountInvoice = totalAmountInvoice;
    }

    public Double getTaxRateInvoice() {
        return taxRateInvoice;
    }

    public void setTaxRateInvoice(Double taxRateInvoice) {
        this.taxRateInvoice = taxRateInvoice;
    }

    public String getStatusInvoice() {
        return statusInvoice;
    }

    public void setStatusInvoice(String statusInvoice) {
        this.statusInvoice = statusInvoice;
    }

    @Override
    public String toString() {
        return "InvoicesDTO{" +
                "idInvoice=" + idInvoice +
                ", idSponsorshipAgreement=" + idSponsorshipAgreement +
                ", dateIssueInvoice='" + dateIssueInvoice + '\'' +
                ", dateSentInvoice='" + dateSentInvoice + '\'' +
                ", dateExpirationInvoice='" + dateExpirationInvoice + '\'' +
                ", totalAmountInvoice=" + totalAmountInvoice +
                ", taxRateInvoice=" + taxRateInvoice +
                ", statusInvoice='" + statusInvoice + '\'' +
                '}';
    }
}