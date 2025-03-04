package DTOs;

public class SponsorshipAgreementsDTO {
    private String id;
    private String idSponsorContact;
    private String idGBMember;
    private String idActivity;
    private String amount;
    private String date;
    private String status;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdSponsorContact() {
		return idSponsorContact;
	}
	public void setIdSponsorContact(String idSponsorContact) {
		this.idSponsorContact = idSponsorContact;
	}
	public String getIdGBMember() {
		return idGBMember;
	}
	public void setIdGBMember(String idGBMember) {
		this.idGBMember = idGBMember;
	}
	public String getIdActivity() {
		return idActivity;
	}
	public void setIdActivity(String idActivity) {
		this.idActivity = idActivity;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	} 
}