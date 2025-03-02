package model;

import util.Database;

import java.util.List;

import DTOs.ActivitiesDTO;

public class RegisterSponshorshipModel {
	// Instance that allows the connection to the DB and execution of queries
	private Database db = new Database();
	
	public List<ActivitiesDTO> getAllActivities()
    {
        String sql = "SELEC* FROM";
        return db.executeQueryPojo(AthleteDTO.class, sql, competitionId);
    } 
}
