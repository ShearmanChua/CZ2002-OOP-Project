# Greetings fellow project mates.

```
// Food, menu components
class Food
    + static enum FoodTypes { MAIN_COURSE, DESSERT, DRINK; }
    - name : String
    - description : String
    - price : double
    - type : FoodType
    + getName : String
    + getDescription : String
    + getPrice : double
    + getType : FoodType
    + Food(String name, String desc, double price, FoodTypes type)

abstract class MenuItem
    //getters only
    + getName() : String
    + getDescription() : String
    + getFoodItems() : <Food>
    + getPrice() : double


class AlaCarteMenuItem extends MenuItem
    - food : Food
    + AlaCarteMenuItem( Food food ) : void  // constructor


class PromotionMenuItem extends MenuItem
    - foodItems : Food[]
    + Promotion( <Food> foodItems, double price )  // constructor
    + Promotion( <Food> foodItems )  // compute price by adding all food item price


class Menu
    - menuItems : <Food>
    + addItem( Food food )  // create new Food object here
    + removeItem( Food food )
    + getAllItems() : <Food>
    + Menu() : void  // constructor
    + dumpToFile() : void    // menu.ser
    + readFromFile() : void


class Table
    + enum Status { AVAILABLE, OCCUPIED, RESERVED }
    - status : Status
    - expiryTime : Date
    + setExpiryTime( Date expiryTime ) : void  // used by ReservationManager
    + reservationIsExpired() : boolean         // used by TableManager
            reservationIsExpired() {
                now = Date.now()
                if (this.expiryTime == null) {
                    return true;
                } else {
                    Date timepassed = now - this.expiryDate   // 40 min
                    if (timepassed > ReservationManager.EXPIRY_TIME & this.status == RESERVED)
                        this.status = AVAILABLE
                        return true;
                }
                return false;
            }

    + Table(int tableId, int capacity) : void  // constructor


class TableManager
    - tables : Table[] 
    - getTableById( int tableId ) : Table
    - getAllTables() : Table[]
    - getAvailTables() : Table[]
            ArrayList<Table> output = new ArrayList<Table>;
            for ( Table t : self.tables ) {
                if t.status == Table.Status.AVAILABLE
                    output.add(t)
                else if t.status==Table.Status.RESERVED
                    if(table.reservationIsExpired())
                    output.add(t);



    + setTableStatus( int tableId, Table.Status newStatus ) : void
    + findEmptyTableForCapacity( int capacity ) : int  // returns tableId or -1 if no vacant tables
    + allocateReservation( Reservation res ) : int     // returns tableId or -1 if no vacant tables
    + TableManager() : void    // constructor


class ReservationManager
    + EXPIRY_MINS : int
    - tableMan : TableMan
    - reservations : <Reservation>
    - checkForExpired() : void
    + startSession(Session session) : void
    + createReservation( int contactNumber, int numPax ) : int  // return tableId
            // straight away call tableMan.allocateReservation() 
            // if new reservation is within this session
    + getReservation( int contactNumber ) : int                 // return tableId
    + setReservationStatus( int contactNumber ) : void
    + ReservationManager ( TableManager tableManager ) : void   // constructor
    + dumpToFile() : void  // reservations.ser
    + readFromFile() : void


class Reservation
    - tableId : int
    - numPax : int
    - contactNumber : int
    + setTableId( int tableId ) : void
    + Reservation( int numPax, int contactNumber )  // constructor


class Order
    - menuItems : <MenuItem>
    - staffName : String
    - staffId : String
    - tableId : int
    + getMenuItems() : <MenuItem>
    + getStaffName() : String
    + getStaffId() : String
    + removeItem( MenuItem itemToRemove )
    + addItem( MenuItem itemToAdd )
    + Order( <MenuItem> menuItems, String staffName, String staffId, int tableId )  // constructor


class OrderManager
    - orders : Order[]  // paid Orders moved to ReportManager
    - getOrderByTableId( int tableId )
    + getAllOrders : <Order>
    + newOrder( <MenuItem> menuItems, String staffName, String staffId, int tableId ) : Order
    + addItemInOrder( int tableId, MenuItem menuitem ) : void
    + removeItemInOrder( int tableId, MenuItem menuitem ) : void
    + printInvoice( int tableId ) : void
    + OrderManager( Menu menu, TableManager tm ) : void  // constructor


class Report
    - orders : Order[]
    + toString() : String
    + Report( Order[] ) : void  // constructor
    + printToFile() : void  // report.txt   <---- TEXT FILE
    + dumpToFile() : void   // report.ser
    + readFromFile() : void


class ReportManager
    - paidOrders : <Order>
    + generateReportByPeriod( String period ) : Report  // period is "today" or "month"
        // switch(format)
        // case "YYYY-MM-DD" { // print report for that day }
        // case "YYYY-MM" { // print report for that month }
    //+ generateReportForRange( Date startDate, Date endDate )
    + ReportManager( OrderManager om ) : void


class App
    + main()
    + App() : void



// App
class Session
class RestaurantApp  (static defineSession)
```