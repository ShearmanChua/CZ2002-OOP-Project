import helpers.userinput.UserInput;
import java.util.ArrayList;
import java.util.Objects;


public class RestaurantApp {

	public static void main(String[] args) {

		//declare all the default constructors
		Menu menu = new Menu();
		ReportManager reportManager = new ReportManager();
		TableManager tableManager = new TableManager();
		StaffManager staffManager = new StaffManager();
		OrderManager orderManger = new OrderManager(reportManager, tableManager);
		ReservationManager reservationManager = new ReservationManager(tableManager);
		System.out.println();
		System.out.println("Starting Restaurant App RRPSS");
		
		//default variable
		int choice = 0;

		do {
			System.out.println(
					"\n[Main menu]\n" +
					"Which do you wish to access?\n" +
					"1. Menu Manager\n" +
					"2. Table Manager\n" +
					"3. Order Manager\n" +
					"4. Reservation Manager\n" +
					"5. Report Manager\n" +
					"6. Staff Manager\n\n" +
					"Enter 0 to cancel");

			choice = UserInput.getIntFromRange(0, 6);
			System.out.println();

			switch (choice){
				case 1:
					menuManager(menu);
					break;
				case 2:
					managingTables(tableManager);
					break;
				case 3:
					managingOrders(orderManger, menu, tableManager, staffManager,reservationManager);
					break;
				case 4:
					reservationManager.ReservationInterface();
					break;
				case 5:
					managingReport(reportManager, menu);
					break;
				case 6:
					staffManager(staffManager);
					break;
			}
		}while(choice != 0);

	}

	public static void menuManager(Menu menu){

		//default variables
		int choice = 0;
		String name;
		String desc;
		double price;
		int type;
		Food.FOODTYPES foodtypes;

		System.out.println("What do you wish to do?\n" +
				"1. Print Menu\n" +
				"2. Add Ala Carte Item\n" +
				"3. Remove Item from Menu\n" +
				"4. Add Promotional Item\n" +
				"5. Update Item\n\n" +
				"ENTER 0 TO QUIT\n");

		choice = UserInput.getIntFromRange(0,5);

		switch (choice) {
			case 1:
				menu.printMenu(0);
				break;
			case 2:
				name = UserInput.getString(100, "What is the name of your food? ", "Please enter a string");
				price = UserInput.getDouble(1.00, 50.00,"What is the price of "+ name + "? ", "Please enter a proper price value");
				desc = UserInput.getString(200, "What is the description of "+ name + "? ", "Please enter a proper description!");
				type = UserInput.getInt(new int[]{1,2,3},"What is the type for "+name+"?\n1-MAIN COURSE, 2-DRINK, 3-DESSERT\n", "Please enter within the range");
				foodtypes = (type==1)? Food.FOODTYPES.MAIN_COURSE:
						(type == 2)? Food.FOODTYPES.DRINK:
								Food.FOODTYPES.DESSERT;

				MenuItem item = new AlaCarteMenuItem(new Food(name, price, desc, foodtypes));
				menu.addItems(1, item);

				System.out.println("\nAdded Ala carte item");
				menu.printMenu(item);
				Menu.writeToFile(Menu.pathToMenuDb, menu.getMenuItems());

				break;
			case 3:
				menu.printMenu(0);
				System.out.println("\nWhat item do you want to remove?\nENTER 0 TO QUIT");
				int removeId = UserInput.getIntFromRange(0,menu.getMenuArraySize(0));
				if(removeId != 0) {
					MenuItem removeItem = menu.getMenuItems().get(removeId - 1);
					menu.removeItem(removeItem);
					System.out.println("\nRemoved Menu item");
					menu.printMenu(removeItem);
					Menu.writeToFile(Menu.pathToMenuDb, menu.getMenuItems());
				}
				break;
			case 4:
				menu.printMenu(1);

				ArrayList<Food> promo = new ArrayList<Food>();

				int toBeIncluded = 1;
				Food food;

				while(toBeIncluded != 0 && toBeIncluded != -1){

					System.out.println("Which Menu Item do you want to include?\n0 TO CONFIRM, -1 TO QUIT\n");
					toBeIncluded = UserInput.getIntFromRange(-1,menu.getMenuArraySize(1)); //FIXME: return correct array size

					if(toBeIncluded != 0 && toBeIncluded != -1){
						food = menu.getMenuItems(toBeIncluded-1).getFood();
						promo.add(food);
						System.out.printf("\n%s has been added\n\n", food.getName());
					}
				}

				if(toBeIncluded == 0){
					name = UserInput.getString(100, "What is the name of your promotion? ", "Please enter a string");
					price = UserInput.getDouble(0.00, 50.00,"What is the price of the promotion? Enter 0 to use total price ", "Please enter a proper price value");
					desc = UserInput.getString(200, "What is the description of the promotion? ", "Please enter a proper description!");

					if(price == 0)
						menu.addItems(2, new PromotionMenuItem(promo, name, desc));
					else
						menu.addItems(2, new PromotionMenuItem(promo, price, name, desc));


					System.out.println("\nAdded promotion item");
					menu.printMenu(menu.getMenuItems(menu.getMenuArraySize(0)-1));
					Menu.writeToFile(Menu.pathToMenuDb, menu.getMenuItems());
				}

				break;
			case 5:
				menu.printMenu(0);

				int tobeUpdated = 1;
				int update_choice = 0;
				System.out.println("Which Menu Item do you want to update?\n0 to quit\n");
				tobeUpdated = UserInput.getIntFromRange(0,menu.getMenuArraySize(0));

				if(tobeUpdated != 0){
					MenuItem menuitem = menu.getMenuItems(tobeUpdated-1);
					if(menuitem instanceof AlaCarteMenuItem){
						System.out.println("What do you wish to update?\n" +
								"1. Product name\n" +
								"2. Description\n" +
								"3. Price\n" +
								"4. Food type\n" +
								"Enter 0 to cancel\n\n");

						update_choice = UserInput.getIntFromRange(0,4);

						switch (update_choice){
							case 1:
								name = UserInput.getString(100, "What is the updated item name? ", "Please enter a string");
								menuitem.setName(name);
								break;
							case 2:
								desc = UserInput.getString(200, "What is the new description? ", "Please enter a proper description!");
								menuitem.setDescription(desc);
								break;
							case 3:
								price = UserInput.getDouble(1.00, 50.00,"What is the new price? ", "Please enter a proper price value");
								menuitem.setPrice(price);
								break;
							case 4:
								type = UserInput.getInt(new int[]{1,2,3},"What is the new food type? "+"?\n1-MAIN COURSE, 2-DRINK, 3-DESSERT\n ", "Please enter within the range");
								foodtypes = (type==1)? Food.FOODTYPES.MAIN_COURSE:
										(type == 2)? Food.FOODTYPES.DRINK:
												Food.FOODTYPES.DESSERT;
								menuitem.setType(foodtypes);
								break;
						}

						if(update_choice != 0) {
							System.out.println("\nUpdated ala carte item");
							menu.printMenu(menuitem);
							Menu.writeToFile(Menu.pathToMenuDb, menu.getMenuItems());
						}
					}
					else{
						//promotional item
						System.out.println("What do you wish to update?\n" +
								"1. Promotion name\n" +
								"2. Description\n" +
								"3. Price\n" +
								"Enter 0 to cancel\n\n");

						update_choice = UserInput.getIntFromRange(0,3);

						switch (update_choice) {
							case 1:
								name = UserInput.getString(100, "What is the updated item name? ", "Please enter a string");
								menuitem.setName(name);
								break;
							case 2:
								desc = UserInput.getString(200, "What is the new description? ", "Please enter a proper description!");
								menuitem.setDescription(desc);
								break;
							case 3:
								price = UserInput.getDouble(1.00, 50.00, "What is the new price? ", "Please enter a proper price value");
								menuitem.setPrice(price);
								break;
						}

						if(update_choice != 0) {
							System.out.println("\nUpdated promotion item");
							menu.printMenu(menuitem);
							Menu.writeToFile(Menu.pathToMenuDb, menu.getMenuItems());
						}

					}

				}
				break;


		}
	}


	public static void managingOrders(
			OrderManager orderManager, 
			Menu menu, 
			TableManager tableManager,
			StaffManager staffManager,
			ReservationManager reservationManager) {

		int choice = 0;
		int tableId = 0;
		boolean backToMain = false;

		System.out.println(
				"[Order management]\n" +
				"1. Create order for table\n" +
				"2. Add items to order\n" +
				"3. Remove items from order\n" +
				"4. Print current invoice for table\n" +
				"5. Close order\n" +
				"Enter 0 to return to main menu");

		choice = UserInput.getIntFromRange(0, 5);
		System.out.println();
		
		// Prepare to extract info from the order at the target table
		Order thisOrder = null;
		ArrayList<MenuItem> thisOrderItems = null;
		
		// Selectively setup refs for thisOrder and thisOrderItems based on choice
		switch (choice) {
			// Do nothing, back to main menu
			case 0:
				System.out.println("Returning to main menu.\n");
				return;
				
			// If creating, no need to setup thisOrder and thisOrderItems	
			case 1:
				break;
			
			// Ask for table and extract info for these cases
			case 2:
			case 3:
			case 4:
			case 5:
				tableId = UserInput.getIntFromRange(0, TableManager.NUM_TABLES, "Enter table ID or 0 to cancel: ", null);
				if ( tableId == 0 )
					return;
				boolean thisTableOccupied = tableManager.isTableOccupied(tableId);
				// choice 2, 3, 4, 5 all need existing order at the table
				if ( thisTableOccupied ) {
					thisOrder = orderManager.getOrderByTableId(tableId);
					thisOrderItems = thisOrder.getMenuItems();
				} else {
					System.out.printf("Table %d has no order available. You have to create an order first.%n", tableId);
					choice = -99;  // Don't do anything in the second switch-case
				}
				break;
			
			default:
				System.out.println("Invalid option, returning to main menu.\n");
				return;
		}
		
		// Second switch-case: Business logic
		switch (choice) {
			// Special case to do nothing and just go back to order manager menu
			case -99:
				break;

			// Create order 
			case 1:
				System.out.println("Creating order...");
				
				// Find empty table
				int numOfPax = UserInput.getIntFromRange(
						0, 10, 
						"  Enter number of pax (or 0 to cancel): ",
						"We only have tables for 1 to 10 people.");
				if ( numOfPax == 0 )
					break;
				int reservedTableId = UserInput.getIntFromRange(
						0, TableManager.NUM_TABLES, 
						"  Enter tableId if customer has reservation, or 0 if no reservation: ",
						"tableId must be between 1 and " + TableManager.NUM_TABLES);
				if ( reservedTableId == 0 ) {
					tableId = tableManager.findEmptyTableForCapacity(numOfPax);
				} else {
					if ( tableManager.isTableReserved(reservedTableId) ) {
						tableId = reservedTableId;
						reservationManager.unsetReservation(reservedTableId);
						reservationManager.writetoFile(reservationManager.getReservation());
					} else {
						System.out.println("No reservation found for table " + reservedTableId + ", aborting.");
						break;
					}
						
				}
				
				// Have empty table, create order here
				if ( tableId != -1 ) {
					System.out.printf("Found table %d.%n", tableId);
					Staff currStaff = staffManager.getCurrentStaff();
					String staffName = currStaff.getName();
					int staffId = currStaff.getID();
					
					orderManager.newOrder(new ArrayList<MenuItem>(), staffName, staffId, tableId);
					tableManager.setTableOccupied(tableId);
					System.out.printf(
							"Order for table %d (%d pax) created successfully. You can now add items to this order.",
							tableId, numOfPax);
				
				// No empty table, do nothing
				} else {
					System.out.println("There are no available tables. You must close some orders first.");
				}
				break;

			// Add items to order
			case 2:
				int addChoice = 0;
				int numItemsAdded = 0;
				MenuItem addItem = null;

				do {
					menu.printMenu(0);
					if ( addChoice != 0 ) {
						System.out.println("Item added to order:");
						menu.printMenu(addItem);
					}
					System.out.printf("Currently %d item%s in order.%n", 
							thisOrderItems.size(), ( thisOrderItems.size() == 1 ) ? "" : "s");
					addChoice = UserInput.getIntFromRange(
							0, menu.getMenuArraySize(0), 
							"What item do you want add to the order for table " + tableId + "?\n(Enter 0 to finish adding)\n", null);
					if ( addChoice != 0 ) {
						addItem = menu.getMenuItems(addChoice - 1);
						orderManager.addItemInOrder(tableId, addItem);
						numItemsAdded++;
					}
				} while ( addChoice != 0 ); 
				System.out.printf("%d item%s added to order for table %d.%n",
						numItemsAdded,
						( numItemsAdded == 1 ) ? "" : "s",
						tableId);
				backToMain = true;
				break;
			
			case 3:
				if ( thisOrderItems.size() == 0 ) {
					System.out.printf("The order for table %d has no items to remove.%n", tableId);
					break;
				}

				int removeChoice = 0;
				int numItemsRemoved = 0;
				MenuItem remItem = null;

				do {
					int numItems = thisOrderItems.size();
					if ( numItems == 0 )
						break;
					for ( int i=0; i<numItems; i++ ) {
						System.out.printf("%d: ", i + 1);    // no newline here!
						menu.printMenu(thisOrderItems.get(i));
					}
					System.out.printf("Currently %d item%s in order.%n", 
							numItems, numItems == 1 ? "" : "s");
					removeChoice = UserInput.getIntFromRange(
							0,
							numItems, 
							"Which item do you want remove from the order for table " + tableId + "?\n" +
									"(Enter 1 to " + numItems + ", or 0 to finish)\n", 
							null);
					if ( removeChoice != 0 ) {
						remItem = thisOrderItems.get(removeChoice - 1);
						orderManager.removeItemInOrder(tableId, remItem);
						numItemsRemoved++;
						System.out.printf("Item removed from order: %d. %s\n", removeChoice, remItem.getName());
					}
				} while ( removeChoice != 0 ); 
				System.out.printf("%d item%s removed from order for table %d.%n",
						numItemsRemoved,
						( numItemsRemoved == 1 ) ? "" : "s",
						tableId);
				backToMain = true;
				break;
			
			case 4:
				orderManager.printInvoice(tableId);
				break;
				
			case 5:
				String cfmPrompt = String.format("Close order for table %d? Type YES to proceed. ",  tableId);
				String cfm = UserInput.getString(3, cfmPrompt, "Please enter only YES or NO");  // length 3
				if ( !Objects.equals(cfm.toLowerCase(), "yes") ) {
					// StackOverflow recommends this crazy way of comparing strings
					// because Java is fucked up
					//   https://stackoverflow.com/a/513839
					System.out.printf("The order for table %d was not closed.%n", tableId);
					break;
				}

				boolean closed = orderManager.closeOrder(tableId);
				if ( !closed ){
					System.out.printf("Failed to close order for table %d due to system error.%n", tableId);
				}
//				if ( closed ) {
//					orderManager.printInvoice(tableId);
//					System.out.printf("The order for table %d was successfully closed.%n", tableId);
//				} else {
//					System.out.printf("Failed to close order for table %d due to system error.%n", tableId);
//				}
				backToMain = true;
				break;
			
			default:
				break;
		} // end switch
		
		// tail recursion to loop until done
		System.out.println();
		if ( !backToMain ) {
			System.out.println("Returning to order management menu.\n");
			managingOrders(orderManager, menu, tableManager, staffManager,reservationManager);
		} else {
			System.out.println("Returning to main menu.\n");
		}
	}

	
	public static void managingTables(TableManager tableManager) {

		int choice = 0;
		boolean backToMain = false;

		System.out.println(
				"[Table management]\n" +
				"Which do you wish to do?\n" +
				"1. Find available table for group\n" +
				"2. List occupied tables only\n" +
				"3. List reserved tables only\n" +
				"4. List available tables only\n" +
				"5. List all tables\n" +
				"Enter 0 to return to main menu");

		choice = UserInput.getIntFromRange(0, 5);
		System.out.println();

		int printedOccupied = 0;
		int printedReserved = 0;
		int printedAvailable = 0;
		switch (choice) {
			// Return to main menu
			case 0:
				System.out.println("Returning to main menu.\n");
				return;
			
			// Find empty table
			case 1:
				int tableId = -1;
				int numOfPax = UserInput.getIntFromRange(
						0, 10, 
						"  Enter number of pax (or 0 to cancel): ",
						"We only have tables for 1 to 10 people.");
				if ( numOfPax == 0 )
					break;
				tableId = tableManager.findEmptyTableForCapacity(numOfPax);
				if ( tableId == -1 ) {
					System.out.println("No tables available. ");
					break;
				}
				System.out.printf("Table %d is available.%n", tableId);
				// This function is for checking only, will not change table status.
				// To set status to occupied, see managingOrders().
				//tableManager.setTableStatus(tableId, Table.Status.OCCUPIED);
				backToMain = true;
				break;
			
			// List occupied tables
			case 5:
			case 2:
				for ( Table t : tableManager.getOccupiedTables() ) {
					System.out.println(t.toString());
					printedOccupied++;
				}
				System.out.printf("%d %s table%s in total.\n\n", 
						printedOccupied,
						"occupied",
						( printedOccupied == 1 ) ? "" : "s");
				if ( choice != 5 )
					break;
			
			// List reserved tables
			case 3:
				for ( Table t : tableManager.getReservedTables() ){
					System.out.println(t.toString());
					printedReserved++;
				}
				System.out.printf("\n%d %s table%s in total.\n", 
						printedReserved,
						"reserved",
						( printedReserved == 1 ) ? "" : "s");
				if ( choice != 5 )
					break;
			
			// List available tables
			case 4:
				for ( Table t : tableManager.getAvailableTables() ){
					System.out.println(t.toString());
					printedAvailable++;
				}
				System.out.printf("\n%d %s table%s in total.\n", 
						printedAvailable,
						"available",
						( printedAvailable == 1 ) ? "" : "s");
				break;
			
			// case 5 starts from 2 and falls-through until case 4
		}
		
		// tail recursion to loop until done
		System.out.println();
		if ( !backToMain ) {
			System.out.println("Returning to table management menu.\n");
			managingTables(tableManager);
		} else {
			System.out.println("Returning to main menu.\n");
		}
	}

	public static void staffManager(StaffManager staffManager){

		//default variables
		int choice = 0;
		String staffName;
		char staffGender = '\0';
		int staffId;
		String jobTitle;

		System.out.println("What do you wish to do?\n" +
				"1. Login as different staff\n" +
				"2. Add Staff\n" +
				"3. Remove Staff\n" +
				"4. Print Staff Details\n" +
				"5. Print All Staff Details\n" +
				"6. Update Staff Details\n\n" +
				"ENTER 0 TO QUIT\n");

		choice = UserInput.getIntFromRange(0,5);

		switch (choice) {
			case 1:
				staffId = UserInput.getInt(null, "Please enter staffId: ", null);
				staffManager.loginToStaff(staffId);
				break;
		
			case 2:
				staffName = UserInput.getString(100, "Please enter name: ", null);
				
				do {
					String input = UserInput.getString(1, "Please enter M / F for gender: ", "Invalid gender given!");
					if (input.equals("\n") || input.equals("")) {
						continue;
					}
					staffGender = input.charAt(0);
				} while (!staffManager.checkGender(staffGender));
				
				jobTitle = UserInput.getString(100, "Please enter the staff's designation (manager,cashier,part-time,full-time): ",null);
				
				staffManager.addStaff(staffName, staffGender, jobTitle);
				break;
				
			case 3:
				staffId = UserInput.getIntFromRange(1, 10, "Please enter staff's ID: ", null);
				staffManager.removeStaff(staffId);
				break;
				
			case 4:
				staffId = UserInput.getIntFromRange(1, 10, "Please enter staff's ID: ", null);
				staffManager.printStaffDetails(staffId);
				break;
				
			case 5:
				staffManager.printAllStaffDetails();
				break;
				
			case 6:
				staffId = UserInput.getIntFromRange(1, 10, "Please enter staff's ID: ", null);
				staffManager.updateStaff(staffId);
				break;
		}
	}


	public static void managingReport(ReportManager reportManager, Menu menu){

		int choice = 0;

		System.out.println(
				"[Report management]\n" +
						"Which do you wish to do?\n" +
						"1. Generate Revenue Report For a Day\n" +
						"2. Generate Revenue Report For a period\n" +
						"3. Generate Revenue Report For a Menu Item\n" +
						"Enter 0 to return to main menu");

		choice = UserInput.getIntFromRange(0, 3);
		System.out.println();

		switch (choice) {
			case 1:
				reportManager.generateReport(1);
				break;
			case 2:
				reportManager.generateReport(2);
				break;
			case 3:
				menu.printMenu(0);
				int itemId = UserInput.getIntFromRange(0,menu.getMenuArraySize(0));
				if(itemId !=0) {
					MenuItem item = menu.getMenuItems(itemId - 1);
					reportManager.generateReport(item);
				}
				break;

		}

	}
}

