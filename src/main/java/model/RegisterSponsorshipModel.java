package model;

import util.Database;

import java.util.Collections;
import java.util.List;

import DTOs.ActivitiesDTO;

public class RegisterSponsorshipModel {
	// Instance that allows the connection to the DB and execution of queries
	private Database db = new Database();

	public List<ActivitiesDTO> getActivitiesbyStatus(String... status) {
	    // Construcción de los placeholders en función del número de elementos en 'status'
	    String placeholders = String.join(",", Collections.nCopies(status.length, "?"));
	    String sql = "SELECT * FROM Activities WHERE status IN (" + placeholders + ")";

	    return db.executeQueryPojo(ActivitiesDTO.class, sql, (Object[]) status);
	}


	public List<Object[]> getAllSponsorsArray() {
	    String sql = "SELECT id || ' - ' || name AS item FROM SponsorOrganizations";
	    return db.executeQueryArray(sql);
	}

	public List<Object[]> getContactsBySponshorArray(String sponshor) {
		String sql = "SELECT id || ' - ' || name AS item FROM SponsorContacts WHERE idSponsorOrganization == ?";
	    return db.executeQueryArray(sql, sponshor);
	}

	public List<Object[]> getAllGBMembersArray() {
		String sql = "SELECT id || ' - ' || name AS item FROM GBMembers";
	    return db.executeQueryArray(sql);
	}

	public void insertSponsorshipAgreement(String idSponsorContact, String idGBMember, String activity, String amount, String date) {
		// TODO Add validations
		String sql = "INSERT INTO SponsorshipAgreements (idSponsorContact, idGBMember, activity, amount, date, status) VALUES "
				+ "(?, ?, ?, ?, ?, 'signed')";
        db.executeUpdate(sql, idSponsorContact, idGBMember, activity, amount, date);
	}
	
}
