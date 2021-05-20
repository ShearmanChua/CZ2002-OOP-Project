import java.io.*;
import java.util.*;

import helpers.TextUtil;

public class OrderManager {

	private ReportManager repMgr;
	private TableManager tableMgr;
	private Order[] orders;
	private final int numTables = TableManager.NUM_TABLES; 
	
	private static final String DB_FILEPATH = "DB/orders.ser";
	private static final boolean DEBUG = false;
	
	/**
	 * Constructor for the TableManager "singleton".
	 * @param repMgr  The ReportManager instance. An internal reference to this will be stored.
	 * @param tableMgr  The TableManager instance. An internal reference to this will be stored.
	 */
	public OrderManager(ReportManager repMgr, TableManager tableMgr) {
    	this.repMgr = repMgr;
    	this.tableMgr = tableMgr;
    	this.orders = new Order[numTables];
    	this.restoreFromFile();
    }
	
	
	/**
	 * Converts the given <code>tableId</code> to the index position of its
	 * corresponding Order in the internal Order[] storage.<br />
	 * This is intended to create separation between the two similar values,
	 * and avoid off-by-one errors.
	 * @param tableId
	 * @return  Internal index position of the Order associated with the given tableId.
	 */
	private int tableIdToIdx(int tableId) {
		return tableId - 1;
	}


	/**
	 * Gets an array of all currently-open {@link Order}s.
	 * @return Order[]
	 */
	public Order[] getOrders() { return this.orders; }
	
	
	/**
	 * Checks whether given tableId is within the index range of the
	 * internal {@link Order}[] storage.
	 * @param tableId
	 * @return Whether the given tableId is within index range of <code>this.orders</code>.
	 */
	private boolean isValidTableId(int tableId) {
		int idx = this.tableIdToIdx(tableId);
		return 0 <= idx && idx <= this.orders.length;
	}


	/**
     * Find the Order for the given tableId.
     * @param tableId
     * @returns {@link Order} or <code>null</code> if tableId has no Order.
     */
    public Order getOrderByTableId(int tableId) {
    	int idx = this.tableIdToIdx(tableId);
    	return ( this.isValidTableId(tableId) )
    			? this.orders[idx]
				: null;
    }
	
	
	/**
	 * Used internally to destroy orders recorded inside OrderManager.
	 * Used when customer makes payment, after Order object is 
	 * transferred to the ReportManager.
	 * <br/>This method triggers a state dump to disk.
	 * @param tableId
	 * @see OrderManager#dumpToFile()
	 */
    private void clearOrderByTableId(int tableId) {
    	int idx = this.tableIdToIdx(tableId);
    	if ( this.isValidTableId(tableId) )
    		this.orders[idx] = null;
    	this.dumpToFile();
    }
    



    /**
     * Creates a new {@link Order} and stores it inside this OrderManager.
     * <br/>This method triggers a state dump to disk.
     * @param menuItems  ArrayList of {@link MenuItem}s to add to the order initially.
     * @param staffName  Name of the staff member creating the new Order.
     * @param staffId  Staff ID of the staff member creating the new Order. Alphanumeric.
	 * @param tableId  <code>tableId</code> of the Table for the new Order.
	 * @see OrderManager#dumpToFile()
     */
    public void newOrder(ArrayList<MenuItem> menuItems, String staffName, int staffId, int tableId) {
    	Order ord = new Order(menuItems, tableId, staffName, staffId);
    	int idx = this.tableIdToIdx(tableId);
    	this.orders[idx] = ord;
    	this.dumpToFile();
    }


    /**
     * Adds a {@link MenuItem} to the {@link Order} assigned to <code>tableId</code>.
     * <br/>This method triggers a state dump to disk.
     * @param tableId  The tableId of the Order
     * @param menuItem  The MenuItem to remove from the Order
     * @see OrderManager#dumpToFile()
     */
    public void addItemInOrder(int tableId, MenuItem menuItem) {
    	Order ord = this.getOrderByTableId(tableId);
    	ord.addItem(menuItem);
    	this.dumpToFile();
    }


    /**
     * Removes a {@link MenuItem} from the {@link Order} assigned to <code>tableId</code>.
     * <br/>This method triggers a state dump to disk.
     * @param tableId  The tableId of the Order
     * @param menuItem  The MenuItem to remove from the Order
     * @see OrderManager#dumpToFile()
     */
    public void removeItemInOrder( int tableId, MenuItem menuItem) {
    	Order ord = this.getOrderByTableId(tableId);
    	ord.removeItem(menuItem);
    	this.dumpToFile();
    }


    /**
     * Prints the invoice of the {@link Order} for the given <code>tableId</code>.
     * @param tableId
     */
    public void printInvoice(int tableId) {
    	final int colWidth = 72;  // use multiples of 6 for best effect
    	final int halfColWidth = Math.floorDiv(colWidth, 2);
    	final int oneSixthColWidth = Math.floorDiv(halfColWidth, 3);
    	
    	Order ord = this.getOrderByTableId(tableId);
    	
    	// Create header
    	String staffName = TextUtil.truncate("Staff: " + ord.getStaffName(), halfColWidth);
    	String staffId = TextUtil.truncate("Staff ID: " + ord.getStaffId(), halfColWidth);
    	
    	String dateTimeNow = TextUtil.dateToString(new Date());
    	String timestamp = TextUtil.truncate("Time: " + dateTimeNow, halfColWidth);
    	String tableInfo = String.format("Table: %d", ord.getTableId());
    	
    	String headerFmt = String.format(
    			" _%s_\n" +
    			"| %s |\n" +
				"| %s%s |\n" +
				"| %s%s |",
				TextUtil.repeatString("_", colWidth),
				TextUtil.alignCenter("ORDER INVOICE", colWidth, null),
				TextUtil.alignLeft(timestamp, halfColWidth, null), TextUtil.alignRight(staffName, halfColWidth, null),
				TextUtil.alignLeft(tableInfo, halfColWidth, null), TextUtil.alignRight(staffId, halfColWidth, null));
    	
    	System.out.println(headerFmt);

    	// Print "blank line"
    	System.out.println("| " + TextUtil.repeatString(" ", colWidth) + " |");
    	
    	// Create formatString for price rows
    	String priceRowFmt = (
    			"| " +
    			String.format("%%-%ds", halfColWidth - 3) + " " +
				String.format("%%-%ds", oneSixthColWidth) + " " +
				String.format("%%-%ds", oneSixthColWidth) + " " +
				String.format("%%-%ds", oneSixthColWidth) +
				" |");
    	
    	// Print price header
    	System.out.println(String.format(priceRowFmt, "Item", "Qty", "Price", "Item total"));
    	System.out.println(String.format(priceRowFmt, "----", "---", "-----", "----------"));
    	
    	Map<MenuItem, Integer> itemCount = ord.getItemCount();
    	Map<MenuItem, Double> itemPrice = ord.getItemPrice();
    	
    	for ( MenuItem mi : itemCount.keySet() ) {
    		String name =  TextUtil.truncate(mi.getName(), colWidth);
    		int count = itemCount.getOrDefault(mi, 0);
    		String qty = TextUtil.truncate(count, oneSixthColWidth);
    		double unitPrice = itemPrice.getOrDefault(mi, 0.0);
    		String price = TextUtil.truncate(String.format("%.2f", unitPrice), oneSixthColWidth);
    		String itemTotal = TextUtil.truncate(String.format("%.2f", unitPrice * count), oneSixthColWidth);
    		System.out.println("| " + TextUtil.alignLeft(name, colWidth, null) + " |");
//    		System.out.println(String.format(priceRowFmt, "", "", "", ""));
    		System.out.println(String.format(priceRowFmt, "", qty, price, itemTotal));
    	}
    	
    	// Print "blank line"
    	System.out.println("| " + TextUtil.repeatString(" ", colWidth) + " |");
    	
    	// Create formatString for summary rows
    	String summaryFmt = (
    			"| " +
    			String.format("%%%ds:", colWidth - oneSixthColWidth - 3) + " " +
				String.format("%%%ds", oneSixthColWidth) + " " +
//				TextUtil.repeatString(" ", oneSixthColWidth) +
    			" |");
    	
    	double nett = ord.getTotalNettPrice();
    	double svc = 0.10 * nett;
    	double gst = 0.07 * svc;
    	double grandTotal = nett + svc + gst;
    	
    	String nettStr = String.format("S$ %.2f", nett);
    	System.out.println(String.format(summaryFmt, "Subtotal", nettStr));
    	
    	String svcStr = String.format("S$ %.2f", svc);
    	System.out.println(String.format(summaryFmt, "Service charge", svcStr));
    	
    	String gstStr = String.format("S$ %.2f", gst);
    	System.out.println(String.format(summaryFmt, "GST", gstStr));
    	
    	// Print "blank line"
    	System.out.println("| " + TextUtil.repeatString(" ", colWidth) + " |");
    	
    	String grandTotalStr = String.format("S$ %.2f", grandTotal);
    	System.out.println(String.format(summaryFmt, "Grand Total", grandTotalStr));
    	
    	// Print footer
    	System.out.printf("|_%s_|\n", TextUtil.repeatString("_", colWidth));
    }
    

    /**
     * Send the {@link Order} to be archived in {@link ReportManager}.
     * If ReportManager successfully archives the Order, the local reference to that Order will be 
     * deleted. Otherwise, if the archiving fails, OrderManager will not delete Order reference.
     * 
     * Once the Order reference is deleted, OrderManager will no longer be able to access that Order.
     * 
     * <br/>This method triggers a state dump to disk.
     * @param tableId
     * @returns boolean indicating whether Order was successfully closed.
     * @see OrderManager#dumpToFile()
     */
    public boolean closeOrder(int tableId) {
    	boolean successfulArchive  = false;
    	Order ord = this.getOrderByTableId(tableId);

    	// ReportManager.addOrder() will try to create a copy for archiving
    	successfulArchive = this.repMgr.addOrder(ord);
    	
    	if ( successfulArchive ) {

			this.printInvoice(tableId); //FIXME: if u are fine with me putting it here
			System.out.printf("The order for table %d was successfully closed.%n", tableId);
    		this.clearOrderByTableId(tableId);   // destroy our local copy of Order
    	}
    	
    	// Should return true if no errors
    	return successfulArchive;
    }
    
    
    /**
     * Helper that reports a failed IO operation to stdout.
     * @param action  A verb that fits the phrase "unable to ______"
     * @param ex  The Exception object that was thrown
     */
    private void reportFailedFileOp(String action, Exception ex) {
    	if ( !DEBUG )
    		return;
    	System.out.printf(
    			"(ERROR) OrderManager: Unable to %s from %s (%s)\n",
    			action,
    			DB_FILEPATH,
    			ex.getClass().getName());
    }
    
    
    /**
     * Reads state from existing <code>.ser</code> file on disk.<br/>
     * <b>Warning:</b> Will merge into, and overwrite, conflicting {@link Order} 
     * elements inside <code>this.orders</code> (if any) based on the 
     * <code>Order[]</code> that was serialized to disk. <br/>
     * This will not delete any existing non-conflicting Order elements in 
     * <code>this.orders</code>.
     * @see #dumpToFile()
     */
	private void restoreFromFile() {
		try {
			// Acquire handle to file and open input object stream.
			FileInputStream fileInSteam = new FileInputStream(DB_FILEPATH);
			ObjectInputStream objInStream = new ObjectInputStream(fileInSteam);
			this.orders = (Order[])objInStream.readObject();
			
			int orderRestoredCount = 0;
			for ( Order ord : this.orders ) {
				if ( ord != null) {
					int tableId = ord.getTableId();
					this.tableMgr.setTableOccupied(tableId);
					orderRestoredCount++;
				}
			}
			
			objInStream.close();
			fileInSteam.close();
			
			if ( DEBUG )
				System.out.printf(
						"(DEBUG) OrderManager: %d open order%s restored from %s\n",
						orderRestoredCount,
						( orderRestoredCount == 1 ) ? "" : "s",
						DB_FILEPATH);

		} catch (IOException e) {
			this.reportFailedFileOp("restore", e);
		} catch (ClassNotFoundException e) {
			this.reportFailedFileOp("restore", e);
		}
	}
	
	
	/**
	 * Dumps state to disk.<br/>
	 * This will overwrite the existing <code>.ser</code> state file.
	 * @see #restoreFromFile()
	 */
	public void dumpToFile() {
		try {
			// Acquire handle to file and open input object stream.
			FileOutputStream fileOutSteam = new FileOutputStream(DB_FILEPATH);
			ObjectOutputStream objOutStream = new ObjectOutputStream(fileOutSteam);
			objOutStream.writeObject(this.orders);
			objOutStream.close();
			fileOutSteam.close();

			if ( DEBUG )
				System.out.println("(DEBUG) OrderManager: Dumped to " + DB_FILEPATH);

		} catch (IOException e) {
			this.reportFailedFileOp("restore", e);
		}
	}
}
