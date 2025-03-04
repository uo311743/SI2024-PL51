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

	public List<ActivitiesDTO> getAllActivitiesbyStatus()
	{
		String sql = "SELECT * FROM Activities;";
	    return db.executeQueryPojo(ActivitiesDTO.class, sql);
	}
	
	public List<SponsorshipAgreementsDTO> getSponsorshipAgreementsByActivity(String idActivity)
	{
		SemanticValidations.validateIdForTable(idActivity, "Activities",
				"ERROR. Provided idActivity for getSponsorshipAgreementsByActivity does not exist.");
		
		String sql = "SELECT * FROM SponsorshipAgreements WHERE idActivity = ?;";
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
		return 1;
	}
	
	public double getActualSponshorships(String idActivity)
	{
		return 2;
	}
	
	public double getEstimatedIncome(String idActivity)
	{
		return 3;
	}
	
	public double getActualIncome(String idActivity)
	{
		return 4;
	}
	
	public double getEstimatedExpenses(String idActivity)
	{
		return 5;
	}
	
	public double getActualExpenses(String idActivity)
	{
		return 6;
	}
}
