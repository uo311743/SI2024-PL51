package util;

import java.util.Date;

/**
 * Contains validations pertaining to the business logic of the data. Used in the model.
 */
public class SemanticValidations {

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
    	SyntacticValidations.validateCondition(number >= min && number <= max, message);
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
    		SyntacticValidations.validateCondition(date.before(today) || date.equals(today), message);
    	else
    		SyntacticValidations.validateCondition(date.before(today), message);
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
    		SyntacticValidations.validateCondition(date.after(today) || date.equals(today), message);
    	else
    		SyntacticValidations.validateCondition(date.after(today), message);
    }

    /**
     * Validates that a value is positive.
     *
     * @param number  the number to validate
     * @param message the exception message if validation fails
     */
    public static void validatePositiveNumber(double number, String message)
    {
    	SyntacticValidations.validateCondition(number > 0, message);
    }
    
    /**
     * Validates that a value is positive or zero.
     *
     * @param number  the number to validate
     * @param message the exception message if validation fails
     */
    public static void validatePositiveNumberOrZero(double number, String message)
    {
    	SyntacticValidations.validateCondition(number >= 0, message);
    }

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
    
    /**
     * Validates the given email address using a regular expression pattern.
     * The email is considered valid if it matches the pattern:
     * a sequence of alphanumeric characters, followed by an "@" symbol, then a domain, and a valid top-level domain (e.g., .com, .org).
     * 
     * @param email the email address to be validated
     * @throws ApplicationException if the email does not match the expected pattern
     */
    public static void validateEmail(String email, String message)
    {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        SyntacticValidations.validateMatchesPattern(email, emailRegex, message);
    }

    /**
     * Validates the given phone number using a regular expression pattern.
     * The phone number is considered valid if it matches the international phone number format,
     * optionally starting with a "+" followed by 1 to 15 digits.
     * 
     * @param phoneNumber the phone number to be validated
     * @throws ApplicationException if the phone number does not match the expected pattern
     */
    public static void validatePhoneNumber(String phoneNumber, String message)
    {
        String phoneRegex = "^\\+?[1-9]\\d{1,14}$";
        SyntacticValidations.validateMatchesPattern(phoneNumber, phoneRegex, message);
    }

}
