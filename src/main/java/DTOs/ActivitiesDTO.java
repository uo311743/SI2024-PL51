package DTOs;

public class ActivitiesDTO {
    private Integer idActivity;
    private String nameActivity;
    private String editionActivity;
    private String statusActivity;
    private String dateStartActivity;
    private String dateEndActivity;
    private String placeActivity;

    // Default constructor
    public ActivitiesDTO() {
    }

    // Parameterized constructor
    public ActivitiesDTO(Integer idActivity, String nameActivity, String editionActivity, 
                         String statusActivity, String dateStartActivity, String dateEndActivity, 
                         String placeActivity) {
        this.idActivity = idActivity;
        this.nameActivity = nameActivity;
        this.editionActivity = editionActivity;
        this.statusActivity = statusActivity;
        this.dateStartActivity = dateStartActivity;
        this.dateEndActivity = dateEndActivity;
        this.placeActivity = placeActivity;
    }

    // Getters and Setters
    public Integer getIdActivity() {
        return idActivity;
    }

    public void setIdActivity(Integer idActivity) {
        this.idActivity = idActivity;
    }

    public String getNameActivity() {
        return nameActivity;
    }

    public void setNameActivity(String nameActivity) {
        this.nameActivity = nameActivity;
    }

    public String getEditionActivity() {
        return editionActivity;
    }

    public void setEditionActivity(String editionActivity) {
        this.editionActivity = editionActivity;
    }

    public String getStatusActivity() {
        return statusActivity;
    }

    public void setStatusActivity(String statusActivity) {
        this.statusActivity = statusActivity;
    }

    public String getDateStartActivity() {
        return dateStartActivity;
    }

    public void setDateStartActivity(String dateStartActivity) {
        this.dateStartActivity = dateStartActivity;
    }

    public String getDateEndActivity() {
        return dateEndActivity;
    }

    public void setDateEndActivity(String dateEndActivity) {
        this.dateEndActivity = dateEndActivity;
    }

    public String getPlaceActivity() {
        return placeActivity;
    }

    public void setPlaceActivity(String placeActivity) {
        this.placeActivity = placeActivity;
    }

    @Override
    public String toString() {
        return "ActivitiesDTO{" +
                "idActivity=" + idActivity +
                ", nameActivity='" + nameActivity + '\'' +
                ", editionActivity='" + editionActivity + '\'' +
                ", statusActivity='" + statusActivity + '\'' +
                ", dateStartActivity='" + dateStartActivity + '\'' +
                ", dateEndActivity='" + dateEndActivity + '\'' +
                ", placeActivity='" + placeActivity + '\'' +
                '}';
    }
}