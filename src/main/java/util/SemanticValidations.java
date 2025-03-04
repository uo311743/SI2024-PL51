package util;

import java.util.Date;
import java.util.List;

/**
 * Contains validations pertaining to the business logic of the data. Used in the model.
 */
public class SemanticValidations
{
	// Instance that allows the connection to the DB and execution of queries
	private static Database db = new Database();
	
	// ============================================================
	
	public static void validateIdSponsorContact(String idSponsorContact)
	{
		String sql = "SELECT EXISTS(SELECT 1 FROM SponsorContacts WHERE id = ?);";
		List<Object[]> result = db.executeQueryArray(sql, idSponsorContact);
		if(result.get(0)[0] == "0")
			throw new ApplicationException("The provided idSponsorContact does not exist in table SponsorContacts");
	}
	public static void validateIdGBMember(String idGBMember)
	{
		String sql = "SELECT EXISTS(SELECT 1 FROM GBMembers WHERE id = ?);";
		List<Object[]> result = db.executeQueryArray(sql, idGBMember);
		if(result.get(0)[0] == "0")
			throw new ApplicationException("The provided idGBMember does not exist in table GBMembers");
	}
	public static void validateIdActivity(String idActivity)
	{
		String sql = "SELECT EXISTS(SELECT 1 FROM Activities WHERE id = ?);";
		List<Object[]> result = db.executeQueryArray(sql, idActivity);
		if(result.get(0)[0] == "0")
			throw new ApplicationException("The provided idActivity does not exist in table Activities");
	}
	public static void validateIdSponsorOrganization(String idSponsorOrganization)
	{
		String sql = "SELECT EXISTS(SELECT 1 FROM SponsorOrganizations WHERE id = ?);";
		List<Object[]> result = db.executeQueryArray(sql, idSponsorOrganization);
		if(result.get(0)[0] == "0")
			throw new ApplicationException("The provided idSponsorOrganization does not exist in table SponsorOrganizations");
	}
	public static void validateName(String name) 
	{
	    if (name == null || name.trim().isEmpty()) {
	        throw new IllegalArgumentException("Name cannot be null or empty");
	    }
	    if (name.matches("-")) {
	        throw new IllegalArgumentException("No dash allowed");
	    }
	}

	
	
	// ============================================================
	
    /**
     * Validates if a number is within a specified range.
     *
     * @param number  the number to validate
     * @param min     the minimum allowed value
     * @param max     the maximum allowed value
     * @param message the exception message if validation fails
     */
    public static void validateNumberInRange(int number, int min, int max, String message)
    {
    	if (number < min || number > max)
    		throw new ApplicationException(message);
    }

    /**
     * Validates if a date is in the past.
     *
     * @param date            the date to validate
     * @param today           today's date to compare against
     * @param includeToday    whether today's date should be considered as valid or not
     * @param message         the exception message if validation fails
     */
    public static void validateDateInPast(Date date, Date today, boolean includeToday, String message)
    {
    	if(includeToday)
    		if (date.after(today))
        		throw new ApplicationException(message);
    	else if (date.after(today) || date.equals(today))
        		throw new ApplicationException(message);
    	}

    /**
     * Validates if a date is in the future.
     *
     * @param date            the date to validate
     * @param today           today's date to compare against
     * @param includeToday    whether today's date should be considered as valid or not
     * @param message         the exception message if validation fails
     */
    public static void validateDateInFuture(Date date, Date today, boolean includeToday, String message)
    {
    	if(includeToday)
    		if (date.before(today))
        		throw new ApplicationException(message);
    	else if (date.before(today) || date.equals(today))
        		throw new ApplicationException(message);
    }

    /**
     * Validates that a value is positive.
     *
     * @param number  the number to validate
     * @param message the exception message if validation fails
     */
    public static void validatePositiveNumber(double number, String message)
    {
    	if (number <= 0)
    		throw new ApplicationException(message);
    }
    
    /**
     * Validates that a value is positive or zero.
     *
     * @param number  the number to validate
     * @param message the exception message if validation fails
     */
    public static void validatePositiveNumberOrZero(double number, String message)
    {
    	if (number < 0)
    		throw new ApplicationException(message);    }

    /**
     * Validates that a string is one of the allowed values.
     *
     * @param value       the value to validate
     * @param validValues the array of allowed values
     * @param message     the exception message if validation fails
     */
    public static void validateAllowedValues(String value, String[] validValues, String message)
    {
        for (String validValue : validValues) if (validValue.equals(value)) return;
        throw new ApplicationException(message);
    }
}
