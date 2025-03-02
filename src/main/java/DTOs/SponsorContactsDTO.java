package DTOs;

public class SponsorContactsDTO {
    private int id;
    private int idSponsorOrganization;
    private String name;
    private String email;
    private String phone;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdSponsorOrganization() {
		return idSponsorOrganization;
	}
	public void setIdSponsorOrganization(int idSponsorOrganization) {
		this.idSponsorOrganization = idSponsorOrganization;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	} 
}