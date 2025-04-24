package model;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import DTOs.LevelsDTO;
import DTOs.SponsorshipAgreementsDTO;
import util.ApplicationException;
import util.Database;
import util.SemanticValidations;
import util.Util;

public class SponsorshipAgreementsModel {

	public static final String SQL_NUMBER_SA = "SELECT COUNT(sa.id) AS total_agreements "
				+ "FROM SponsorshipAgreements sa "
				+ "JOIN SponsorContacts sc ON sa.idSponsorContact = sc.id "
				+ "WHERE sc.idSponsorOrganization = ( "
				+ "SELECT idSponsorOrganization "
				+ "FROM SponsorContacts "
				+ "WHERE id = ? ) AND sa.idActivity = ?;";
	
	public static final String SQL_NUMBER_LONG_SA = "SELECT COUNT(*) AS agreement_count "
			+ "FROM SponsorshipAgreements sa JOIN LongTermAgreementActivities lta ON sa.id = lta.idSponsorshipAgreement "
			+ "WHERE sa.idSponsorContact = ? AND lta.idActivity = ?;";
	
	public static final String SQL_SA_ID = "SELECT sa.id "
				+ "FROM SponsorshipAgreements sa "
				+ "JOIN SponsorContacts sc ON sa.idSponsorContact = sc.id "
				+ "JOIN SponsorOrganizations so ON sc.idSponsorOrganization = so.id "
				+ "JOIN Activities a ON a.id = sa.idActivity "
				+ "WHERE (so.nif = ? OR so.vat = ?) AND (a.name = ?);";
    
	private Database db = new Database();

	// GETTERS
	
	public List<SponsorshipAgreementsDTO> getApplicableSponsorshipAgreementsByActivity(String idActivity) {
		SemanticValidations.validateIdForTable(idActivity, "Activities", "ERROR. Provided idActivity for getSponsorshipAgreementsByActivity does not exist.");
	    String sql = "SELECT * FROM SponsorshipAgreements WHERE status IN ('signed', 'closed') AND idActivity = ?;";
	    String sql_la = "SELECT * FROM SponsorshipAgreements sa JOIN LongTermAgreementActivities lta ON sa.id = lta.idSponsorshipAgreement WHERE sa.status IN ('signed', 'closed') AND lta.idActivity = ?;";
	    List<SponsorshipAgreementsDTO> sa = db.executeQueryPojo(SponsorshipAgreementsDTO.class, sql, idActivity);
	    List<SponsorshipAgreementsDTO> lsa = db.executeQueryPojo(SponsorshipAgreementsDTO.class, sql_la, idActivity);
	    for (SponsorshipAgreementsDTO s : lsa) {
	    	sa.add(s);
	    }
	    return sa;
	}

    public double getEstimatedSponshorships(String idActivity) {
		SemanticValidations.validateIdForTable(idActivity, "Activities", "ERROR. Provided idActivity for getEstimatedSponshorships does not exist.");
		String sql = "SELECT SUM(amount) FROM SponsorshipAgreements WHERE status IN ('signed', 'closed') AND idActivity = ?;";
	    String sql_la = "SELECT SUM(sa.amount) "
	    		+ "FROM SponsorshipAgreements sa "
	    		+ "JOIN LongTermAgreementActivities lta ON sa.id = lta.idSponsorshipAgreement "
	    		+ "WHERE sa.status IN ('signed', 'closed') AND lta.idActivity = ?;";
		Object result = db.executeQueryArray(sql, idActivity).get(0)[0];
		Object result_la = db.executeQueryArray(sql_la, idActivity).get(0)[0];
		double amount_sa = result == null ? 0.0 : (double) result;
		double amount_lsa = result_la == null ? 0.0 : (double) result_la;
		
		return (amount_sa + amount_lsa);
	}
	
	public double getActualSponshorships(String idActivity) {
		SemanticValidations.validateIdForTable(idActivity, "Activities", "ERROR. Provided idActivity for getActualSponshorships does not exist.");
		String sql = "SELECT SUM(SP.amount) FROM SponsorshipAgreements SA "
				+ "JOIN Invoices I ON SA.id = I.idSponsorshipAgreement "
				+ "JOIN SponsorshipPayments SP ON I.id = SP.idInvoice "
				+ "WHERE idActivity = ?;";
	    Object result = db.executeQueryArray(sql, idActivity).get(0)[0];
		if (result == null) {
			return 0.0;
		}
		return (double) result;
	}

    public int getNumberOldSponsorshipAgreements(String idSponsorContact, String idActivity) {
		List<Object[]> result = db.executeQueryArray(SQL_NUMBER_SA, idSponsorContact, idActivity);
		List<Object[]> rs = db.executeQueryArray(SQL_NUMBER_LONG_SA, Integer.parseInt(idSponsorContact), idActivity);
		
		if (result == null || result.isEmpty()) {
			return 0;
		} else if (result == null || result.isEmpty()) {
			return 0;
		}
		
		return ((int)result.get(0)[0] + (int)rs.get(0)[0]);
	}

    public Integer getSponsorshipAgreementId(String nifOrVat, String activity) {
	    try {
	    	List<Object[]> result = db.executeQueryArray(SQL_SA_ID, nifOrVat, nifOrVat, activity);
		    if (result.isEmpty()) {
		    	throw new ApplicationException("No SponsorshipAgreement found");
		    } else {
		    	return Integer.parseInt(result.get(0)[0].toString());
		    }
	    } catch (Exception e) {
	    	throw new ApplicationException("Unexpected error while retrieving SponsorshipAgreement ID: " + e.getMessage());
	    }
	}
    
    public List<SponsorshipAgreementsDTO> getSignedAgreementsByActivityName(String activityName, String editionName) {
		SemanticValidations.validateName(activityName);
		SemanticValidations.validatePositiveNumberOrZero(editionName, "Not valid edition");
		String sql = "SELECT SA.* FROM SponsorshipAgreements SA JOIN Activities A ON SA.idActivity == A.id WHERE SA.status == 'signed' AND A.name == ? AND A.edition == ?;";
		return db.executeQueryPojo(SponsorshipAgreementsDTO.class, sql, activityName, editionName);
	}
    
    public String getFeeMaxByLevelFee(String feeLevelSelected) {
		SemanticValidations.validatePositiveNumber(feeLevelSelected, "Not valid fee");
		String sql = "SELECT * FROM Levels WHERE fee > ? ORDER BY fee ASC;";
		List<LevelsDTO> max = db.executeQueryPojo(LevelsDTO.class, sql, feeLevelSelected);
		if (max.size() == 0) {
			return "isTheMax";
		}
		return max.get(0).getFee();
	}

    // INSERTIONS

    public void insertNewSponsorshipAgreement(String idSponsorContact, String idGBMember, String idActivity, String amount, String date) {
		SemanticValidations.validateIdForTable(idSponsorContact, "SponsorContacts",
				"ERROR. Tried to insert a Sponsorship agreement with an unexisting idSponsorContact.");
		
		SemanticValidations.validateIdForTable(idGBMember, "GBMembers",
				"ERROR. Tried to insert a Sponsorship agreement with an unexisting idGBMember.");
		
		SemanticValidations.validateIdForTable(idActivity, "Activities",
				"ERROR. Tried to insert a Sponsorship agreement with an unexisting idActivity.");
		
		SemanticValidations.validatePositiveNumber(amount,
				"ERROR. Tried to insert a Sponsorship agreement with a non-positive amount.");
		
		SemanticValidations.validateDateInPast(date, true,
				"ERROR. Tried to insert a Sponsorship agreement with a future date.");
		
		if(getNumberOldSponsorshipAgreements(idSponsorContact, idActivity) != 0)
			throw new ApplicationException("Args provided to insertNewSponsorshipAgreement do not correspond to a new Agreement but an old one.");
		
		String sql = "INSERT INTO SponsorshipAgreements"
				+ "(idSponsorContact, idGBMember, idActivity, amount, date, status) VALUES "
				+ "(?, ?, ?, ?, ?, 'signed')";
		db.executeUpdate(sql, idSponsorContact, idGBMember, idActivity, amount, date);
	}
    
    public void insertNewLongTermSponsorshipAgreement(String idSponsorContact, String idGBMember, 
            List<String> activityIds, String amount, String date, String endDate) {
        // Validate main agreement inputs
        SemanticValidations.validateIdForTable(idSponsorContact, "SponsorContacts",
                "ERROR. Tried to insert a Sponsorship agreement with an unexisting idSponsorContact.");
        
        SemanticValidations.validateIdForTable(idGBMember, "GBMembers",
                "ERROR. Tried to insert a Sponsorship agreement with an unexisting idGBMember.");
        
        SemanticValidations.validateDateInPast(date, true,
                "ERROR. Tried to insert a Sponsorship agreement with a future date.");
        
        SemanticValidations.validateDateAfterTo(endDate, date, false, 
        		"ERROR. Tried to insert a Sponsorship agreement with a End Date previous to Start Date.");
        
        SemanticValidations.validateDatesAtLeastOneYearApart(endDate, date, false,
        		"ERROR. Tried to insert a Sponsorship agreement with a term of less than a year.");
        
        SemanticValidations.validatePositiveNumber(amount,
				"ERROR. Tried to insert a Sponsorship agreement with a non-positive amount.");

        for (int i = 0; i < activityIds.size(); i++) {
            String idActivity = activityIds.get(i);

            SemanticValidations.validateIdForTable(idActivity, "Activities",
                    "ERROR. Tried to insert a Sponsorship agreement with an unexisting idActivity.");
            
            // Check for existing agreements for this sponsor and activity
            if (getNumberOldSponsorshipAgreements(idSponsorContact, idActivity) != 0) {
                throw new ApplicationException("Args provided for activity " + idActivity + 
                        " correspond to an existing agreement.");
            }
        }

        // Insert into SponsorshipAgreements
        String sqlAgreement = "INSERT INTO SponsorshipAgreements " +
                "(idSponsorContact, idGBMember, amount, date, endDate, status) VALUES " +
                "(?, ?, ?, ?, ?, 'signed')";
        String agreementId = db.executeInsertion(sqlAgreement, idSponsorContact, idGBMember, 
                amount, date, endDate);

        // Insert into LongTermAgreementActivities
        String sqlActivity = "INSERT INTO LongTermAgreementActivities " +
                "(idSponsorshipAgreement, idActivity) VALUES (?, ?)";
        for (int i = 0; i < activityIds.size(); i++) {
            db.executeUpdate(sqlActivity, agreementId, 
                    activityIds.get(i));
        }
    }

    public void insertUpdateSponsorshipAgreement(String idSponsorContact, String idGBMember, String idActivity, String amount, String date) {
		SemanticValidations.validateIdForTable(idSponsorContact, "SponsorContacts",
				"ERROR. Tried to insert a Sponsorship agreement with an unexisting idSponsorContact.");
		
		SemanticValidations.validateIdForTable(idGBMember, "GBMembers",
				"ERROR. Tried to insert a Sponsorship agreement with an unexisting idGBMember.");
		
		SemanticValidations.validateIdForTable(idActivity, "Activities",
				"ERROR. Tried to insert a Sponsorship agreement with an unexisting idActivity.");
		
		SemanticValidations.validatePositiveNumber(amount,
				"ERROR. Tried to insert a Sponsorship agreement with a non-positive amount.");
		
		SemanticValidations.validateDateInPast(date, true,
				"ERROR. Tried to insert a Sponsorship agreement with a future date.");
		
		this.validateDateForUpdateSponsorshipAgreement(idSponsorContact, idActivity, date,
				"ERROR. There already exists a sponsorship after this one's date.");
		
		String sql = "INSERT INTO SponsorshipAgreements"
				+ "(idSponsorContact, idGBMember, idActivity, amount, date, status) VALUES "
				+ "(?, ?, ?, ?, ?, 'signed')";
		db.executeUpdate(sql, idSponsorContact, idGBMember, idActivity, amount, date);
		
		sql = "UPDATE SponsorshipAgreements "
				+ "SET status = 'modified' "
				+ "WHERE idActivity = ? "
				+ "AND idSponsorContact IN ("
				+ "    SELECT id "
				+ "    FROM SponsorContacts "
				+ "    WHERE idSponsorOrganization = ("
				+ "        SELECT idSponsorOrganization "
				+ "        FROM SponsorContacts "
				+ "        WHERE id = ?"
				+ "    )"
				+ ")";
		db.executeUpdate(sql, idActivity, idSponsorContact);
	}

	// SPECIFIC VALIDATIONS

	private void validateDateForUpdateSponsorshipAgreement(String idSponsorContact, String idActivity, String date, String message) {
		String sql = "SELECT MAX(sa.date) AS last_agreement_date "
				+ "FROM SponsorshipAgreements sa "
				+ "JOIN SponsorContacts sc ON sa.idSponsorContact = sc.id "
				+ "WHERE sc.idSponsorOrganization = ( "
				+ "    SELECT idSponsorOrganization "
				+ "    FROM SponsorContacts "
				+ "    WHERE id = ? "
				+ ") "
				+ "AND sa.idActivity = ?;";
		List<Object[]> result = db.executeQueryArray(sql, idSponsorContact, idActivity);
		
		if (result == null || result.isEmpty() || result.get(0)[0] == null) {
			return;
		}
		
		Date lastSponsorshipAgreementDate = Util.isoStringToDate((String) result.get(0)[0]);
		Date newSponsorshipAgreementDate = Util.isoStringToDate(date);
		
		if (newSponsorshipAgreementDate.before(lastSponsorshipAgreementDate)) {
			throw new ApplicationException(message);
		}
	}
}
