import java.util.*;


public class TableManager {
	@SuppressWarnings("serial")  
	// This suppress is for the "does not declare a static final serialVersionUID" warning.
	// That warning is irrelevant because serialVersionUID is used for de-serialization,
	// and we don't care because we are just using this structure for convenient
	// data storage, no need to care about de-serialization.
	private static TreeMap<Integer, Integer> CAPACITY_AND_COUNT = new TreeMap<Integer, Integer>() {{
		// put(tableCapacity, tableCount);
		put(10, 5);		// 5 tables with capacity 10
		put(8,  5);		// 5 tables with capacity 8
		put(4,  10);	// 10 tables with capacity 4
		put(2,  10);	// 10 tables with capacity 10
	}};

	private Table[] tables;

	private static final int getNumTables() {
		int numTables = 0;

		for (int tableCount : CAPACITY_AND_COUNT.values()) {
			numTables += tableCount;
		}
		
		return numTables;
	}
	
	// Expose return value of getNumTables() to public
	public static final int NUM_TABLES = getNumTables();


	/**
	 * Constructor for the TableManager "singleton".
	 */
	public TableManager() {
		// Create array to store tables
		int numTables = NUM_TABLES;
		this.tables = new Table[numTables];

		// Init table elements in the array...
		int tableId = 0; //1 for the first table

		// keySet() returns keys (=capacity) sorted from small to big.
		// This makes it so that this.tables will be sorted by table capacity
		// from smallest table to biggest.
		for ( int key : CAPACITY_AND_COUNT.keySet() ) {
			int capacity = key;
			int countNeeded = CAPACITY_AND_COUNT.get(key);

			// Init as many tables as needed for this given capacity
			for ( int i=0; i < countNeeded; i++ ) {
				this.tables[tableId] = new Table(tableId+1, capacity);
				tableId++;
			}
		}
	}


	/**
	 * Find a table with a given tableId.
	 * @param tableId	
	 * @return  {@link Table} with the given tableId
	 */
	private Table getTableById(int tableId) { 
		return this.tables[tableId - 1];
	}
	
	
	/**
	 * Lists all tables
	 * @return  Table[] containing all tables.
	 */
	private Table[] getAllTables() { return this.tables; }
	
	
	/**
	 * Returns a list of tables with the given status
	 * @param status
	 * @return
	 */
	private ArrayList<Table> getTablesByStatus(Table.Status status) {
		ArrayList<Table> tables = new ArrayList<Table>();
		for (Table t : this.getAllTables()) {
			if ( status == t.getStatus() )
				tables.add(t);
		}
		return tables;
	}
	
	
	/**
	 * Shorthand to fetch tables with AVAILABLE {@link Table.Status}.
	 * Use {@link #getUsableTables()} to include tables that have 
	 * expired {@link Reservation}s.
	 * @return  ArrayList<Table> of AVAILABLE tables
	 * @see #getUsableTables()
	 */
	public ArrayList<Table> getAvailableTables() { 
		return this.getTablesByStatus(Table.Status.AVAILABLE);
	}
	
	/**
	 * Shorthand to fetch tables with OCCUPIED {@link Table.Status}.
	 * @return  ArrayList<Table> of OCCUPIED tables
	 */
	public ArrayList<Table> getOccupiedTables() { 
		return this.getTablesByStatus(Table.Status.OCCUPIED);
	}
	
	/**
	 * Shorthand to fetch tables with RESERVED {@link Table.Status}.
	 * @return  ArrayList<Table> of RESERVED tables
	 */
	public ArrayList<Table> getReservedTables() { 
		return this.getTablesByStatus(Table.Status.RESERVED);
	}
	
	
	/**
	 * Gets ArrayList of {@link Table}s that have the AVAILABLE {@link Table.Status}.
	 *  
	 * This method will have the side-effect of updating the status of table
	 * {@link Reservation}s that have expired. These expired tables will be set to
	 * AVAILABLE status and included in the returned list of usable tables.
	 * 
	 * To list available tables, without side-effects, use {@link #getAvailableTables()}
	 * @return an ArrayList<Table> of {@link Table}s with AVAILABLE status
	 * @see #getAvailableTables()
	 */
	private ArrayList<Table> getUsableTables() {
		ArrayList<Table> usable = new ArrayList<Table>();

		for ( Table t : this.tables ) {
			Table.Status status = t.getStatus();

			switch (status) {
				case AVAILABLE:
					usable.add(t);
					break;
				
				case RESERVED:
					// Set to AVAILABLE and add to usable if reservation expired
					if ( t.isReservationExpired() ) {
						t.setStatus(Table.Status.AVAILABLE);
						usable.add(t);
					}
					break;
				
				default:
					break;
			}
		}
		
		return usable; 	
	}


	/**
	 * Sets {@link Table} with the given tableId to the given {@link Table.Status} 
	 * @param tableId
	 * @param status
	 */
	private void setTableStatus(int tableId, Table.Status status) {
		Table table = this.getTableById(tableId);
		table.setStatus(status);
	}
	
	
	/**
	 * Sets {@link Table} with the given tableId to occupied status 
	 * @param tableId
	 * @param status
	 */
	public void setTableOccupied(int tableId) {
		setTableStatus(tableId, Table.Status.OCCUPIED);
	}
	
	
	/**
	 * Sets {@link Table} with the given tableId to reserved status 
	 * @param tableId
	 * @param status
	 */
	public void setTableReserved(int tableId) {
		setTableStatus(tableId, Table.Status.RESERVED);
	}
	
	
	/**
	 * Sets {@link Table} with the given tableId to available status 
	 * @param tableId
	 * @param status
	 */
	public void setTableAvailable(int tableId) {
		setTableStatus(tableId, Table.Status.AVAILABLE);
	}


	/**
	 * Find ID of smallest table that can fit the given numPax
	 * @param numPax  Capacity of the found {@link Table} will be at least this big
	 * @return tableId, or -1 if not found 
	 */
	public int findEmptyTableForCapacity(int numPax) {
		// Return table id
		Table foundTable = this.findUsableTable(numPax);
		
		return foundTable == null ? -1 : foundTable.getTableId(); //return -1 == null
	}

	/**
	 * A simple check if table status is occupied return true
	 * else we know the table Id is false
	 * @deprecated  Renamed to {@link #isTableOccupied(int)}
	 * @param tableId: Table Id
	 * @return boolean
	 */
	@Deprecated
	public boolean checkTableForAuthenticity(int tableId) {
//		return this.getTableById(tableId).getStatus() == Table.Status.OCCUPIED;
		return this.isTableOccupied(tableId);
	}
	
	
	/**
	 * Checks if given table has the given status
	 * @param tableId  tableId to check
	 * @param status  {@link Table.Status} to check for
	 * @return
	 */
	private boolean isTableStatus(int tableId, Table.Status status) {
		return this.getTableById(tableId).getStatus() == status;
	}
	
	/**
	 * Returns true if the table with the given tableId has status occupied.
	 * @param tableId  tableId to check
	 * @return  Whether the {@link Table} is occupied
	 */
	public boolean isTableOccupied(int tableId) {
		return this.isTableStatus(tableId, Table.Status.OCCUPIED);
	}
	
	
	/**
	 * Returns true if the table with the given tableId has status reserved.
	 * @param tableId  tableId to check
	 * @return  Whether the {@link Table} is reserved
	 */
	public boolean isTableReserved(int tableId) {
		return this.isTableStatus(tableId, Table.Status.RESERVED);
	}
	
	
	/**
	 * Returns true if the table with the given tableId has status available.
	 * @param tableId  tableId to check
	 * @return  Whether the {@link Table} is available
	 */
	public boolean isTableAvailable(int tableId) {
		return this.isTableStatus(tableId, Table.Status.AVAILABLE);
	}
	
	
	/**
	 * Internal algorithm for finding smallest suitable table.
	 * @param minimumCapacity  Capacity of the found table will be at least this big
	 * @return Smallest usable {@link Table}, or null if not found
	 */
	private Table findUsableTable(int minimumCapacity) {
		Table foundTable = null;

		// Array is sorted from smallest table to biggest
		ArrayList<Table> usableTables = this.getUsableTables();
		for ( Table t : usableTables ) {
			boolean bigEnough = t.getCapacity() >= minimumCapacity;
			if ( bigEnough ) {
				foundTable = t;
				break;
			}
		}
		return foundTable;
	}
	

	/** 
	 * Used by ReservationManager to assign {@link Reservation}s to {@link Table}s.
	 * This will try to find a suitable table based on the numPax of the
	 * given Reservation.
	 * If a table is found, then its {@link Table.Status} and expiry will be updated.
	 * Prints a warning if no suitable table is found.
	 * @param reservation  The {@link Reservation} object to assign.
	 * @return  tableId of the {@link Table} allocated to the given Reservation
	 */
	public int allocateReservation(Reservation reservation) {
		int numPax = reservation.getNoOfPax();
		Date expiry = reservation.getExpiredTime();
		int contactNo = reservation.getContactNo();
		
		Table foundTable = this.findUsableTable(numPax);
		
		if ( foundTable == null ) {
			System.out.printf(
					"(WARNING) TableManager: Could not find suitable table for Reservation(contactNo=%d, numPax=%d)",
					contactNo, numPax);
			return 0;
		} else {
			foundTable.setStatus(Table.Status.RESERVED);
			foundTable.setExpiryDate(expiry);
			return foundTable.getTableId();
		}
	}
}
