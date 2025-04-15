package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Contains validations pertaining to the nature of the data. Used in controller.
 */
public class SyntacticValidations {
    
    public static final String PATTERN_EMAIL =
    		"^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    public static final String PATTERN_PHONE = "^\\+?[1-9][0-9]{8,14}$";
    
    public static final String NIF_PATTERN = "^[A-HJNP-SUVW][0-9]{7}[0-9A-J]$";

    /**
     * Validates if the given object is an integer.
     *
     * @param number  the object to validate
     * @return true if the object is an integer, false otherwise
     */
    public static boolean isNumber(Object number)
    {
        try {
            Integer.parseInt(number.toString());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validates if the given object is a positive integer.
     * 
     * @param number  the object to validate (can be String, Number, etc.)
     * @param includeZero  if true, considers 0 as positive
     * @return true if the object is a positive integer, false otherwise
     */
    public static boolean isPositiveNumber(Object number, boolean includeZero) {
        try {
            int value = Integer.parseInt(number.toString());
            return includeZero ? value >= 0 : value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates if the given object is a decimal number.
     *
     * @param number  the object to validate
     * @return true if the object is a decimal number, false otherwise
     */
    public static boolean isDecimal(Object number)
    {
        try {
            Double.parseDouble(number.toString());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates if the given string follows the 'yyyy-MM-dd' date format.
     *
     * @param date    the date string to validate
     * @return true if the string follows the date format, false otherwise
     */
    public static boolean isDate(String date)
    {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Validates that an object is not null.
     *
     * @param obj     the object to validate
     * @return true if the object is not null, false otherwise
     */
    public static boolean isNotNull(Object obj)
    {
        return obj != null;
    }

    /**
     * Validates that a string is not empty or null.
     *
     * @param value   the string to validate
     * @return true if the string is not empty or null, false otherwise
     */
    public static boolean isNotEmpty(String value)
    {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Validates if a string matches a given regex pattern.
     *
     * @param value   the string to validate
     * @param regex   the regex pattern to match
     * @return true if the string matches the pattern, false otherwise
     */
    public static boolean matchesPattern(String value, String regex)
    {
        return value != null && value.matches(regex);
    }
}