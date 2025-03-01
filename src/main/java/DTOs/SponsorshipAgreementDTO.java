package DTOs;

public class SponsorshipAgreementDTO {
    private Integer idSponsorshipAgreement;
    private Integer idSponsorContact;
    private Integer idGBMembers;
    private Double amountSponsorshipAgreement;
    private String dateSponsorshipAgreement;
    private String statusSponsorshipAgreement; // allowed values: 'closed', 'modified', 'cancelled'

    // Default constructor
    public SponsorshipAgreementDTO() {
    }

    // Parameterized constructor
    public SponsorshipAgreementDTO(Integer idSponsorshipAgreement, Integer idSponsorContact, Integer idGBMembers,
                                   Double amountSponsorshipAgreement, String dateSponsorshipAgreement, 
                                   String statusSponsorshipAgreement) {
        this.idSponsorshipAgreement = idSponsorshipAgreement;
        this.idSponsorContact = idSponsorContact;
        this.idGBMembers = idGBMembers;
        this.amountSponsorshipAgreement = amountSponsorshipAgreement;
        this.dateSponsorshipAgreement = dateSponsorshipAgreement;
        this.statusSponsorshipAgreement = statusSponsorshipAgreement;
    }

    // Getters and Setters
    public Integer getIdSponsorshipAgreement() {
        return idSponsorshipAgreement;
    }

    public void setIdSponsorshipAgreement(Integer idSponsorshipAgreement) {
        this.idSponsorshipAgreement = idSponsorshipAgreement;
    }

    public Integer getIdSponsorContact() {
        return idSponsorContact;
    }

    public void setIdSponsorContact(Integer idSponsorContact) {
        this.idSponsorContact = idSponsorContact;
    }

    public Integer getIdGBMembers() {
        return idGBMembers;
    }

    public void setIdGBMembers(Integer idGBMembers) {
        this.idGBMembers = idGBMembers;
    }

    public Double getAmountSponsorshipAgreement() {
        return amountSponsorshipAgreement;
    }

    public void setAmountSponsorshipAgreement(Double amountSponsorshipAgreement) {
        this.amountSponsorshipAgreement = amountSponsorshipAgreement;
    }

    public String getDateSponsorshipAgreement() {
        return dateSponsorshipAgreement;
    }

    public void setDateSponsorshipAgreement(String dateSponsorshipAgreement) {
        this.dateSponsorshipAgreement = dateSponsorshipAgreement;
    }

    public String getStatusSponsorshipAgreement() {
        return statusSponsorshipAgreement;
    }

    public void setStatusSponsorshipAgreement(String statusSponsorshipAgreement) {
        this.statusSponsorshipAgreement = statusSponsorshipAgreement;
    }

    @Override
    public String toString() {
        return "SponsorshipAgreementDTO{" +
                "idSponsorshipAgreement=" + idSponsorshipAgreement +
                ", idSponsorContact=" + idSponsorContact +
                ", idGBMembers=" + idGBMembers +
                ", amountSponsorshipAgreement=" + amountSponsorshipAgreement +
                ", dateSponsorshipAgreement='" + dateSponsorshipAgreement + '\'' +
                ", statusSponsorshipAgreement='" + statusSponsorshipAgreement + '\'' +
                '}';
    }
}