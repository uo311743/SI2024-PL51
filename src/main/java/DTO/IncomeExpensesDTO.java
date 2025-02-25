package DTO;

public class IncomeExpensesDTO {
	private String idIE;
	private String conceptIE;
	private String amountIE;
	private String dateIE;
	private String statusIE;
	
	public String getIdIE() { return idIE; }
    public String getConceptIE() { return conceptIE; }
    public String getAmountIE() { return amountIE; }
    public String getDateIE() { return dateIE; }   
    public String getStatusIE() { return statusIE; } 
    
    public void setIdIE(String idIE) { this.idIE = idIE; }
    public void setConceptIE(String conceptIE) { this.conceptIE = conceptIE; }
    public void setAmountIE(String amountIE) { this.amountIE = amountIE; }
    public void setDateIE(String dateIE) { this.dateIE = dateIE; }
    public void setStatusIE(String statusIE) { this.statusIE = statusIE; }
}
