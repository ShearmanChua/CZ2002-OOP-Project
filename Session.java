import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;  
import java.util.*;

public class Session {

	private String period;
	private Date startTime;
	private Date endTime;

	/**
	 * 
	 * @param period - "AM" or "PM"
	 * @param start
	 * @param end
	 */
	public Session() {
		
		Date date = new Date();
		this.setStartTime(date);
		this.setEndTime(date);
		this.setPeriod(date);
		
		
	}

	public String getPeriod() {
		return this.period;
	}

	/**
	 * set period
	 * @param period
	 */
	private void setPeriod(Date curDate) {
		
		String strDate = DateHandling.DateNoTimetoString(curDate);
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
		
		
		if((curDate.compareTo(amStartDate) >= 0)&&(curDate.compareTo(amEndDate) < 0)) {
			this.period="AM";
		}
		else if((curDate.compareTo(pmStartDate) >= 0)&&(curDate.compareTo(pmEndDate) < 0)) {
			this.period="PM";
		}
		
	}
	
	// FIXME: setStart/EndTime change to private, change to 
	//        use public SetStartEndTime(Date start, Date end) -Wilson
	/**
	 * set period
	 * @return  start_time
	 */
	public Date getStartTime() {
		return this.startTime;
	}

	/**
	 * set start time
	 * @param start_time
	 */
	public void setStartTime(Date curDate) {
		
		String strDate = DateHandling.DateNoTimetoString(curDate);
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
		
		
		if((curDate.compareTo(amStartDate) >= 0)&&(curDate.compareTo(amEndDate) < 0)) {
			this.startTime=amStartDate;
		}
		else if((curDate.compareTo(pmStartDate) >= 0)&&(curDate.compareTo(pmEndDate) < 0)) {
			this.startTime=pmStartDate;
		}
	}

	/**
	 * get end time
	 * @return end_time
	 */
	public Date getEndTime(Date curDate) {
		return this.endTime;
	}

	/**
	 * set endTime
	 * @param end_time
	 */
	public void setEndTime(Date curDate) {
		String strDate = DateHandling.DateNoTimetoString(curDate);
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
		
		
		if((curDate.compareTo(amStartDate) >= 0)&&(curDate.compareTo(amEndDate) < 0)) {
			this.startTime=amEndDate;
		}
		else if((curDate.compareTo(pmStartDate) >= 0)&&(curDate.compareTo(pmEndDate) < 0)) {
			this.startTime=pmEndDate;
		}
	}

}