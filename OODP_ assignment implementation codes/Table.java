import java.util.*;
import java.text.SimpleDateFormat;


public class Table {
	/**
	 * Enum providing possible {@link Table} states.
	 * <li>{@link #AVAILABLE}</li>
	 * <li>{@link #OCCUPIED}</li>
	 * <li>{@link #RESERVED}</li>
	 */
	public enum Status {
		/**
		 * Table is marked available if it has no {@link Order} and 
		 * no {@link Reservation}.
		 */
		AVAILABLE,
		/**
		 * Table is marked occupied if it has an open {@link Order}.
		 */
		OCCUPIED,
		/**
		 * Table is marked reserved it it has a {@link Reservation}.
		 */
		RESERVED
	};

	private Status status;
	private Date expiryDate;
	private int capacity;
	private int tableId;
	
	private static final int EXPIRY_MINS = ReservationManager.EXPIRY_MINS; 


	/**
	 * Constructor.
	 * Tables initialize with the AVAILABLE {@link Status}.
	 * @param id  The table's unique <code>tableId</code>.
	 * @param capacity  Maximum number of people that can sit at this table.
	 */
	public Table(int id, int capacity) {
		this.tableId = id;
		this.capacity = capacity;
		this.status = Status.AVAILABLE;
		this.expiryDate = null;
	}


	// Getters for read-only properties
	public int getTableId() { return this.tableId; }

	public int getCapacity() { return this.capacity; }

	// Getters/setters for mutable properties
	public Status getStatus() { return this.status; }
	
	public void setStatus(Status status) { this.status = status; }
	
	public Date getExpiryDate() { return this.expiryDate; }
	
	public void setExpiryDate(Date date) { this.expiryDate = date; }
	
	
	/**
	 * Convenience method for getting the expiry date with the given format.
	 * @param formatString  Must be compatible with {@link SimpleDateFormat}.
	 * @return  The expiry date as a String in the given format, or 
	 * <code>"null"</code> if this {@link Table} has no expiry date.
	 */
	public String getExpiryDateFormatted(String formatString) {
		SimpleDateFormat dateFmt = new SimpleDateFormat(formatString);
		Date exp = this.getExpiryDate();
		return (exp != null)
				? dateFmt.format(exp)
				: "null";
	}
	
	/**
	 * Shorthand to fetch this {@link Table}'s expiry as a String, using  
	 * <code>"dd-MMM-yy-HH:mm"</code> as the date format. 
	 * @return  The expiry date as a String, or 
	 * <code>"null"</code> if this {@link Table} has no expiry date.
	 * @see #getExpiryDateFormatted(String)
	 */
	public String getExpiryDateFormatted() { return this.getExpiryDateFormatted("dd-MMM-yy-HH:mm"); }
	

	/**
	 * Check whether this table's {@link Reservation} has expired.
	 * @return <code>true</code> if this table has no Reservation, or if 
	 * time passed has exceeded {@link ReservationManager#EXPIRY_MINS}.<br />
	 * <code>false</code> if Reservation exists and has not expired.
	 */
	public boolean isReservationExpired() {
		Date exp = this.getExpiryDate();

		if (exp == null)
			return true;

		Date now = new Date();
		long timePassed_msecs = now.getTime() - exp.getTime();
		int timePassed_mins = (int) (timePassed_msecs / (60 * 1000));

		return timePassed_mins >= EXPIRY_MINS;
	}
	
	
	@Override
	public String toString() {
		return String.format(
				"Table[id=%d, status=%s, capacity=%d, expiryDate=%s]",
				this.getTableId(),
				this.getStatus(),
				this.getCapacity(),
				this.getExpiryDateFormatted());
	}

}
