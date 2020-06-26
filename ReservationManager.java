import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import helpers.userinput.UserInput;
import java.util.Scanner;
import java.io.Serializable;


public class ReservationManager {

	private ArrayList<Reservation> reservation;
	private TableManager tableManager;
	private Session session;
	private final String pathtofile = "DB/reser.ser";

	// Time to wait for customer before reservation expires
	// Default: EXPIRY_MINS = 30
	public static final int EXPIRY_MINS = 30;

	
	/**
	 *constructor stores, array of reservation
	 *read reservations from serial file
	 *start a new restaurant session
	 *store table manager
	 *@param tableManager  The TableManager instance. An internal reference to this will be stored.
	 */
	public ReservationManager(TableManager tableManager) {
		this.readFromFile();
		this.checkForExpired();
		session = new Session();
		this.tableManager=tableManager;
		this.startSession(session);
	}

	/**
	 *block tables for user when session begins
	 *@param session  The session instance that is generated on starting the RRPSS
	 */
	public void startSession(Session session){
		
		String period = session.getPeriod();
		//period = "AM";
//		System.out.println();
//		System.out.println("Welcome,now starting "+period+" session");
//		System.out.println();
		if(period=="AM") {
			Date date = Calendar.getInstance().getTime();  
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");  
			String strDate = dateFormat.format(date);
			String sessionStart = strDate + " 11:00";
			String sessionEnd = strDate + " 15:00";
			Date startDate=null;
			Date endDate=null;
			startDate = DateHandling.stringToDateTime(sessionStart);
			endDate = DateHandling.stringToDateTime(sessionEnd);
			
			for(Reservation res : reservation) {
				if((res.getArriveTime().compareTo(startDate) > 0)&&(res.getArriveTime().compareTo(endDate) < 0)) {
						res.setTableAlloc(this.tableManager.allocateReservation(res));
				}
			}
		}
		else if(period=="PM") {
			Date date = Calendar.getInstance().getTime();  
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");  
			String strDate = dateFormat.format(date);
			String sessionStart = strDate + " 18:00";
			String sessionEnd = strDate + " 22:00";
			Date startDate=null;
			Date endDate=null;
			
			startDate=DateHandling.stringToDateTime(sessionStart);
			endDate=DateHandling.stringToDateTime(sessionEnd);
			
			for(Reservation res : reservation) {
				if((res.getArriveTime().compareTo(startDate) > 0)&&(res.getArriveTime().compareTo(endDate) < 0)) {
						res.setTableAlloc(this.tableManager.allocateReservation(res));
				}
			}
		}
		
			
	}
	
	/**
	 * block tables for user when session begins
	 * this overloaded method is run after creating new reservations to update table manager
	 */
	public void startSession(){
		
		String period = session.getPeriod();
		//period = "AM";
		System.out.println();
		System.out.println("Welcome,now starting "+period+" session");
		System.out.println();
		if(period=="AM") {
			Date date = Calendar.getInstance().getTime();  
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");  
			String strDate = dateFormat.format(date);
			String sessionStart = strDate + " 11:00";
			String sessionEnd = strDate + " 15:00";
			Date startDate=null;
			Date endDate=null;
			startDate = DateHandling.stringToDateTime(sessionStart);
			endDate = DateHandling.stringToDateTime(sessionEnd);
			
			for(Reservation res : reservation) {
				if((res.getArriveTime().compareTo(startDate) > 0)&&(res.getArriveTime().compareTo(endDate) < 0)) {
						res.setTableAlloc(this.tableManager.allocateReservation(res));
				}
			}
		}
		else if(period=="PM") {
			Date date = Calendar.getInstance().getTime();  
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");  
			String strDate = dateFormat.format(date);
			String sessionStart = strDate + " 18:00";
			String sessionEnd = strDate + " 22:00";
			Date startDate=null;
			Date endDate=null;
			
			startDate=DateHandling.stringToDateTime(sessionStart);
			endDate=DateHandling.stringToDateTime(sessionEnd);
			
			for(Reservation res : reservation) {
				if((res.getArriveTime().compareTo(startDate) > 0)&&(res.getArriveTime().compareTo(endDate) < 0)) {
						res.setTableAlloc(this.tableManager.allocateReservation(res));
				}
			}
		}
		
			
	}

	/**
	 * return reservation with contact number
	 * @param contact get user input of contact number to search for reservation
	 * @return reservation return the found reservation if any
	 */
	public ArrayList<Reservation> getReservation(int contact) {
		
		if(reservation.isEmpty()) {
			System.out.println("\t There are no reservations in database!");
			return null;
		}
		ArrayList<Reservation> checkreservation = new ArrayList<Reservation>();
		for ( Reservation res : this.reservation ) {
			if ( res.getContactNo() == contact ) {
				checkreservation.add(res);
			}
		}
		return checkreservation;
	}
	
	/**
	 * This method is called to create a new reservation for customer
	 * @param contactNumber get user input contact number
	 * @param arrivalTime get user input of what time they will arrive at restaurant
	 * @param numPax get user input number of people coming
	 * @param name get user input user's name
	 */
	public void createReservation(int contactNumber,String arrivalTime, int numPax,String name){
		Date resDate=null;
		
		resDate=DateHandling.stringToDateTime(arrivalTime);
		Date checkDate = DateHandling.stringToDate(arrivalTime);
		String checkSession = Reservation.allocateSession(resDate);
		//System.out.println(checkDate);
		//System.out.println(checkSession);
		
		if(this.reservation!=null) {
			int tableKind;
			int tableCount=0;
			
			String strDate = DateHandling.DateNoTimetoString(resDate);
	
			if(numPax>8 && numPax<=10){
				tableKind=10;
				for(Reservation res : reservation) {
					Date dat = res.getArriveTime();
					String ress = DateHandling.DateNoTimetoString(dat);
					Date resDatenoTime = DateHandling.stringToDate(ress);
					if((contactNumber==res.getContactNo())&&(checkDate.equals(resDatenoTime))&&(checkSession.equals(res.getSession()))) {
						System.out.println("\t Reservation under this number already exists!");
						SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' HH:mm:ss");
						System.out.println("\t Contact Number: "+res.getContactNo());
			    	    System.out.println("\t Reservation at: " + ft.format(res.getArriveTime()) +" Session "+ res.getSession()+ " for "+res.getNoOfPax());
			    	    System.out.println("\t For: "+ res.getName());
						return;
					}
					  
					String resstrDate = DateHandling.DateNoTimetoString(res.getArriveTime());
					if((res.getTableKind()==tableKind)&&(res.getSession().equals(Reservation.allocateSession(resDate)))&&(strDate.equals(resstrDate))) {
						tableCount++;
					}
				}
				
				if(tableCount<5) {
					Reservation res = new Reservation(contactNumber,resDate,numPax,name);
					reservation.add(res);
					SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' HH:mm:ss");
					System.out.println("\t Contact number: "+res.getContactNo());
			    	System.out.println("\t Reservation at: " + ft.format(res.getArriveTime()) +" Session "+ res.getSession()+ " for "+res.getNoOfPax());
			    	System.out.println("\t For: "+ res.getName());
			    	System.out.println("\t Expiring at: "+res.getExpiredTime());
			    	System.out.println();
				}
				else {
					System.out.println("\t Fully booked for this session!");
				}
		    }
			else if(numPax>4 && numPax<=8) {
				tableKind=8;
				for(Reservation res : reservation) {
					Date dat = res.getArriveTime();
					String ress = DateHandling.DateNoTimetoString(dat);
					Date resDatenoTime = DateHandling.stringToDate(ress);
					if((contactNumber==res.getContactNo())&&(checkDate.equals(resDatenoTime))&&(checkSession.equals(res.getSession()))) {
						System.out.println("\t Reservation under this number already exists!");
						SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' HH:mm:ss");
						System.out.println("\t Contact number: "+res.getContactNo());
			    	    System.out.println("\t Reservation at: " + ft.format(res.getArriveTime()) +" Session "+ res.getSession()+ " for "+res.getNoOfPax());
			    	    System.out.println("\t For: "+ res.getName());
						return;
					}
					
					String resstrDate = DateHandling.DateNoTimetoString(res.getArriveTime());
					if((res.getTableKind()==tableKind)&&(res.getSession().equals(Reservation.allocateSession(resDate)))&&(strDate.equals(resstrDate)))
						tableCount++;
				}
				if(tableCount<5) {
					Reservation res = new Reservation(contactNumber,resDate,numPax,name);
					reservation.add(res);
					SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' HH:mm:ss");
					System.out.println("\t Contact number: "+res.getContactNo());
			    	System.out.println("\t Reservation at: " + ft.format(res.getArriveTime()) +" Session "+ res.getSession()+ " for "+res.getNoOfPax());
			    	System.out.println("\t For: "+ res.getName());
			    	System.out.println("\t Expiring at: "+res.getExpiredTime());
			    	System.out.println();
				}
				else {
					System.out.println("\t Fully booked for this session!");
				}
			}
			else if(numPax>2 && numPax<=4) {
				tableKind=4;
				for(Reservation res : reservation) {
					Date dat = res.getArriveTime();
					String ress = DateHandling.DateNoTimetoString(dat);
					Date resDatenoTime = DateHandling.stringToDate(ress);
					if((contactNumber==res.getContactNo())&&(checkDate.equals(resDatenoTime))&&(checkSession.equals(res.getSession()))) {
						System.out.println("\t Reservation under this number already exists!");
						SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' HH:mm:ss");
						System.out.println("\t Contact number: "+res.getContactNo());
			    	    System.out.println("\t Reservation at: " + ft.format(res.getArriveTime()) +" Session "+ res.getSession()+ " for "+res.getTableKind());
			    	    System.out.println("\t For: "+ res.getName());
						return;
					}
					
					String resstrDate = DateHandling.DateNoTimetoString(res.getArriveTime());
					if((res.getTableKind()==tableKind)&&(res.getSession().equals(Reservation.allocateSession(resDate)))&&(strDate.equals(resstrDate)))
						tableCount++;
				}
				if(tableCount<10) {
					Reservation res = new Reservation(contactNumber,resDate,numPax,name);
					reservation.add(res);
					SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' HH:mm:ss");
					System.out.println("\t Contact number: "+res.getContactNo());
			    	System.out.println("\t Reservation at: " + ft.format(res.getArriveTime()) +" Session "+ res.getSession()+ " for "+res.getNoOfPax());
			    	System.out.println("\t For: "+ res.getName());
			    	System.out.println("\t Expiring at: "+res.getExpiredTime());
			    	System.out.println();
				}
				else {
					System.out.println("\t Fully booked for this session!");
				}
			}
			else {
				tableKind=2;
				for(Reservation res : reservation) {
					Date dat = res.getArriveTime();
					String ress = DateHandling.DateNoTimetoString(dat);
					Date resDatenoTime = DateHandling.stringToDate(ress);
					if((contactNumber==res.getContactNo())&&(checkDate.equals(resDatenoTime))&&(checkSession.equals(res.getSession()))) {
						System.out.println("\t Reservation under this number already exists!");
						SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' HH:mm:ss");
						System.out.println("\t Contact number: "+res.getContactNo());
			    	    System.out.println("\t Reservation at: " + ft.format(res.getArriveTime()) +" Session "+ res.getSession()+ " for "+res.getTableKind());
			    	    System.out.println("\t For: "+ res.getName());
						return;
					}
					 
					String resstrDate = DateHandling.DateNoTimetoString(res.getArriveTime());
					if((res.getTableKind()==tableKind)&&(res.getSession().equals(Reservation.allocateSession(resDate)))&&(strDate.equals(resstrDate)))
						tableCount++;
				}
				if(tableCount<10) {
					Reservation res = new Reservation(contactNumber,resDate,numPax,name);
					reservation.add(res);
					SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' HH:mm:ss");
					System.out.println("\t Contact number: "+res.getContactNo());
			    	System.out.println("\t Reservation at: " + ft.format(res.getArriveTime()) +" Session "+ res.getSession()+ " for "+res.getNoOfPax());
			    	System.out.println("\t For: "+ res.getName());
			    	System.out.println("\t Expiring at: "+res.getExpiredTime());
			    	System.out.println();
				}
				else {
					System.out.println("\t Fully booked for this session!");
				}
			}
		
		}
		else {
			Reservation res = new Reservation(contactNumber,resDate,numPax,name);
			reservation = new ArrayList<Reservation>();
			reservation.add(res);
			SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' HH:mm:ss");
			System.out.println("\t Contact number: "+res.getContactNo());
	    	System.out.println("\t Reservation at: " + ft.format(res.getArriveTime()) +" Session "+ res.getSession()+ " for "+res.getNoOfPax());
	    	System.out.println("\t For: "+ res.getName());
	    	System.out.println("\t Expiring at: "+res.getExpiredTime());
	    	System.out.println();
		}
		
	}

	/**
	 *cancel reservation with contact number
	 * @param contact get user input contact number
	 */
	public void removeReservation(int contact) {
		
		ArrayList<Reservation> checkreservation = new ArrayList<Reservation>();
		for ( Reservation res : this.reservation ) {
			if ( res.getContactNo() == contact ) {
				checkreservation.add(res);
			}
		}
		Reservation foundRes = null;
		
		if(checkreservation.size()>1) {
			while(foundRes==null) {
				for(Reservation res: checkreservation) {
					Date dNow = res.getArriveTime();
				      SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' HH:mm:ss");

			    	  System.out.println("\t Contact number: "+res.getContactNo());
			    	  System.out.println("\t Reservation at: " + ft.format(dNow) +" Session "+ res.getSession()+ " for "+res.getNoOfPax());
			    	  System.out.println("\t For: "+ res.getName());
			    	  System.out.println("\t Expiring at: "+res.getExpiredTime());
			    	  System.out.println();
				}
				System.out.println("\t Which reservation would you like to remove?");
				Scanner sc = new Scanner(System.in);
				System.out.print("\t Please enter a valid date in the format (dd/MM/yyyy): ");
				//sc.nextLine();
				String dateTime = sc.nextLine();
				while(!DateHandling.isThisDateValid(dateTime)) {
					System.out.print("\t Please enter a valid date in the format (dd/MM/yyyy): ");
					dateTime = sc.nextLine();
				}
				System.out.print("\t Please enter a time(HH:mm) between AM Session(11:00 to 15:00) or PM Session(18:00 to 22:00): ");
				
				String time = sc.nextLine();
				time = dateTime + " " + time;
				
				while(!DateHandling.isThisTimeValid(time)) {
					System.out.print("\t Please enter a time(HH:mm) between AM Session(11:00 to 15:00) or PM Session(18:00 to 22:00): ");
					time = sc.nextLine();
					time = dateTime + " " + time;
				}
				
				for(Reservation res : checkreservation) {
					//System.out.println(DateHandling.DateTimetoString(res.getArriveTime()));
					if (time.equals(DateHandling.DateTimetoString(res.getArriveTime()))) {
						foundRes = res;
					}
				}
				if(foundRes==null) {
					System.out.println();
					System.out.println("\t Please enter a correct reservation date and time to remove reservation");
				}
			}
		}
		else {
			for(Reservation res : checkreservation) {
				if ( res.getContactNo() == contact ) {
					foundRes = res;
				}
			}
		}
		if(foundRes!=null) {
			SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' HH:mm:ss");
			System.out.println("\t Removing Reservation: ");
	    	System.out.println("\t Contact number: "+foundRes.getContactNo());
	    	System.out.println("\t Reservation at: " + ft.format(foundRes.getArriveTime()) +" Session "+ foundRes.getSession()+ " for "+foundRes.getNoOfPax());
	    	System.out.println("\t For: "+ foundRes.getName());
	    	System.out.println();
			reservation.remove(foundRes);
		}
		else {
			System.out.println("\t No reservation found under this contact number!");
		}
	}
	
	public void unsetReservation(int tableId) {
		
		Reservation foundRes = null;
		for ( Reservation res : this.reservation ) {
			if ( res.getTableAlloc() == tableId ) {
				foundRes = res;
			}
		}
		
		if(foundRes!=null) {
			SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' HH:mm:ss");
			System.out.println("Reservation arrived!!!: ");
	    	System.out.println("Contact number: "+foundRes.getContactNo());
	    	System.out.println("Reservation at: " + ft.format(foundRes.getArriveTime()) +" Session "+ foundRes.getSession()+ " for "+foundRes.getNoOfPax());
	    	System.out.println("For: "+ foundRes.getName());
	    	System.out.println();
			reservation.remove(foundRes);
		}
	}

	/**
	 * retrieve data into 
	 * retrieve all data and store into Arraylist of Reservations
	 */
	public void readFromFile(){

		File tempFile = new File(this.pathtofile);
		boolean exists = tempFile.exists();
		if(exists==true) {
			try {
		         FileInputStream fileIn = new FileInputStream(this.pathtofile);
		         ObjectInputStream in = new ObjectInputStream(fileIn);
		         this.reservation = (ArrayList) in.readObject();
		         in.close();
		         fileIn.close();
		      } catch (IOException i) {
		    	 System.out.println("Has empty serial file");
		         i.printStackTrace();
		         return;
		      } catch (ClassNotFoundException c) {
		         System.out.println("Reservation class not found here");
		         c.printStackTrace();
		         return;
		      }
			if(!reservation.isEmpty()) {
				
				System.out.println("Reservations retrieved: ");
				System.out.println();
				for(Reservation res : reservation){
					  Date dNow = res.getArriveTime();
				      SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' HH:mm:ss");

			    	  System.out.println("Contact number: "+res.getContactNo());
			    	  System.out.println("Reservation at: " + ft.format(dNow) +" Session "+ res.getSession()+ " for "+res.getNoOfPax());
			    	  System.out.println("For: "+ res.getName());
			    	  System.out.println("Expiring at: "+res.getExpiredTime());
			    	  System.out.println();

			     } 
				System.out.println();
			}
		}
		else {
			reservation = new ArrayList<Reservation>();
		}
	
	}

	/**
	 * insert data into serial file
	 * will rewrite the entire serial file
	 * @param reser pass in the array of reservations to be stored
	 */
	public void writetoFile(ArrayList<Reservation> reser){

		try {
	         FileOutputStream fileOut =
	         new FileOutputStream(this.pathtofile);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(reser);
	         out.close();
	         fileOut.close();
	         System.out.println("Serialized data is saved in DB/reservationDB.ser");
	         System.out.println();
	      } catch (IOException i) {
	         i.printStackTrace();
	      }
		for(Reservation res : reservation){
			  Date dNow = res.getArriveTime();
			  
		      SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' HH:mm:ss");

		      System.out.println("Contact number: "+res.getContactNo());
	    	  System.out.println("Reservation at: " + ft.format(dNow) +" Session "+ res.getSession()+ " for "+res.getNoOfPax());
	    	  System.out.println("For: "+ res.getName());
	    	  System.out.println("Expiring at: "+res.getExpiredTime());
	    	  System.out.println();

	     } 
		
	}
	public ArrayList<Reservation> getReservation() {
		return reservation;
	}

	/**
	 *check for expired reservation
	 */
	private void checkForExpired(){
		if(reservation==null) {
			return;
		}
		if(!reservation.isEmpty()) {
			ArrayList<Reservation> notExpired=new ArrayList<Reservation>();
			for(Reservation res : reservation) {
				if(!res.getIsExpired()) {
					notExpired.add(res);
				}
			}
			reservation = notExpired;
			System.out.println("Reservations not expired: ");
			System.out.println();
			for(Reservation res : reservation){
				  Date dNow = res.getArriveTime();
			      SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' HH:mm:ss");

			      System.out.println("Contact number: "+res.getContactNo());
		    	  System.out.println("Reservation at: " + ft.format(dNow) +" Session "+ res.getSession()+ " for "+res.getNoOfPax());
		    	  System.out.println("For: "+ res.getName());
		    	  System.out.println("Expiring at: "+res.getExpiredTime());
		    	  System.out.println();

		     } 
			
		}
	}
	/*private static void addReservation(Reservation res) {
		reservation.add(res);
	}*/
	
	public void ReservationInterface() {
		Scanner sc = new Scanner(System.in);
		int choice;
				
		
		do {
			System.out.println("(1) Book a reservation");
			System.out.println("(2) Cancel a Reservation");
			System.out.println("(3) Check Reservation");
			System.out.println("(4) Exit ReservationManager");
			System.out.println();
			System.out.print("\t Enter the number of your choice: ");
			choice = sc.nextInt();
			switch (choice) {
				case 1: 
					System.out.println("\t Assigning Reservation ..");
					String inputPrompt = "\t Please enter Contact No: ";
					String errorPrompt = "\t Please enter a valid contact number e.g.88767378";
					int contNo=UserInput.getPhoneNumber(inputPrompt, errorPrompt);
					System.out.print("\t Please enter date(dd/MM/yyyy) between "+DateHandling.DateNoTimetoString(DateHandling.getCurrentDate())+" and "+DateHandling.DateNoTimetoString(DateHandling.getMonthLaterDate())+": ");
					sc.nextLine();
					String dateTime = sc.nextLine();
					while(!DateHandling.isThisDateValid(dateTime)) {
						System.out.print("\t Please enter a valid date in the format (dd/MM/yyyy): ");
						dateTime = sc.nextLine();
					}
					while(!DateHandling.withinOneMonth(dateTime)) {
						System.out.print("\t Please enter date(dd/MM/yyyy) between "+DateHandling.DateNoTimetoString(DateHandling.getCurrentDate())+" and "+DateHandling.DateNoTimetoString(DateHandling.getMonthLaterDate())+": ");
						dateTime = sc.nextLine();
						while(!DateHandling.isThisDateValid(dateTime)) {
							System.out.print("\t Please enter a valid date in the format (dd/MM/yyyy): ");
							dateTime = sc.nextLine();
						}
					}
					
					System.out.print("\t Please enter a time(HH:mm) between AM Session(11:00 to 15:00) or PM Session(18:00 to 22:00): ");
					
					String time = sc.nextLine();
					time = dateTime + " " + time;
					
					while(!DateHandling.isThisTimeValid(time)) {
						System.out.print("\t Please enter a time(HH:mm) between AM Session(11:00 to 15:00) or PM Session(18:00 to 22:00): ");
						time = sc.nextLine();
						time = dateTime + " " + time;
					}
					dateTime=time;
					String inPrompt = "\t Please enter Number of pax: ";
					int pax = UserInput.getIntFromRange(1, 10, inPrompt, null);
					System.out.print("\t Please enter Name: ");
					String name = sc.nextLine();
					this.createReservation(contNo, dateTime, pax,name);
					break;
					
				case 2:
					inputPrompt = "\t Please enter contact number to remove Reservation: ";
					errorPrompt = "\t Please enter a valid contact number e.g.88767378";
					this.removeReservation(UserInput.getPhoneNumber(inputPrompt, errorPrompt));
					break;
					
				case 3:
					inputPrompt = "\t Please enter contact number to check Reservation: ";
					errorPrompt = "\t Please enter a valid contact number e.g.88767378";
					int num = UserInput.getPhoneNumber(inputPrompt, errorPrompt);
					ArrayList<Reservation>res = this.getReservation(num);
					if(res!=null) {
						for(Reservation reser:res) {
							SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' HH:mm:ss");
							System.out.println("\t Contact number: "+reser.getContactNo());
					    	System.out.println("\t Reservation at: " + ft.format(reser.getArriveTime()) +" Session "+ reser.getSession()+ " for "+reser.getNoOfPax());
					    	System.out.println("\t For: "+ reser.getName());
					    	System.out.println("\t Expiring at: "+reser.getExpiredTime());
					    	System.out.println();
						}
					}
					break;
				case 4: 
					this.startSession();
					System.out.println();
			        this.writetoFile(this.reservation);
					break;
			}
		} while (choice < 4);
	
		return;
	}
	
}