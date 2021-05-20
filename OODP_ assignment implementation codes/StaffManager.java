import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import helpers.userinput.UserInput;

public class StaffManager {

	private ArrayList<Staff> staffList;
	
	private Staff currentStaff;
	
	private static final String DB_FILEPATH = "DB/staff.ser";
	private static final boolean DEBUG = false;

	//Constructor
	public StaffManager() {
		staffList = new ArrayList<Staff>();
		this.restoreFromFile();
		this.currentStaff = this.fetchDefaultStaff();
		
		if ( currentStaff == null ) {
			String staffName;
			char staffGender = '\0';
			String jobTitle;
			System.out.println("No default staff user detected, please create one before proceeding.");			
			staffName = UserInput.getString(100, "Please enter name: ", null);
			
			do {
				String input = UserInput.getString(1, "Please enter M / F for gender: ", "Invalid gender given!");
				if (input.equals("\n") || input.equals("")) {
					continue;
				}
				staffGender = input.charAt(0);
			} while (!this.checkGender(staffGender));
			
			jobTitle = UserInput.getString(100, "Please enter the staff's designation ",null);
			
			this.addStaff(staffName, staffGender, jobTitle);
			this.currentStaff = this.fetchDefaultStaff();
		}
		this.loginToStaff(this.currentStaff.getID());
		
	}
	
	//Getters and setters
	public Staff getCurrentStaff() { return this.currentStaff; } 
	
	//Methods
	public Staff addStaff(String staffName, char staffGender, String staffTitle) {
		for (int id = 1; id < 11; id++) {
			if (findStaff(id) == null) {
				Staff newStaff = new Staff(id, staffName, Character.toUpperCase(staffGender), staffTitle);
				staffList.add(newStaff);
				System.out.println("Staff " + staffName + " has been created with this ID: " + id);
				this.dumpToFile();
				return newStaff;
			}
		}
		return null;
	}
	
	public void loginToStaff(int staffId) {
		Staff newUser = this.findStaff(staffId);
		if (newUser == null) {
			System.out.printf("Staff ID %d does not exist, aborting.\n", staffId);
			return;
		} else {
			this.currentStaff = newUser;
			System.out.printf("Logged in as \"%s\" (staffid=%d).\n",
					this.currentStaff.getName(),
					this.currentStaff.getID());
		}
	}
	
	public Staff findStaff(int staffId) {
		for (int i = 0; i < staffList.size(); i++) {
			if (staffList.get(i).getID() == staffId) {
				return staffList.get(i);
			}
		} 
		return null;
	}
	
	public boolean checkGender(char staffGender) {
		boolean valid = staffGender == 'M' || staffGender == 'm' || staffGender == 'F' || staffGender == 'f';
		if (!valid) {
			System.out.println("Invalid gender given!");
		}
		return valid;
	}
	
	public Staff findStaff(String staffName) {
		for (int i = 0; i < staffList.size(); i++) {
			if (staffList.get(i).getName().equalsIgnoreCase(staffName)) {
				return staffList.get(i);
			}
		} 
		return null;
	}
	
	public void removeStaff(int staffId) {
		if (staffId == this.currentStaff.getID()) {
			System.out.printf("Unable to remove currently logged-in staff (%s, id=%d).\n",
					this.currentStaff.getName(), this.currentStaff.getID());
			System.out.println("Log in to a different staff user first.");
			return;
		}
			
		Staff toRemove = findStaff(staffId);
		if (toRemove != null) {
			staffList.remove(toRemove);
			this.dumpToFile();
			System.out.println("Staff has been removed!");
		} else {
			System.out.println("Staff does not exist!");			
		}
	}
	
	public void printStaffDetails(int staffId) {
		Staff findstaff = findStaff(staffId);
		if (findstaff != null) {
			System.out.println("Staff name = " + findstaff.getName() + "\n" + "Staff Id = " + staffId + "\n"+ 
								"Staff Gender = " + findstaff.getGender() + "\n"  + "Staff's Designation = " + findstaff.getJobTitle());
		} else {
			System.out.println("Staff does not exist!");			
		}
	}
	
	public void printAllStaffDetails( ) {
		for (Staff staff : staffList) {
			System.out.println("\nStaff name = " + staff.getName() + "\n" + "Staff Id = " + staff.getID() + "\n"+ 
					"Staff Gender = " + staff.getGender() + "\n"  + "Staff's Designation = " + staff.getJobTitle());
		}
	}
	
	public void updateStaff(int staffId) {
		Staff findstaff = findStaff(staffId);
		if (findstaff != null) {
			String newJobTitle = UserInput.getString(100, "Please enter the new designation: ", null);
			findstaff.setJobTitle(newJobTitle);
			this.dumpToFile();
			System.out.println("Staff's designation has been updated.");
		} else {
			System.out.println("Staff does not exist!");			
		}
	}
	
	private Staff fetchDefaultStaff() {
		if ( this.staffList.size() > 0 )
			return this.staffList.get(0);
		return null;
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
    			"(ERROR) StaffManager: Unable to %s from %s (%s)\n",
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
			this.staffList = (ArrayList<Staff>)objInStream.readObject();
			
			int staffRestoredCount = this.staffList.size();
			
			objInStream.close();
			fileInSteam.close();
			
			if ( DEBUG )
				System.out.printf(
						"(DEBUG) StaffManager: %d staff record%s restored from %s\n",
						staffRestoredCount,
						( staffRestoredCount == 1 ) ? "" : "s",
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
			objOutStream.writeObject(this.staffList);
			objOutStream.close();
			fileOutSteam.close();

			if ( DEBUG )
				System.out.println("(DEBUG) StaffManager: Dumped to " + DB_FILEPATH);

		} catch (IOException e) {
			this.reportFailedFileOp("restore", e);
		}
	}
}
