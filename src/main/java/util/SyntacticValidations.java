package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Contains validations pertaining to the nature of the data. Used in controller.
 */
public class SyntacticValidations {
	
	public static final String PATTERN_EMAIL =
			"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\\\.[a-zA-Z]{2,}$";
	
	public static final String PATTERN_EMAIL =
			"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\\\.[a-zA-Z]{2,}$";
	
	private SyntacticValidations() {
		throw new IllegalStateException("Utility class");
	}
	
    /**
     * Validates if the given object is an integer.
     *
     * @param number  the object to validate
     * @param message the exception message if validation fails
     */
    public static void validateNumber(Object number, String message)
    {
        try { Integer.parseInt(number.toString()); }
        catch (NumberFormatException e) { throw new ApplicationException(message); }
    }

    /**
     * Validates if the given object is a decimal number.
     *
     * @param number  the object to validate
     * @param message the exception message if validation fails
     */
    public static void validateDecimal(Object number, String message)
    {
        try { Double.parseDouble(number.toString()); }
        catch (NumberFormatException e) { throw new ApplicationException(message); }
    }

    /**
     * Validates if the given string follows the 'yyyy-MM-dd' date format.
     *
     * @param date    the date string to validate
     * @param message the exception message if validation fails
     */
    public static void validateDate(String date, String message)
    {
        try
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate.parse(date, formatter);
        }
        catch (DateTimeParseException e) { throw new ApplicationException(message); }
    }

    /**
     * Validates that an object is not null.
     *
     * @param obj     the object to validate
     * @param message the exception message if validation fails
     */
    public static void validateNotNull(Object obj, String message)
    {
        if (obj == null) throw new ApplicationException(message);
    }

    /**
     * Validates that a string is not empty or null.
     *
     * @param value   the string to validate
     * @param message the exception message if validation fails
     */
    public static void validateNotEmpty(String value, String message)
    {
        if (value == null || value.trim().isEmpty())
            throw new ApplicationException(message);
    }

    /**
     * Validates a condition and throws an exception if the condition is false.
     *
     * @param condition the boolean condition to validate
     * @param message   the exception message if validation fails
     */
    public static void validateCondition(boolean condition, String message)
    {
        if (!condition) throw new ApplicationException(message);
    }

    /**
     * Validates if a string matches a given regex pattern.
     *
     * @param value   the string to validate
     * @param regex   the regex pattern to match
     * @param message the exception message if validation fails
     */
    public static void validateMatchesPattern(String value, String regex, String message)
    {
        if (value == null || !value.matches(regex))
            throw new ApplicationException(message);
    }
}

