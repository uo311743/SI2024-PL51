package DTOs;

public class SponsorshipAgreementsDTO {
    private int id;
    private int idSponsorContact;
    private int idGBMember;
    private int idActivity;
    private double amount;
    private String date;
    private String status;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdSponsorContact() {
		return idSponsorContact;
	}
	public void setIdSponsorContact(int idSponsorContact) {
		this.idSponsorContact = idSponsorContact;
	}
	public int getIdGBMember() {
		return idGBMember;
	}
	public void setIdGBMember(int idGBMember) {
		this.idGBMember = idGBMember;
	}
	public int getIdActivity() {
		return idActivity;
	}
	public void setIdActivity(int idActivity) {
		this.idActivity = idActivity;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
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