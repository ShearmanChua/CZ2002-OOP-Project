import java.io.Serializable;
import java.util.*;

/**
 * Represents a customer Order.
 */
public class Order implements Serializable {
	/**
	 * Serial version ID used for checking when this object is de-serialized
	 */
	private static final long serialVersionUID = -7040097863887693817L;
	
	private ArrayList<MenuItem> menuItems;
	private int tableId;
	private String staffName;
	private int staffId;
	private Date timeCreated;


	/**
	 * Constructor. The <code>timeCreated</code> is automatically assigned
	 * based on the current time when this constructor is called at runtime.
	 * @param menuItems  ArrayList of {@link MenuItem}s to add to the order initially.
     * @param staffName  Name of the staff member creating the new Order.
     * @param staffId  Staff ID of the staff member creating the new Order. Alphanumeric.
	 * @param tableId  <code>tableId</code> of the {@link Table} for the new Order.
	 */
	public Order(ArrayList<MenuItem> menuItems, int tableId, String staffName, int staffId) {
		this.menuItems = (menuItems.size() == 0)?menuItems = new ArrayList<MenuItem>():menuItems;
		this.tableId = tableId;
		this.staffName = staffName;
		this.staffId = staffId;
		this.timeCreated = new Date();
	}


	// Getters for read-only properties.
	public ArrayList<MenuItem> getMenuItems() { return this.menuItems; }

	public String getStaffName() { return this.staffName; }
	
	public int getStaffId() { return this.staffId; }

	public Date getTimeCreated() { return this.timeCreated; }
	
	private void setTimeCreated(Date d) { this.timeCreated = d; }

	public int getTableId() { return tableId; }
	
	
//	/**
//	 * Creates a copy of this Order. The copy instance will have have the same
//	 * <code>timeCreated</code> value as this instance.
//	 * @return Order  A copy of this Order instance.
//	 */
//	public Order copy() {
//		Order ord = new Order(this.menuItems, this.tableId, this.staffName, this.staffId);
//		ord.setTimeCreated(this.getTimeCreated());
//		return ord;
//	}


	/**
	 * Remove {@link MenuItem} from the Order, using its name (<code>String</code>).
	 * @param itemName  The name of the MenuItem to remove.
	 */
	public void removeItem(String itemName) {
		ArrayList<MenuItem> menuItems = this.getMenuItems();
		
		for ( int i=0; i < menuItems.size(); i++ ) {
			MenuItem mi = menuItems.get(i);
			if ( mi.getName() == itemName ) {
				this.menuItems.remove(i);
				break;
			}
		}		
	}
	

	/**
	 * Remove {@link MenuItem} from the Order, using an instance of the item.
	 * @param item  The MenuItem to remove.
	 */
	public void removeItem(MenuItem item) {
		String itemName = item.getName();
		this.removeItem(itemName);
	}


	/**
	 * Add {@link MenuItem} to the Order
	 * @param item  The MenuItem to add.
	 */
	public void addItem(MenuItem item){
		this.menuItems.add(item);
	}
	
	
	public Map<MenuItem, Integer> getItemCount() {
		Map<MenuItem, Integer> itemCount = new HashMap<MenuItem, Integer>();
		for ( MenuItem mi : this.getMenuItems() ) {
    		int count = itemCount.getOrDefault(mi, 0);
    		itemCount.put(mi, ++count);
    	}
		return itemCount;
	}
	
	
	public Map<MenuItem, Double> getItemPrice() {
		Map<MenuItem, Double> itemPrice = new HashMap<MenuItem, Double>();
		for ( MenuItem mi : this.getMenuItems() ) {
    		if ( !itemPrice.containsKey(mi) )
    			itemPrice.put(mi, mi.getPrice());
    	}
		return itemPrice;
	}
	
	
	public Double getTotalNettPrice() {
		Map<MenuItem, Integer> itemCount = this.getItemCount();
		Map<MenuItem, Double> itemPrice = this.getItemPrice();
		
		double nettPrice = 0.0;
		for ( MenuItem mi : itemCount.keySet() ) { 
			int count = itemCount.getOrDefault(mi, 0);
			double price = itemPrice.getOrDefault(mi, 0.0);
			nettPrice += count * price;
		}
		
		return nettPrice;
	}
}