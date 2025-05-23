package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Contains validations pertaining to the business logic of the data. Used in the model.
 */
public class SemanticValidations
{
	// Instance that allows the connection to the DB and execution of queries
	private static Database db = new Database();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	
	// ============================================================
	
	public static void validateIdForTable(String id, String table, String message)
	{
		String sql = "SELECT EXISTS(SELECT 1 FROM " + table +" WHERE id = ?);";
		List<Object[]> result = db.executeQueryArray(sql, id);
		if(result.get(0)[0] == "0")
			throw new ApplicationException(message);
	}

	public static void validateName(String name) 
	{
	    if (name == null || name.trim().isEmpty()) {
	        throw new IllegalArgumentException("Name cannot be null or empty");
	    }
	    if (name.contains("-")) {
	        throw new IllegalArgumentException("Name cannot contain '-'");
	    }
	}


	public static void validateStatus(String status) 
	{
	    if (status == null || status.trim().isEmpty()) {
	        throw new IllegalArgumentException("Status cannot be null or empty");
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
    public static void validateNumberInRange(String number, String min, String max, String message)
    {
        double tmp_number = Double.parseDouble(number);
        double tmp_min = Double.parseDouble(min);
        double tmp_max = Double.parseDouble(max);

    	if (tmp_number < tmp_min || tmp_number > tmp_max)
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
    public static void validateDateInPast(String date, boolean includeToday, String message)
    {
        Date tmp_today = SwingMain.getTodayDate();
        Date tmp_date = null;
        try {tmp_date = DATE_FORMAT.parse(date);}
        catch (ParseException e) {
        	throw new UnexpectedException("Invalid date for validateDateInPast");
        }
        		
    	if(includeToday)
    		if (tmp_date.after(tmp_today))
        		throw new ApplicationException(message);
    	else if (tmp_date.after(tmp_today) || tmp_date.equals(tmp_today))
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
    public static void validateDateInFuture(String date, boolean includeToday, String message)
    {
    	Date tmp_today = SwingMain.getTodayDate();
        Date tmp_date = null;
        try {tmp_date = DATE_FORMAT.parse(date);}
        catch (ParseException e) {
        	throw new UnexpectedException("Invalid date for validateDateInPast");
        }

    	if(includeToday)
    		if (tmp_date.before(tmp_today))
        		throw new ApplicationException(message);
    	else if (tmp_date.before(tmp_today) || tmp_date.equals(tmp_today))
        		throw new ApplicationException(message);
    }

    /**
     * Validates that a value is positive.
     *
     * @param number  the number to validate
     * @param message the exception message if validation fails
     */
    public static void validatePositiveNumber(String number, String message)
    {
        double tmp_number = Double.parseDouble(number);
    	if (tmp_number <= 0)
    		throw new ApplicationException(message);
    }
    
    /**
     * Validates that a value is negative.
     *
     * @param number  the number to validate
     * @param message the exception message if validation fails
     */
    public static void validateNegativeNumber(String number, String message)
    {
        double tmp_number = Double.parseDouble(number);
    	if (tmp_number >= 0)
    		throw new ApplicationException(message);
    }
    
    /**
     * Validates that a value is positive or zero.
     *
     * @param number  the number to validate
     * @param message the exception message if validation fails
     */
    public static void validatePositiveNumberOrZero(String number, String message)
    {
        double tmp_number = Double.parseDouble(number);
    	if (tmp_number < 0)
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
    
    public static void validateDateBeforeTo(String date, String dateFuture, boolean includeToday, String message) {
    	Date tmp_future = Util.isoStringToDate(dateFuture);
        Date tmp_date = Util.isoStringToDate(date);

    	if(includeToday) {
    		if (tmp_date.after(tmp_future))
        		throw new ApplicationException(message);
    	}
    	else {
    		if (tmp_date.after(tmp_future) || tmp_date.equals(tmp_future))
        		throw new ApplicationException(message);
    	}
    }
    
    public static void validateDateAfterTo(String date, String datePast, boolean includeToday, String message) {
    	Date tmp_past = Util.isoStringToDate(datePast);
        Date tmp_date = Util.isoStringToDate(date);

        if(includeToday) {
    		if (tmp_date.before(tmp_past))
        		throw new ApplicationException(message);
    	}
    	else {
    		if (tmp_date.before(tmp_past) || tmp_date.equals(tmp_past))
        		throw new ApplicationException(message);
    	}
    }
    
    public static void validateDatesAtLeastOneYearApart(String date1, String date2, boolean includeOneYear, String message) {
        Date tmp_date1 = Util.isoStringToDate(date1);
        Date tmp_date2 = Util.isoStringToDate(date2);

        // Calculate the absolute difference in days
        long diffInMillies = Math.abs(tmp_date1.getTime() - tmp_date2.getTime());
        long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        // Check if the difference is less than 365 days (or 365 days if includeOneYear is false)
        if (includeOneYear) {
            if (diffInDays < 365) {
                throw new ApplicationException(message);
            }
        } else {
            if (diffInDays <= 365) {
                throw new ApplicationException(message);
            }
        }
    }
}
