import java.util.ArrayList;

import helpers.TextUtil;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;

public class Menu {

	private ArrayList<MenuItem> menuItems;
	public static final String pathToMenuDb = "DB/menuDB.ser";
	private static DecimalFormat df2 = new DecimalFormat(".##");

	/**
	 * constructor for Menu
	 * @see #loadMenu()
	 */
	public Menu() {
		this.loadMenu();
	}

	/**
	 * Attempts to read retrieve data from .ser file using #readFromFile(String)
	 * if no data is received, an arraylist of menuItems would be generated for <code>this.MenuItems</code>
	 * @see #readFromFile(String) ()
	 */
	private void loadMenu(){


		this.menuItems = this.readFromFile(pathToMenuDb);

		if(this.menuItems == null ){
			System.out.println("Empty data set, load default Menu\n");

			this.menuItems = new ArrayList<MenuItem>();

			//empty menu, declare default menu
			ArrayList<Food>food = new ArrayList<Food>();
			food.add(new Food("Chicken Burger", 2, "Freshly fried chicken", Food.FOODTYPES.MAIN_COURSE));
			food.add(new Food("Cheese Burger", 2, "The usual", Food.FOODTYPES.MAIN_COURSE));
			food.add(new Food("Double Cheese Burger", 3.50, "Double the trouble, double the satisfaction", Food.FOODTYPES.MAIN_COURSE));
			food.add(new Food("Fish Burger", 2.50, "Deep fried fish", Food.FOODTYPES.MAIN_COURSE));
			food.add(new Food("Grilled Burger", 3, "Freshly grilled chicken/beef", Food.FOODTYPES.MAIN_COURSE));

			food.add(new Food("Coke", 1.70, "Just Coke", Food.FOODTYPES.DRINK));
			food.add(new Food("Pepsi", 1.50, "Hate Coke, Love Pepsi", Food.FOODTYPES.DRINK));
			food.add(new Food("Mountain Dew", 1.80, "Love it", Food.FOODTYPES.DRINK));
			food.add(new Food("Water", 1.00, "H2O", Food.FOODTYPES.DRINK));

			food.add(new Food("Chocolate Cake", 2.50, "Mouth watering chocolate", Food.FOODTYPES.DESSERT));
			food.add(new Food("Durian Cake", 2.50, "King of all fruits", Food.FOODTYPES.DESSERT));
			food.add(new Food("Cheese Cake", 2.50, "Just Cheese", Food.FOODTYPES.DESSERT));

			for(int i=0; i<food.size(); i++)
				this.menuItems.add(new AlaCarteMenuItem(food.get(i)));



			ArrayList<Food>set1 = new ArrayList<Food>();
			set1.add(food.get(0));
			set1.add(food.get(6));
			set1.add(food.get(10));

			ArrayList<Food>set2 = new ArrayList<Food>();
			set2.add(food.get(1));
			set2.add(food.get(2));
			set2.add(food.get(5));
			set2.add(food.get(6));
			set2.add(food.get(10));
			set2.add(food.get(11));

			this.menuItems.add(new PromotionMenuItem(set1, "Chicken Burger set", "Easy Combo"));
			this.menuItems.add(new PromotionMenuItem(set2,12.0, "Cheese buddy set", "Easy Combo"));

			this.writeToFile(this.pathToMenuDb, this.menuItems);

		}

	}


	/**
	 * Attempts to add item to menuItems
	 * @param item - MenuItem to be added onto <code>this.menuItems</code>
	 * @param choice - 1, ala_carte; 2, promotionalItem to sort them in the menu
	 */
	public void addItems(int choice, MenuItem item) {

		if(choice == 1) {
			this.menuItems.add(0, item);
		}
		else {
			this.menuItems.add(item);
		}
	}

	/**
	 * Attempts to remove item from <code>this.menuItems</code>
	 * @param item - MenuItem to be removed from <code>this.menuItems</code>
	 */
	public void removeItem(MenuItem item) {
		for(int i=0;i<this.menuItems.size();i++){
			if(item == this.menuItems.get(i))
				this.menuItems.remove(i);
		}
	}

	/**
	 * static method
	 * Reads state from existing <code>.ser</code> file on disk.<br/>
	 * Will return an ArrayList
	 * @param  filePath - path to .ser file.
	 * @return ArrayList - arraylist of data else return null
	 */
	public static ArrayList readFromFile(String filePath) {
		try {

			ArrayList<Object> data;
			FileInputStream file = new FileInputStream(filePath);
			ObjectInputStream in = new ObjectInputStream(file);

			data = (ArrayList) in.readObject();

			in.close();
			file.close();

			return data;


		} catch (IOException e) {
			System.out.println("EMPTY TEXT FILE FROM " + filePath);

		} catch (ClassNotFoundException e) {
			System.out.println("CLASSNOTFOUNDEXCEPTION: " + e);
			System.exit(0);
		}

		return null;
	}
	/**
	 * static method
	 * Writes state from existing <code>.ser</code> file on disk.<br/>
	 * <b>Warning</b> will overwrite any data in <code>.ser</code> on given filepath
	 * @param  pathtofile - path to .ser file.
	 * @param  data - arraylist of data to be stored
	 * @return boolean - success of overwriting data
	 */
	public static boolean writeToFile(String pathtofile, ArrayList data){

		try {

			FileOutputStream foStream = new FileOutputStream(pathtofile);
			ObjectOutputStream doStream = new ObjectOutputStream(foStream);

			doStream.writeObject(data);
			doStream.flush();

			doStream.close();
			foStream.close();
		}
		catch( FileNotFoundException e ) {
			System.out.println( "IOError: File not found!" + data );
			return false;
		}
		catch( IOException e ) {
			System.out.println( "IO Error!" + e.getMessage() );
			return false;
		}
		return true;

	}

	/**
	 * get method to get <code>this.menuItems</code>
	 * @return menuItems arraylist
	 */
	public ArrayList<MenuItem> getMenuItems() {
		return menuItems;
	}

	/**
	 * get method for specific items with index param
	 * @param index
	 * @return menuItems
	 * @see #getMenuItems()
	 */
	public MenuItem getMenuItems(int index) {
		return menuItems.get(index);
	}

	/**
	 * Attempts to print the entire Menu
	 * @param choice; 0 - print the entire menu, 1-print only Ala_carte, 2-print only promotionItems
	 */
	public void printMenu(int choice){

		System.out.println("Here is your menu:");

		int i=0;
		for (MenuItem item : this.menuItems){

			if(item instanceof AlaCarteMenuItem && (choice == 1 || choice == 0)) {
				System.out.printf("%3d. %-30s %s\n   $%.2f | \"%s\"\n\n",
						i+1, item.getName(), "(Ala Carte - "+item.getType()+")", item.getPrice(), item.getDescription());
			}
			else if(item instanceof PromotionMenuItem && (choice == 2 || choice == 0)){
				System.out.printf("%3d. %-30s %s\n",
						i+1, item.getName(), "(Promotion Set)", item.getPrice(), item.getDescription());
				for (Food food : item.getArrayFood())
					System.out.printf("     %12s: %s\n",
							food.getType(), food.getName());
				System.out.printf("   $%.2f | \"%s\"\n\n",
						item.getPrice(), item.getDescription());
			}

			i++;
		}
	}

	/**
	 * Attempts to print given MenuItem
	 * @param item;
	 */
	public void printMenu(MenuItem item){

		if(item instanceof AlaCarteMenuItem) {
			System.out.printf("  %-30s %s\n  $%.2f | \"%s\"\n\n",
					item.getName(), "(Ala Carte - "+item.getType()+")", item.getPrice(), item.getDescription());
		}
		else if(item instanceof PromotionMenuItem){
			System.out.printf("  %-30s %s\n",
					item.getName(), "(Promotion Set)", item.getPrice(), item.getDescription());
			for (Food food : item.getArrayFood())
				System.out.printf("     %12s: %s\n",
						food.getType(), food.getName());
			System.out.printf("  $%.2f | \"%s\"\n\n",
					item.getPrice(), item.getDescription());
		}
	}


	/**
	 * get method for Arraysize of <code>this.menuItems</code> to specific requests
	 * @param choice - 0 - return this.menuItems; 1 - only ala_carte size; 2 - only promotionalMenuItem size;
	 * @return size of requested item menuItems/promotionalMenuItem/alacarteMenuItem
	 */
	public int getMenuArraySize(int choice){

		if(choice == 0)
			return this.menuItems.size();
		else{

			int i=0;
			for (MenuItem item : this.menuItems) {
				if(item instanceof AlaCarteMenuItem && choice == 1) {
					i++;
				}
				else if(item instanceof PromotionMenuItem && choice == 2)
					i++;
			}
			return i;

		}

	}




}