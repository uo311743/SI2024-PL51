package model;

import java.util.List;

import DTOs.ActivitiesDTO;
import DTOs.MovementsDTO;
import DTOs.SponsorOrganizationsDTO;
import DTOs.SponsorshipAgreementsDTO;
import util.Database;
import util.SemanticValidations;

public class ConsultStatusActivityModel
{
	// Instance that allows the connection to the DB and execution of queries
	private Database db = new Database();

	public List<ActivitiesDTO> getAllActivities()
	{
		String sql = "SELECT * FROM Activities;";
	    return db.executeQueryPojo(ActivitiesDTO.class, sql);
	}
	
	public List<SponsorshipAgreementsDTO> getApplicableSponsorshipAgreementsByActivity(String idActivity)
	{
		SemanticValidations.validateIdForTable(idActivity, "Activities",
				"ERROR. Provided idActivity for getSponsorshipAgreementsByActivity does not exist.");
		
	    String sql = "SELECT * FROM SponsorshipAgreements WHERE status IN ('signed', 'closed') AND idActivity = ?;";
	    return db.executeQueryPojo(SponsorshipAgreementsDTO.class, sql, idActivity);
	}
	
	public SponsorOrganizationsDTO getSponsorOrganizationByIdSponsorContact(String idSponsorContact)
	{
		SemanticValidations.validateIdForTable(idSponsorContact, "SponsorContacts",
				"ERROR. Provided idSponsorContact for getSponsorOrganizationByID does not exist.");
		
		String sql = "SELECT SO.* FROM SponsorOrganizations SO "
				+ "JOIN SponsorContacts SC ON SO.id = SC.idSponsorOrganization "
                + "WHERE SC.id = ?;";
	    return db.executeQueryPojo(SponsorOrganizationsDTO.class, sql, idSponsorContact).get(0);
	}

	public List<MovementsDTO> getIncomeByActivity(String idActivity)
	{
		SemanticValidations.validateIdForTable(idActivity, "Activities",
				"ERROR. Provided idActivity for getIncomeByActivity does not exist.");
		
		String sql = "SELECT * FROM Movements WHERE type = 'income' AND idActivity = ?;";
	    return db.executeQueryPojo(MovementsDTO.class, sql, idActivity);
	}
	
	public List<MovementsDTO> getExpensesByActivity(String idActivity)
	{
		SemanticValidations.validateIdForTable(idActivity, "Activities",
				"ERROR. Provided idActivity for getExpensesByActivity does not exist.");
		
		String sql = "SELECT * FROM Movements WHERE type = 'expense' AND idActivity = ?;";
	    return db.executeQueryPojo(MovementsDTO.class, sql, idActivity);
	}
	
	public double getEstimatedSponshorships(String idActivity)
	{
		SemanticValidations.validateIdForTable(idActivity, "Activities", 
		        "ERROR. Provided idActivity for getEstimatedSponshorships does not exist."
	    );

	    String sql = "SELECT SUM(amount) FROM SponsorshipAgreements WHERE status IN ('signed', 'closed') AND idActivity = ?;";
	    Object result = db.executeQueryArray(sql, idActivity).get(0)[0];
		if (result == null) {
			return 0.0;
		}
		return (double) result;
	}
	
	public double getActualSponshorships(String idActivity)
	{
		SemanticValidations.validateIdForTable(idActivity, "Activities", 
		        "ERROR. Provided idActivity for getActualSponshorships does not exist."
	    );

	    String sql = "SELECT SUM(amount) FROM SponsorshipAgreements WHERE status = 'closed' AND idActivity = ?;";
	    Object result = db.executeQueryArray(sql, idActivity).get(0)[0];
		if (result == null) {
			return 0.0;
		}
		return (double) result;
	}
	
	public double getEstimatedIncome(String idActivity)
	{
		SemanticValidations.validateIdForTable(idActivity, "Movements", 
		        "ERROR. Provided idActivity for getEstimatedIncome does not exist."
	    );

	    String sql = "SELECT SUM(amount) FROM Movements WHERE type = 'income' AND status IN ('estimated', 'paid') AND idActivity = ?;";
	    Object result = db.executeQueryArray(sql, idActivity).get(0)[0];
		if (result == null) {
			return 0.0;
		}
		return (double) result;
	}
	
	public double getActualIncome(String idActivity)
	{
		SemanticValidations.validateIdForTable(idActivity, "Movements", 
		        "ERROR. Provided idActivity for getEstimatedIncome does not exist."
	    );

	    String sql = "SELECT SUM(amount) FROM Movements WHERE type = 'income' AND status = 'paid' AND idActivity = ?;";
	    Object result = db.executeQueryArray(sql, idActivity).get(0)[0];
		if (result == null) {
			return 0.0;
		}
		return (double) result;
	}
	
	public double getEstimatedExpenses(String idActivity)
	{
		SemanticValidations.validateIdForTable(idActivity, "Movements", 
		        "ERROR. Provided idActivity for getEstimatedIncome does not exist."
	    );

	    String sql = "SELECT SUM(amount) FROM Movements WHERE type = 'expense' AND status IN ('estimated', 'paid') AND idActivity = ?;";
	    Object result = db.executeQueryArray(sql, idActivity).get(0)[0];
		if (result == null) {
			return 0.0;
		}
		return (double) result;
	}
	
	public double getActualExpenses(String idActivity)
	{
		SemanticValidations.validateIdForTable(idActivity, "Movements", 
		        "ERROR. Provided idActivity for getEstimatedIncome does not exist."
	    );

	    String sql = "SELECT SUM(amount) FROM Movements WHERE type = 'expense' AND status = 'paid' AND idActivity = ?;";
	    Object result = db.executeQueryArray(sql, idActivity).get(0)[0];
		if (result == null) {
			return 0.0;
		}
		return (double) result;
	}
}
