# CE/CZ2002: Object-Oriented Design & Programming - Building an OO Application

## THE ASSIGNMENT

The assignment for your group will be to design and develop a :
Restaurant Reservation and Point of Sale System (RRPSS). RRPSS is an application to computerize the processes of making reservation, recording of orders and displaying of sale records. It will be solely used by the restaurant staff.

The restaurant operates in 2 sessions (AM : 11am – 3pm, PM : 6pm – 10pm).

The following are information about the application:
a) Menu items should be categorized according to its type, eg, Main course, drinks,
dessert, etc.
b) Menu items can be added with details like name, description, price, etc.
c) Promotional set package comes in a single package price with descriptions of the
items to be served.
d) A customer may order a set package or ala carte menu items.
e) An order should indicate the staff who created the order.
f) Staff information can be in the form of name, gender, employee ID and job title.
g) Reservation is made by providing details like date, arrival time, #pax, name, contact
number, etc and the table status is ‘reserved’. The system should check availability
and allocate a suitable table.
h) Contact number is used to identify reservation.
i) When a reservation is made, the table is reserved till the reservation booking is
removed (eg time expired). Once a table is reserved, it cannot be booked for that
particular session (AM/PM).
j) Once an order is entered, the table status is ‘occupied’*.
k) Once an order invoice is printed, it is assumed that payment has been made and the
table statue is ‘vacated’*.
l) Table comes in different seating capacity, in even sizes, with minimum of 2 and
maximum of 10 pax ("Persons At Table").
m) Order invoice can be printed to list the order details (eg, table number, timestamp)
and a complete breakdown of order items details with taxes details.
n) Sale revenue report will detail the period, individual sale items (either ala carte or
promotional items) and total revenue.

Functional Requirements:
1. Create/Update/Remove menu item
2. Create/Update/Remove promotion
3. Create order
4. View order
5. Add/Remove order item/s to/from order
6. Create reservation booking
7. Check/Remove reservation booking
8. Check table availability
9. Print bill invoice
10. Print sale revenue report by period (eg day or month)

The application is to be developed as a Console-based application (non-Graphical UI). Data should
be stored in flat file format, either in text or binary.

## Start Up of Restaurant Reservation and Point of Sale System (RRPSS).

![Image of start up](https://github.com/ShearmanChua/CZ2002-OOP-Project/blob/master/images/startup.jpg)

## Classes and their attributes for the Restaurant Reservation and Point of Sale System (RRPSS).

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
