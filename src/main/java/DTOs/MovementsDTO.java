package DTOs;

public class MovementsDTO {
    private int id;
    private Integer idType; // Nullable field
    private String concept;
    private double amount;
    private String date;

    // Constructors
    public MovementsDTO() {}

    public MovementsDTO(int id, Integer idType, String concept, double amount, String date) {
        this.id = id;
        this.idType = idType;
        this.concept = concept;
        this.amount = amount;
        this.date = date;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getIdType() {
        return idType;
    }

    public void setIdType(Integer idType) {
        this.idType = idType;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
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

    // toString method for debugging/logging
    @Override
    public String toString() {
        return "MovementsDTO{" +
                "id=" + id +
                ", idType=" + idType +
                ", concept='" + concept + '\'' +
                ", amount=" + amount +
                ", date='" + date + '\'' +
                '}';
    }
}