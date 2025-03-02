package DTOs;

public class SponsorshipAgreementDTO {
<<<<<<< HEAD
	private String idSA;
	private String amountSA;
	private String dateSA;
	private String statusSA;

	public String getIdSA() { return idSA; }
    public String getAmountSA() { return amountSA; }
    public String getDateSA() { return dateSA; }
    public String getStatusSA() { return statusSA; }   

    public void setIdSA(String idSA) { this.idSA = idSA; }
    public void setAmountSA(String amountSA) { this.amountSA = amountSA; }
    public void setDateSA(String dateSA) { this.dateSA = dateSA; }
    public void setStatusSA(String statusSA) { this.statusSA = statusSA; }
}
=======
    private String sponsorOrgId;
    private String activity;
    private double amount;
    private String dateAgreement;

    public SponsorshipAgreementDTO() {}

    public SponsorshipAgreementDTO(String sponsorOrgId, String activity, double amount, String dateAgreement) {
        this.sponsorOrgId = sponsorOrgId;
        this.activity = activity;
        this.amount = amount;
        this.dateAgreement = dateAgreement;
    }

    public String getSponsorOrgId() {
        return sponsorOrgId;
    }

    public void setSponsorOrgId(String sponsorOrgId) {
        this.sponsorOrgId = sponsorOrgId;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDateAgreement() {
        return dateAgreement;
    }

    public void setDateAgreement(String dateAgreement) {
        this.dateAgreement = dateAgreement;
    }

    public String getIdSA() {
        return sponsorOrgId + "_" + activity;
    }
}
>>>>>>> refs/heads/develop
