import helpers.TextUtil;
import helpers.userinput.UserInput;
import java.util.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class ReportManager {
	
	private static final boolean DEBUG = false;

	private ArrayList<Order> paidOrders;
	private static final String pathToReportDB = "DB/reportDB.ser";

    /**
     * Constructor of Report Manager
     * @see #loadReport()
     */
	public ReportManager() {

		this.loadReport();
	}

	/**
	 * Archives a copy of the given Order to the database
	 *
	 * @param order
	 * @returns boolean indicating whether archiving is successful
	 */
	public boolean addOrder(Order order) {
		boolean success;

		// Make copy of the Order and archive it
		try {
			Order ord = order;
			if(order.getMenuItems().size() > 0) {
				this.paidOrders.add(ord);
				success = this.dumpToFile();
			}
			else
				success = false;
			// TODO: Archive the received Order to database

		} catch (Exception e) {
			success = false;
		}

		// Returns true if no errors in archiving the Order.
		return success;
	}

    /**
     * Printing a report with the given Param
     * @param mapMi - A <code>Map<MenuItem, Integer></code> to generate a report
     */
	private void printReport(Map<MenuItem, Integer> mapMi){

		// Workaround for differing obj-id; cluster my menuitem names & prices
		Map<MenuItem, Integer> clustered = new HashMap<MenuItem, Integer>();
		for ( MenuItem mi : mapMi.keySet() ) {
			String miName = mi.getName();
			double miPrice = mi.getPrice();
			int miCount = mapMi.get(mi);
			boolean added = false;
			for ( MenuItem existing : clustered.keySet() ) {
				String existingName = existing.getName();
				Double existingPrice = existing.getPrice();
				int existingCount = clustered.getOrDefault(existing, 0);
				if ( existingName.equals(miName) && existingPrice.equals(miPrice) ) {
					clustered.put(existing, existingCount + miCount);
					added = true;
					break;
				}
			}
			if ( !added )
				clustered.put(mi, miCount);
		}
				
		mapMi = clustered;
		
		
		final int colWidth = 72;  // use multiples of 6 for best effect
		final int halfColWidth = Math.floorDiv(colWidth, 2);
		final int oneSixthColWidth = Math.floorDiv(halfColWidth, 3);

		double grandTotal = 0;

		String headerFmt = String.format(
				" _%s_\n" +
						"| %s |",
				TextUtil.repeatString("_", colWidth),
				TextUtil.alignCenter("REVENUE REPORT", colWidth, null));

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

		for (MenuItem mi : mapMi.keySet()) {

			int count = mapMi.getOrDefault(mi, 0);
			String qty = TextUtil.truncate(count, oneSixthColWidth);
			double unitPrice = mi.getPrice();

			grandTotal += (unitPrice * count);
			String price = TextUtil.truncate(String.format("%.2f", unitPrice), oneSixthColWidth);
			String itemTotal = TextUtil.truncate(String.format("%.2f", unitPrice * count), oneSixthColWidth);

			System.out.println("| " + TextUtil.alignLeft(mi.getName(), colWidth, null) + " |");
			System.out.println(String.format(priceRowFmt, "", qty, price, itemTotal));
		}

		// Create formatString for summary rows
		String summaryFmt = (
				"| " +
						String.format("%%%ds:", colWidth - oneSixthColWidth - 3) + " " +
						String.format("%%%ds", oneSixthColWidth) + " " +
//				TextUtil.repeatString(" ", oneSixthColWidth) +
						" |");


		// Print "blank line"
		System.out.println("| " + TextUtil.repeatString(" ", colWidth) + " |");
		String grandTotalStr = String.format("S$ %.2f", grandTotal);
		System.out.println(String.format(summaryFmt, "Total Revenue", grandTotalStr));

		// Print footer
		System.out.printf("|_%s_|\n", TextUtil.repeatString("_", colWidth));


	}

    /**
     * Attempts to generate a report with the given MenuItem
     * A Hashmap <code>mapMi</code> would store all the required data and be passed to
     * printReport
     * @param item
     * @see #printReport(Map)
     */
	public void generateReport(MenuItem item) {

		Map<MenuItem, Integer> mapMi = new HashMap<MenuItem, Integer>();
		int count;

		for(Order ord : this.paidOrders) {
			Map<MenuItem, Integer> itemCount = ord.getItemCount();

			for (MenuItem mi : itemCount.keySet()) {

				if (mi.getName().equals(item.getName()) && mapMi.containsKey(mi) == false) {
					count = itemCount.getOrDefault(mi, 0);
					mapMi.put(mi, count);
				}
				else if(mi.getName().equals(item.getName()) && mapMi.containsKey(mi) == true){
					count = itemCount.getOrDefault(mi, 0);
					mapMi.put(mi, count + mapMi.getOrDefault(mi, 0));
				}
			}

		}

		if(mapMi.isEmpty())
			System.out.println("There are no orders for this item");
		else
			this.printReport(mapMi);


	}

    /**
     * Attempts to generate a report with the given choice
     * A Hashmap <code>mapMi</code> would store all the required data and be passed to
     * printReport
     * @param choice - 1 a date, 2 for a period
     * @see #printReport(Map)
     */
	public void generateReport(int choice) {
		//1 - print all, 2 - period, 3 - individual sales item

		Map<MenuItem, Integer> mapMi = new HashMap<MenuItem, Integer>();
		int count;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		switch(choice){
			case 1:
				Date dateNeeded = UserInput.getDate();

				for(Order ord : this.paidOrders) {

					if(sdf.format(dateNeeded).equals(sdf.format(ord.getTimeCreated()))) {
						Map<MenuItem, Integer> itemCount = ord.getItemCount();

						for (MenuItem mi : itemCount.keySet()) {
							if (mapMi.containsKey(mi) == false) {
								count = itemCount.getOrDefault(mi, 0);
								mapMi.put(mi, count);
							}
							else {
								count = itemCount.getOrDefault(mi, 0);
								mapMi.put(mi, count + mapMi.getOrDefault(mi, 0));
							}
						}
					}
				}

				if(mapMi.isEmpty())
					System.out.println("There are no orders on this date");
				else
					this.printReport(mapMi);

				break;
			case 2:
				Date initialDate = UserInput.getDate("Please enter initial date with 'today/tomorrow/tmr/ytd/yesterday' or a custom date (dd/mm or dd/mm/yyyy): ", null);
				Date finalDate = UserInput.getDate("Please enter final date with 'today/tomorrow/tmr/ytd/yesterday' or a custom date (dd/mm or dd/mm/yyyy): ", null);

				for(Order ord : this.paidOrders) {

					if(sdf.format(initialDate).compareTo(sdf.format(ord.getTimeCreated())) <= 0
							&& sdf.format(finalDate).compareTo(sdf.format(ord.getTimeCreated())) >= 0) {

						Map<MenuItem, Integer> itemCount = ord.getItemCount();

						for (MenuItem mi : itemCount.keySet()) {
							if (mapMi.containsKey(mi) == false) {
								count = itemCount.getOrDefault(mi, 0);
								mapMi.put(mi, count);
							}
							else {
								count = itemCount.getOrDefault(mi, 0);
								mapMi.put(mi, count + mapMi.getOrDefault(mi, 0));
							}
						}
					}
				}

				if(mapMi.isEmpty())
					System.out.println("There are no orders on this period");
				else
					this.printReport(mapMi);
				break;

		}


	}

    /**
     * Attempts to load report from <code>.ser</code> file
     * using static method from Menu Class Menu.readFromFile(String)
     */
	private void loadReport() {
		this.paidOrders = Menu.readFromFile(pathToReportDB);

		if (this.paidOrders == null) {
			System.out.println("Empty data set, load default Menu\n");
			this.paidOrders = new ArrayList<Order>();
		}
	}

    /**
     * Attempts to write report into <code>.ser</code> file
     * using static method from Menu Class Menu.readFromFile(String)
     */
	public boolean dumpToFile() {

		boolean success = Menu.writeToFile( pathToReportDB, this.paidOrders);
		if (DEBUG) {
			if(success)
				System.out.println("(DEBUG) ReportManager: Dumped to " + pathToReportDB);
			else
				System.out.println("(DEBUG) ReportManager: failed to dump into " + pathToReportDB);
		}
		

		return success;


	}
}
