package DTOs;

public class IncomesExpensesDTO {

	private String id;
	private String idActivity;
	private String type;
	private String status;
	private String amountEstimated;
	private String dateEstimated;
	private String concept;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdActivity() {
		return idActivity;
	}
	public void setIdActivity(String idActivity) {
		this.idActivity = idActivity;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAmountEstimated() {
		return amountEstimated;
	}
	public void setAmountEstimated(String amountEstimated) {
		this.amountEstimated = amountEstimated;
	}
	public String getDateEstimated() {
		return dateEstimated;
	}
	public void setDateEstimated(String dateEstimated) {
		this.dateEstimated = dateEstimated;
	}
	public String getConcept() {
		return concept;
	}
	public void setConcept(String concept) {
		this.concept = concept;
	}
}
