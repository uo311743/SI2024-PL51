package DTOs;

public class SponsorshipAgreementDTO {
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
