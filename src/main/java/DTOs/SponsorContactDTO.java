package DTOs;

public class SponsorContactDTO {
	private String idSponsor;
	private String nameSponsor;
	private String emailSponsor;
	private String phoneSponsor;

	public String getIdSponsor() { return idSponsor; }
    public String getNameSponsor() { return nameSponsor; }
    public String getEmailSponsor() { return emailSponsor; }
    public String getPhoneSponsor() { return phoneSponsor; }   

    public void setIdSponsor(String idSponsor) { this.idSponsor = idSponsor; }
    public void setNameSponsor(String nameSponsor) { this.nameSponsor = nameSponsor; }
    public void setEmailSponsor(String emailSponsor) { this.emailSponsor = emailSponsor; }
    public void setPhoneSponsor(String phoneSponsor) { this.phoneSponsor = phoneSponsor; }
}
