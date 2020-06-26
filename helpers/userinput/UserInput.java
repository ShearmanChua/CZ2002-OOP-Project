package helpers.userinput;

import java.util.*;
import java.util.regex.Pattern;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class UserInput {
	private static Scanner sc = new Scanner(System.in);


	/**
	 * Fetches int from user. Also fixes the getInt();getNextLine(); problem for you.
	 * You can use an inline declaration of int[] accepts like this:
	 *     getInt(new int[]{1, 2, 3}, null, null)
	 * Also avoids the getInt();getNextLine(); problem for you.
	 * @param accepts - int[] specifying accepted inputs
	 * @param inputPrompt - Can be null to use default prompt
	 * @param errorPrompt - Can be null to use default error message when input is invalid
	 * @return int - User input cast to int
	 */
	public static int getInt(int[] accepts, String inputPrompt, String errorPrompt) {
		if (inputPrompt == null)
			inputPrompt = "Please enter any integer: ";
		
		if (errorPrompt == null)
			errorPrompt = "You have entered an invalid number.";
		
		Integer result = null;

		do {
			System.out.printf(inputPrompt);
			String input = sc.nextLine();
			
			if ( Pattern.matches("-?\\d+", input) ) {
				int n = Integer.parseInt(input);

				// No specified input restriction
				if ( accepts == null ) {
					result = n;

				// Check against input restriction
				} else {
					for ( int i : accepts ) {
						if ( n == i )
							result = i;
					}
				}
			} // end .matches()
			
			if ( result == null ) {
				System.out.println(errorPrompt);
			}
		} while ( result == null );
		
		return (int)result;
	}

	
	/**
	 * Shorthand no-arg overload for {@link #getInt(int[], String, String)}
	 * @return int
	 */
	public static int getInt() { return getInt(null, null, null); }
	
	
	/**
	 * Fetches int within a specific range from user.
	 * The accepted values are inclusive of the lower and upper limit, so the
	 * accepted values for getIntFromRange(0, 3) are [0, 3].
	 * Also avoids the getInt();getNextLine(); problem for you.
	 * @param lowerLimit - Lower bound of accepted value, inclusive
	 * @param upperLimit - Upper bound of accepted value, inclusive
	 * @param inPrompt - Can be null to use default prompt
	 * @param errPrompt - Can be null to use default error message when input is invalid
	 * @return int - User input cast to int
	 */
	public static int getIntFromRange(int lowerLimit, int upperLimit, 
			String inPrompt, String errPrompt) {
		Integer exactMatch = null; 
		
		if ( lowerLimit == upperLimit ) {
			exactMatch = lowerLimit;
		} else if ( lowerLimit > upperLimit ) {
			// Swap with temp var
			int smallNum = upperLimit;
			upperLimit = lowerLimit;
			lowerLimit = smallNum;
		}
		
		// Derive range of accepted ints
		int size = upperLimit - lowerLimit + 1;  // range (0, 3), size is 4
		int num = lowerLimit;
		int[] acceptRange = new int[size];
		for (int i=0; i<size; i++)
			acceptRange[i] = num++;

		// Special defaults for range of accepted vals
		if ( inPrompt == null ) 
			inPrompt = String.format("Please enter an integer between %d and %d (inclusive): ",
					lowerLimit, upperLimit);
		if ( errPrompt == null )
			errPrompt = "The number you entered is not within the range of accepted values.";
		
		if ( !(exactMatch == null) )
			// Why do you even need this???
			inPrompt = String.format("Please enter the number %d exactly to continue: ", lowerLimit);
		
		return getInt(acceptRange, inPrompt, errPrompt);
	}


	/**
	 * Shorthand overload for {@link #getIntFromRange(int, int, String, String)}
	 * @return int
	 */
	public static int getIntFromRange(int lowerLimit, int upperLimit) { 
		return getIntFromRange(lowerLimit, upperLimit, null, null);
	}
	
	
	/**
	 * Fetches double within a specific range from user.
	 * The accepted values are inclusive of the lower and upper limit, so the
	 * accepted values for getDouble(0, 3) are [0.0, 3.0].
	 * Also fixes the getDouble();getNextLine(); problem for you.
	 * @param min - Lower bound of accepted ints, inclusive
	 * @param max - Upper bound of accepted ints, inclusive
	 * @param inputPrompt - Can be null to use default prompt
	 * @param errorPrompt - Can be null to use default error message when input is invalid
	 * @return double - user input cast to double
	 */
	public static double getDouble(Double min, Double max, String inputPrompt, String errorPrompt) {
		// Ensure inputs are in order
		Double exactMatch = null;
		
		if ( min == max ) {
			exactMatch = min;
		} else if ( min > max ) {
			// swap using temp var
			Double smaller = min;
			min = max;
			max = smaller;
		}
		
		// Setup defaults
		if (inputPrompt == null)
			inputPrompt = "Please enter any number: ";
		
		if (errorPrompt == null)
			errorPrompt = "You have entered an invalid number.";
		
		Double result = null;
		do {
			System.out.printf(inputPrompt);
			String input = sc.nextLine();
			
			if ( Pattern.matches("-?\\d+(\\.\\d*)?", input) ) {
				double d = Double.parseDouble(input);

				// No specified input restriction
				if ( min == null & max == null ) {
					result = d;

				// Check for exactMatch using float precision threshold
				} else if ( exactMatch != null ) {
					double diff = d - (double)exactMatch;
					if ( diff < 1e-10 ) // precise to 0.0000000001
						result = d;

				// Check against min/max sizes
				// Also handles if only one of min or max is defined
				} else {
					boolean bigEnough = min == null ? true : d >= min;
					boolean smallEnough = max == null ? true : d <= max;
					if (bigEnough && smallEnough)
						result = d;
				}
			} // end .matches()
			
			if ( result == null ) {
				System.out.println(errorPrompt);
			}
		} while ( result == null );
		
		return (double)result;
	}


	/**
	 * Shorthand no-arg overload for {@link #getDouble(Double, Double, String, String)}
	 * @return double
	 */
	public static double getDouble() { return getDouble(null, null, null, null); }
	
	
	/**
	 * Fetches string from user with the specified maximum character length.
	 * @param maxLen - int indicating maximum length of input. Defaults to Integer.MAX_VALUE.
	 * @param inputFormat - Can be null to use default prompt
	 * @param errorFormat - Can be null to use default error message when input is invalid
	 * @return String
	 */
	public static String getString(Integer maxLen, String inputFormat, String errorFormat) {
		if ( maxLen == null )
			maxLen = Integer.MAX_VALUE;  // iirc this is correct...

		if ( inputFormat == null )
			inputFormat = "Please enter text (up to %d characters long): ";
		
		if ( errorFormat == null )
			errorFormat = "The text you have entered is too long (expecting only up to %d characters long).";
		
		String inputPrompt = String.format(inputFormat, maxLen);
		String errorPrompt = String.format(errorFormat, maxLen);

		String result = null;

		do {
			System.out.printf(inputPrompt);

			String input = sc.nextLine();
			
			if ( input.length() <= maxLen )
				result = input;
			
			if ( result == null ) {
				System.out.println(errorPrompt);
			}
		} while ( result == null );
		
		
		return result;
	}
	
	
	/**
	 * Shorthand no-arg overload for {@link #getString(Integer, String, String)}
	 * @return String
	 */
	public static String getString() { return getString(100, null, null); }
	
	
	/**
	 * Fetches Date input from user in a specific format.
	 * Checking for valid date formats happens inside {@link #stringToDate(String)}
	 * @param inputPrompt - Can be null to use default prompt
	 * @param errorPrompt - Can be null to use default error message when input is invalid
	 * @return Date
	 */
	public static Date getDate(String inputPrompt, String errorPrompt) {
		if ( inputPrompt == null )
			inputPrompt = "Please enter 'today/tomorrow/tmr/ytd/yesterday' or a custom date (dd/mm or dd/mm/yyyy): ";
			//inputPrompt = "Please enter 'today/tomorrow/tmr' or a custom date (dd/mm or dd/mm/yyyy); type 'cancel' to cancel: ";
		
		if ( errorPrompt == null )
			errorPrompt = "You have entered an invalid date.";
		
		Date output = null;
		boolean cancel = false;

		do {
			System.out.println(inputPrompt);
			
			String input = sc.nextLine();
			Calendar c = Calendar.getInstance();
			
			Date now = new Date();
			switch (input.toLowerCase()) {
				case "today":
					output = now;
					break;
				
				case "tomorrow":
				case "tmr":
					c.setTime(now);
					c.add(Calendar.DATE, 1);
					output = c.getTime();
					break;
				case "yesterday":
				case "ytd":
					c.setTime(now);
					c.add(Calendar.DATE, -1);
					output = c.getTime();
					break;
				
//				case "cancel":
//					cancel = true;
//					break;
				
				default:
					output = stringToDate(input);
					break;
			}

			if ( output == null )
				System.out.println(errorPrompt);

		} while ( output == null | cancel );
		
		return output;
	}
	
	
	/**
	 * Shorthand no-arg overload for {@link #getDate(String, String)}
	 * @return Date
	 */
	public static Date getDate() { return getDate(null, null); }
	
	
	/**
	 * Fetches phone number from user in a specific format.
	 * Note that 0 is a possible return value if the user inputs "0".
	 * This is useful as a sentinel value for "canceling".
	 * Checking for valid phone number formats happens inside 
	 * {@link #phoneNumberStringToInt(String)}
	 * @param inputPrompt - Can be null to use default prompt
	 * @param errorPrompt - Can be null to use default error message when input is invalid
	 * @return int
	 */
	public static int getPhoneNumber(String inputPrompt, String errorPrompt) {
		if ( inputPrompt == null )
			inputPrompt = "Please enter phone number: ";

		if ( errorPrompt == null )
			errorPrompt = "You have entered an invalid phone number.";

		Integer output = null;

		do {
			System.out.print(inputPrompt);

			String input = sc.nextLine();
			output = phoneNumberStringToInt(input);

			if ( output == null )
				System.out.println(errorPrompt);

		} while ( output == null );

		return (int)output;
	}


	/**
	 * Shorthand no-arg overload for {@link #getString(Integer, String, String)}
	 * @return String
	 */
	public static int getPhoneNumber() { return getPhoneNumber(null, null); }


	/**
	 * Converts input String to {@link Date} object.
	 * Can return <code>null</code> if input string does not match Date format.
	 * So far supported inputs are:
	 * <li><code>today</code></li>
	 * <li><code>tomorrow</code></li>
	 * <li><code>tmr</code></li>
	 * <li><code>DD/MM</code></li> format, like <code>22/03</code> for 22 Mar 2019 
	 * (resolves to date in current year)
	 * <li><code>DD/MM/YYYY</code></li> format, like <code>22/03/2018</code> 
	 * the year must start with 1 or 2)
	 * @param s - Input string to parse to Date
	 * @return Date or null if input is not in recognised format.
	 */
	public static Date stringToDate(String s) {
		String pattDayMonth     = "([1-9]|[1-2][1-9]|3[0-1])\\s*\\/\\s*([1-9]|1[0-2])";
		String pattDayMonthYear = pattDayMonth + "\\s*\\/\\s*[1-2]\\d{3}";
		
		String dateFmtStr = "dd/MM/yyyy";
		
		if ( Pattern.matches(pattDayMonth, s) ) {
			// If s doesn't have "/<given year>", we append "/<current year>" to s
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("/yyyy");
			s += dateFormat.format(now);
		} else if ( Pattern.matches(pattDayMonthYear, s) ) {
			// continue
		} else {
			return null;
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateFmtStr);
		try {
			return dateFormat.parse(s);
		} catch (ParseException e) {
			// if for some reason the input doesn't match the SimpleDateFormat 
			// pattern, just give up here
			return null;
		}
	}
	

	/**
	 * Converts input String to phone number as an int object.
	 * Phone numbers must start with 6, 8 or 9 and have 8 digits in total.
	 * There can be any number of spaces or hyphens (-) in the input string. 
	 * Can return 0 if input string is "0".
	 * Can return null if input string does not match phone number format.
	 * @param s - Input string to parse to phone number
	 * @return int or null if input is not in recognised format.
	 */
	public static Integer phoneNumberStringToInt(String s) {
		String pattPhoneNum = "(0|[689][\\d\\s-]*)";

		StringBuilder sb = new StringBuilder();
		Integer output = null;

		if ( Pattern.matches(pattPhoneNum, s) ) {
			for ( char c : s.toCharArray() ) {
				if ( Character.isDigit(c) ) 
					sb.append(c);
		}

		String output_s = sb.toString();
		if ( output_s.length() == 8 )
			output = Integer.parseInt(output_s);
		}

		return output;
	}
}

