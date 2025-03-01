package DTOs;

public class SponsorContactDTO {
    private Integer idSponsorContact;
    private Integer idSponsorOrganization;
    private String nameSponsorContact;
    private String emailSponsorContact;
    private String phoneSponsorContact;

    // Default constructor
    public SponsorContactDTO() {
    }

    // Parameterized constructor
    public SponsorContactDTO(Integer idSponsorContact, Integer idSponsorOrganization, 
                                 String nameSponsorContact, String emailSponsorContact, 
                                 String phoneSponsorContact) {
        this.idSponsorContact = idSponsorContact;
        this.idSponsorOrganization = idSponsorOrganization;
        this.nameSponsorContact = nameSponsorContact;
        this.emailSponsorContact = emailSponsorContact;
        this.phoneSponsorContact = phoneSponsorContact;
    }

    // Getters and Setters
    public Integer getIdSponsorContact() {
        return idSponsorContact;
    }

    public void setIdSponsorContact(Integer idSponsorContact) {
        this.idSponsorContact = idSponsorContact;
    }

    public Integer getIdSponsorOrganization() {
        return idSponsorOrganization;
    }

    public void setIdSponsorOrganization(Integer idSponsorOrganization) {
        this.idSponsorOrganization = idSponsorOrganization;
    }

    public String getNameSponsorContact() {
        return nameSponsorContact;
    }

    public void setNameSponsorContact(String nameSponsorContact) {
        this.nameSponsorContact = nameSponsorContact;
    }

    public String getEmailSponsorContact() {
        return emailSponsorContact;
    }

    public void setEmailSponsorContact(String emailSponsorContact) {
        this.emailSponsorContact = emailSponsorContact;
    }

    public String getPhoneSponsorContact() {
        return phoneSponsorContact;
    }

    public void setPhoneSponsorContact(String phoneSponsorContact) {
        this.phoneSponsorContact = phoneSponsorContact;
    }

    @Override
    public String toString() {
        return "SponsorshipContactDTO{" +
                "idSponsorContact=" + idSponsorContact +
                ", idSponsorOrganization=" + idSponsorOrganization +
                ", nameSponsorContact='" + nameSponsorContact + '\'' +
                ", emailSponsorContact='" + emailSponsorContact + '\'' +
                ", phoneSponsorContact='" + phoneSponsorContact + '\'' +
                '}';
    }
}