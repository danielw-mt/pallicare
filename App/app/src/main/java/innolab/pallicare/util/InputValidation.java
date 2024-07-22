package innolab.pallicare.util;

/**
 * This class can be used to cast and validate strings to other data types
 * TODO simplify with just valueOf and thrown error or add more validation checks?
 */
public class InputValidation {

    /**
     * Validate a string against an int
     * @param input a string
     * @return any int. 0 as well if no string.
     */
    public static int isStringValidInt(String input) {
        if (input.trim().isEmpty()) {
            return 0;
        }
        return Integer.valueOf(input);
    }

    /**
     * Validate a string against a float
     * @param input a string
     * @return any float. 0 as well if no string.
     */
    public static float isStringValidFloat(String input) {
        if (input.trim().isEmpty()) {
            return 0.0f;
        }
        return Float.valueOf(input);
    }

}
