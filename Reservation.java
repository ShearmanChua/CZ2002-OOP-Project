import java.util.Date;
import java.util.Calendar;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Reservation implements Serializable{

	private int contactNo;
	private Date arriveTime;
	private Date expiredTime;
	private int noOfPax;
	private String name;
	private boolean isExpired;
	private int tableAlloc;
	private int tableKind;
	private String session;
	static final long ONE_MINUTE_IN_MILLIS=60000;

	/**
	 * constructor
	 * @param contact
	 * @param arrivalTime
	 * @param pax
	 * @param name
	 */
	public Reservation(int contact, Date arrivalTime, int pax, String name) {
		this.contactNo = contact;
		this.arriveTime = arrivalTime;
		this.noOfPax = pax;
		this.tableKind = this.determineTableKind(pax);
		this.name = name;
		this.isExpired = false;
		Calendar cal = Calendar.getInstance();
		cal.setTime(arrivalTime);
		cal.add(Calendar.MINUTE, 15);
		this.expiredTime = cal.getTime();
		this.session = allocateSession(this.arriveTime);
		
	}
	/**
	 * return contact
	 * @return contact_no
	 */
	public int getContactNo() {
		return this.contactNo;
	}
	/**
	 * return arrival time
	 * @return arrive_Time
	 */
	public Date getArriveTime() {
		return this.arriveTime;
	}
	/**
	 * return reservation expire time
	 * @return expired_Time
	 */
	public Date getExpiredTime() {
		return this.expiredTime;
	}
	/**
	 * return reservation number of pax
	 * @return noOfPax
	 */
	public int getNoOfPax() {
		return this.noOfPax;
	}
	/**
	 * return name of person who booked reservation
	 * @return name
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * return true if the reservation has expired
	 * @return isExpired
	 */
	public boolean getIsExpired() {
		boolean expired = this.isExpired;
		// If not expired yet, check now to see if it is expired or not.
		if (!expired) {
			this.isExpired = this.checkIfExpired();
		}
		return this.isExpired;
	}
	/**
	 * method used to check against current date time to see if reservation has expired
	 * return true if expired, false if not expired
	 */
	private boolean checkIfExpired() {
		Date expTime = this.getExpiredTime();
		Date timeNow = new Date();
		// compareTo returns 0 if timeNow == expTime
		//           negative int if timeNow < expTime
		//           positive int if timeNow > expTime   
		return (timeNow.compareTo(expTime) > 0);
	}
	
	/**
	 * This method is used to check which table is suitable for the number of pax 
	 * in the reservation and return an integer number of the number of seats in that table 
	 * arrangement
	 * @return tableKind
	 */
	
	public int determineTableKind(int pax) {
		int tableKind;
		
		if(pax>8 && pax<=10) {
			tableKind=10;
		}
		else if(pax>4 && pax<=8) {
			tableKind=8;
		}
		else if(pax>2 && pax<=4) {
			tableKind=4;
		}
		else {
			tableKind=2;
		}
		return tableKind;
	}
	
	/**
	 * return reservation Table Kind
	 * @return tableKind
	 */
	
	public int getTableKind() {
		return tableKind;
	}
	
	/**
	 * return reservation table allocation
	 * @return tableAlloc
	 */
	public int getTableAlloc() {
		return this.tableAlloc;
	}
	public void setTableAlloc(int tableAlloc) {
		this.tableAlloc=tableAlloc;
	}
	public static String allocateSession(Date resDate) {
		
		String strDate = DateHandling.DateNoTimetoString(resDate);
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
		
		
		if((resDate.compareTo(amStartDate) >= 0)&&(resDate.compareTo(amEndDate) < 0)) {
			return "AM";
		}
		else if((resDate.compareTo(pmStartDate) >= 0)&&(resDate.compareTo(pmEndDate) < 0)) {
			return "PM";
		}
		
		return null;
	}
	public String getSession() {
		return session;
	}
	
}