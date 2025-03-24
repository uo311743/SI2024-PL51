package DTOs;

public class LevelsDTO {
    private String id;
    public String idActivity;
    private String name;
    private String fee;
    
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
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getFee() {
		return fee;
	}
	
	public void setFee(String fee) {
		this.fee = fee;
	}
}
