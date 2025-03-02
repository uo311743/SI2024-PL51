package DTOs;

public class ActivitiesDTO {
	private String idActivities;
	private String nameActivities;
	private String editionActivities;
	private String stateActivities;
	private String dateActivities;
	private String placeActivities;
	private String estimatedIncomeActivities;
	private String estimatedExpensesActivities;

	public String getIdActivities() { return idActivities; }
    public String getNameActivities() { return nameActivities; }
    public String getEditionActivities() { return editionActivities; }
    public String getStateActivities() { return stateActivities; }
    public String getDateActivities() { return dateActivities; }  
    public String getPlaceActivities() { return placeActivities; }
    public String getEstimatedIncomeActivities() { return estimatedIncomeActivities; }
    public String getEstimatedExpensesActivities() { return estimatedExpensesActivities; } 

    public void setIdActivities(String idActivities) { this.idActivities = idActivities; }
    public void setEditionActivities(String editionActivities) { this.editionActivities = editionActivities; }
    public void setNameActivities(String nameActivities) { this.nameActivities = nameActivities; }
    public void setStateActivities(String stateActivities) { this.stateActivities = stateActivities; }
    public void setDateActivities(String dateActivities) { this.dateActivities = dateActivities; }
    public void setPlaceActivities(String placeActivities) { this.placeActivities = placeActivities; }
    public void setEstimatedIncomeActivities(String estimatedIncomeActivities) { this.estimatedIncomeActivities = estimatedIncomeActivities; }
    public void setEstimatedExpensesActivities(String estimatedExpensesActivities) { this.estimatedExpensesActivities = estimatedExpensesActivities; }
}
