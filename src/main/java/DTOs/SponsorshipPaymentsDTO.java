package DTOs;

public class SponsorshipPaymentsDTO {
    private Integer idSponsorshipPayment;
    private Integer idInvoice;
    private String dateSponsorshipPayment;
    private Double amountSponsorshipPayments;

    // Default constructor
    public SponsorshipPaymentsDTO() {
    }

    // Parameterized constructor
    public SponsorshipPaymentsDTO(Integer idSponsorshipPayment, Integer idInvoice, String dateSponsorshipPayment, Double amountSponsorshipPayments) {
        this.idSponsorshipPayment = idSponsorshipPayment;
        this.idInvoice = idInvoice;
        this.dateSponsorshipPayment = dateSponsorshipPayment;
        this.amountSponsorshipPayments = amountSponsorshipPayments;
    }

    // Getters and Setters
    public Integer getIdSponsorshipPayment() {
        return idSponsorshipPayment;
    }

    public void setIdSponsorshipPayment(Integer idSponsorshipPayment) {
        this.idSponsorshipPayment = idSponsorshipPayment;
    }

    public Integer getIdInvoice() {
        return idInvoice;
    }

    public void setIdInvoice(Integer idInvoice) {
        this.idInvoice = idInvoice;
    }

    public String getDateSponsorshipPayment() {
        return dateSponsorshipPayment;
    }

    public void setDateSponsorshipPayment(String dateSponsorshipPayment) {
        this.dateSponsorshipPayment = dateSponsorshipPayment;
    }

    public Double getAmountSponsorshipPayments() {
        return amountSponsorshipPayments;
    }

    public void setAmountSponsorshipPayments(Double amountSponsorshipPayments) {
        this.amountSponsorshipPayments = amountSponsorshipPayments;
    }

    @Override
    public String toString() {
        return "SponsorshipPaymentsDTO{" +
                "idSponsorshipPayment=" + idSponsorshipPayment +
                ", idInvoice=" + idInvoice +
                ", dateSponsorshipPayment='" + dateSponsorshipPayment + '\'' +
                ", amountSponsorshipPayments=" + amountSponsorshipPayments +
                '}';
    }
}