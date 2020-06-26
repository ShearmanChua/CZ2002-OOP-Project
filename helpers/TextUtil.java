package helpers;

import java.text.SimpleDateFormat;
import java.util.*;

public class TextUtil {
	/**
	 * If user does not specify truncation length for {@link #truncate(Object, Integer)},
	 * then this value is used by default.
	 * @see #truncate(Object, Integer)
	 */
	public static final int DEFAULT_TRUNC_LENGTH = 80;
	
	/**
	 * The shortest length that strings can be truncated to by {@link #truncate(Object, Integer)}.
	 * @see TextUtil#truncate(Object, Integer)
	 */
	public static final int TRUNCATE_MIN_LENGTH = 3;

	/**
	 * Default date-time format as used in this package.<br/>
	 * Example: <code>06-Apr-19-16:22</code>
	 * @see #dateToStringFormat(Date, String)
	 * @see #dateToString(Date)
	 */
	public static final String DEFAULT_DATETIME_FORMAT = "dd-MMM-yy-HH:mm";

	
	/**
	 * Truncates the string representation of the input object to the given length.<br/>
	 * Truncated strings will end with <code>...</code>, unless adding it would
	 * make the output string longer than <code>lengthLimit</code>.<br/>
	 * If <code>lengthLimit</code> is not provided, then the string will be truncated to the 
	 * length defined at {@link #DEFAULT_TRUNC_LENGTH}.<br/>
	 * The shortest length that strings can be truncated to is controlled by
	 * {@link #TRUNCATE_MIN_LENGTH}. 
	 * @param thing
	 * @param lengthLimit
	 * @return  The truncated input string, which will not be longer than the given <code>lengthLimit</code>
	 */
	public static String truncate(Object thing, Integer lengthLimit) {
		String s = thing.toString();
		if ( lengthLimit == null )
			lengthLimit = DEFAULT_TRUNC_LENGTH;
		lengthLimit = Math.max(3, lengthLimit);
		
		int sLen = s.length();
		boolean tooLong = sLen > lengthLimit;
		
		if ( !tooLong )
			return s;

    boolean tooShort = (lengthLimit - 3) < TRUNCATE_MIN_LENGTH;
    if ( tooShort )
        return s.substring(0, lengthLimit);

		int subStrLen = lengthLimit - 3;
		
		String head = s.substring(0, subStrLen);
		String tail = "...";
		
		return head + tail;
	}

	
	/**
	 * Formats the input {@link Date} using the given <code>formatString</code>. 
	 * @param date  The date object to format.
	 * @param formatString  A valid String that is compatible with {@link SimpleDateFormat}.
	 * @return  A formatted string representation of the given Date.
	 */
	public static String dateToStringFormat(Date date, String formatString) {
		if ( date == null )
			return "null";
		SimpleDateFormat dateFmt = new SimpleDateFormat(formatString);
		return dateFmt.format(date);
	}
	
	
	/**
	 * Formats the given date according to {@link #DEFAULT_DATETIME_FORMAT}.
	 * @param date  The date object to format.
	 * @return  A string representation of the given Date with the 
	 *          format {@link #DEFAULT_DATETIME_FORMAT}.
	 */
	public static String dateToString(Date date) {
		return dateToStringFormat(date, DEFAULT_DATETIME_FORMAT);
	}

	
	/**
	 * Creates a string of length <code>totalWidth</code>, padded with the 
	 * given <code>paddingChar</code>, such that the input is center-aligned in 
	 * the output.<br/>
	 * If <code>paddingChar</code> is not provided, the default padding character
	 * is just single-width space (<code>" "</code>).
	 * @param thing  The {@link Object} to be center-aligned. The string 
	 * 				 representation from calling <code>thing.toString()</code> 
	 *               will be used.
	 * @param totalWidth  The length of the output string.
	 * @param paddingChar  The character (or String) used to align <code>thing</code> 
	 *                     to the center in the output. If this argument is a String
	 *                     of length 2, then the output string will be roughly
	 *                     twice as long.
	 * @return  A String of length <code>totalWidth</code>, where the input object 
	 * 		    is center-aligned.
	 */
	public static String alignCenter(Object thing, Integer totalWidth, String paddingChar) {
		// Convert null inputs to usable values
		String text = thing.toString();
		int width = ( totalWidth == null ) ? text.length() : totalWidth;
		String pad = ( paddingChar == null ) ? " " : paddingChar;
		
		int padSpace = Math.max(0, width - text.length());
		int padLeft = Math.floorDiv(padSpace, 2);
		int padRight = padSpace - padLeft;
		// padLeft will be smaller than right if padSpace is not even
		
		StringBuilder sb = new StringBuilder();
		for ( int i=0; i < padLeft; i++ )
			sb.append(pad);
		sb.append(text);
		for ( int i=0; i < padRight; i++ )
			sb.append(pad);
		
		return sb.toString();
	}
	
	
	/**
	 * Aligns the string representation of the input object to the left or right of
	 * the output string, which will have length <code>totalWidth</code>, padded by 
	 * the given <code>paddingChar</code>.
	 * @param thing  The {@link Object} to be aligned. The string  
	 * 				 representation from calling <code>thing.toString()</code> 
	 *               will be used.
	 * @param totalWidth  The length of the output string.
	 * @param paddingChar  The character (or String) used to align <code>thing</code> 
	 *                     to the left/right in the output. If this argument is a String
	 *                     of length 2, then the output string will be roughly
	 *                     twice as long.
	 * @return  A String of length <code>totalWidth</code>, where the input object 
	 * 		    is left/right-aligned.
	 */
	private static String align(Object thing, Integer totalWidth, String paddingChar, boolean alignLeft) {
		// Convert null inputs to usable values
		String text = thing.toString();
		int width = ( totalWidth == null ) ? text.length() : totalWidth;
		String pad = ( paddingChar == null ) ? " " : paddingChar;
		
		if ( text.length() > width )
			width = text.length();
		
		if ( alignLeft )
			width = -1 * width;
		
		String fmt = "%" + width + "s";
		return String.format(fmt, text);
	}
	
	
	/**
	 * Aligns the string representation of the input object to the right of
	 * the output string, which will have length <code>totalWidth</code>, padded by 
	 * the given <code>paddingChar</code>.
	 * @param thing  The {@link Object} to be aligned. The string  
	 * 				 representation from calling <code>thing.toString()</code> 
	 *               will be used.
	 * @param totalWidth  The length of the output string.
	 * @param paddingChar  The character (or String) used to align <code>thing</code> 
	 *                     to the right in the output. If this argument is a String
	 *                     of length 2, then the output string will be roughly
	 *                     twice as long.
	 * @return  A String of length <code>totalWidth</code>, where the input object 
	 * 		    is right-aligned.
	 */
	public static String alignRight(Object thing, Integer totalWidth, String paddingChar) {
		return align(thing, totalWidth, paddingChar, false);
	}


	/**
	 * Aligns the string representation of the input object to the left of
	 * the output string, which will have length <code>totalWidth</code>, padded by 
	 * the given <code>paddingChar</code>.
	 * @param thing  The {@link Object} to be aligned. The string  
	 * 				 representation from calling <code>thing.toString()</code> 
	 *               will be used.
	 * @param totalWidth  The length of the output string.
	 * @param paddingChar  The character (or String) used to align <code>thing</code> 
	 *                     to the left in the output. If this argument is a String
	 *                     of length 2, then the output string will be roughly
	 *                     twice as long.
	 * @return  A String of length <code>totalWidth</code>, where the input object 
	 * 		    is left-aligned.
	 */
	public static String alignLeft(Object thing, Integer totalWidth, String paddingChar) {
		return align(thing, totalWidth, paddingChar, true);
	}
	
	
	/**
	 * Creates a string where the given Object is repeated for the given number of times.
	 * @param thing  The {@link Object} to be repeated in the output. The string  
	 * 				 representation from calling <code>thing.toString()</code> 
	 *               will be used.
	 * @param count  Number of times to repeat the string. The absolute (positive) value
	 *               is used.
	 * @return  String where the given Object is repeated for the given number of times.
	 */
	public static String repeatString(Object thing, int count) {
		String str = thing.toString();
		count = Math.abs(count);
		StringBuilder sb = new StringBuilder();
		for ( int i=0; i<count; i++ )
			sb.append(str);
		return sb.toString();
	}
}
