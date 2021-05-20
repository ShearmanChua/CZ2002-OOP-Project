import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;  
import java.util.*;

public class DateHandling {
	
	/**
	 * return a date object with time stamp from a string being passed in
	 * @return date
	 */
	public static Date stringToDateTime(String strDate) {
		Date date=null;
		try {
			date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(strDate); //string must be in dd/MM/yyyy HH:mm format 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to parse into Date Object");
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * return a date object from a string being passed in
	 * @return date
	 */
	public static Date stringToDate(String strDate) {
		Date date=null;
		try {
			date = new SimpleDateFormat("dd/MM/yyyy").parse(strDate); //string must be in dd/MM/yyyy format 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to parse into Date Object");
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * return a string of date and time object from a date being passed in
	 * @return strDate
	 */
	public static String DateTimetoString(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");  
		String strDate = dateFormat.format(date);
		return strDate;
	}
	/**
	 * return a string of date without time object from a date being passed in
	 * @return strDate
	 */
	public static String DateNoTimetoString(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");  
		String strDate = dateFormat.format(date);
		return strDate;
	}
	/**
	 * return a boolean value to check if the date format passed in is valid
	 * @return true or false
	 */
	public static boolean isThisDateValid(String dateToValidate){
			
			if(dateToValidate == null){
				return false;
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			sdf.setLenient(false);
			
			try {
				
				//if not valid, it will throw ParseException
				Date date = sdf.parse(dateToValidate);
				//System.out.println(date);
			
			} catch (ParseException e) {
				
				return false;
			}
			
			return true;
	}
	/**
	 * return a boolean value to check if the time format passed in is valid
	 * @return true or false
	 */
	public static boolean isThisTimeValid(String timeToValidate){
		
		if(timeToValidate == null){
			return false;
		}
		
		Date date=null;
		
		try {
			date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(timeToValidate); //string must be in dd/MM/yyyy HH:mm format 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to parse into Date Object");
			e.printStackTrace();
		}
		
		String strDate = DateHandling.DateNoTimetoString(date);
		
		
		String amSessionStart = strDate + " 11:00";
		String amSessionEnd = strDate + " 15:00";
		String pmSessionStart = strDate + " 18:00";
		String pmSessionEnd = strDate + " 22:00";
		Date amStartDate=null;
		Date amEndDate=null;
		Date pmStartDate=null;
		Date pmEndDate=null;
		
		amStartDate=DateHandling.stringToDateTime(amSessionStart);
		amEndDate=DateHandling.stringToDateTime(amSessionEnd);
		pmStartDate=DateHandling.stringToDateTime(pmSessionStart);
		pmEndDate=DateHandling.stringToDateTime(pmSessionEnd);
		
		
		if((date.compareTo(amStartDate) >= 0)&&(date.compareTo(amEndDate) < 0)) {
			return true;
		}
		else if((date.compareTo(pmStartDate) >= 0)&&(date.compareTo(pmEndDate) < 0)) {
			return true;
		}
		
		return false;

	}
	/**
	 * return the current date time based on server time
	 * @return date
	 */
	public static Date getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		String strdate = dateFormat.format(date);
		try {
			date = dateFormat.parse(strdate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * return the date time exactly 1 month from now based on server time
	 * @return date
	 */
	public static Date getMonthLaterDate() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		String strdate = dateFormat.format(date);
		try {
			date = dateFormat.parse(strdate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.add(Calendar.MONTH, 1);
	    strdate = dateFormat.format(calendar.getTime());
	    try {
			date = dateFormat.parse(strdate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * return true if the date the user keyed in is within one month from current date, else
	 * return false
	 * @return true or false
	 */
	public static boolean withinOneMonth(String dateToValidate) {
		Date date=null;
		
		try {
			date = new SimpleDateFormat("dd/MM/yyyy").parse(dateToValidate); //string must be in dd/MM/yyyy HH:mm format 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to parse into Date Object");
			e.printStackTrace();
		}
		if((date.compareTo(DateHandling.getCurrentDate()) >= 0)&&(date.compareTo(DateHandling.getMonthLaterDate()) <= 0)) {
			return true;
		}
		
		return false;
	}
		
}