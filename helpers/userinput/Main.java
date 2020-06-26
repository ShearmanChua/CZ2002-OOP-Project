package helpers.userinput;

import java.util.*;
import java.text.SimpleDateFormat;

import helpers.userinput.UserInput;


class Main {
	public static void main(String args[]) {
		while (true) {
	        int i;
	        String s;
	        Date d;
	        
	        int[] accepts = {2, 4, 8, 10};
	        i = UserInput.getInt(accepts, "Enter 2, 4, 8 or 10: ", null);
	        show(i);
	        
	        i = UserInput.getIntFromRange(1, 123);
	        show(i);
	        
	        i = UserInput.getIntFromRange(7423, 7423);
    		show(i);
	        
	        i = UserInput.getInt();
	        show(i);
	        
	        
	        s = UserInput.getString();
	        show(s);
	     
	        s = UserInput.getString(4, null, null);
	        show(s);

	        for (int n=0; n<3; n++) {
		        d = UserInput.getDate();
		        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
		        show(df.format(d));
	        }
    		
	        System.out.printf("-----\n\n");
    		
    	}
	}
	
	public static void show(String s) {
    	System.out.printf("You entered: %s\n\n", s);
    }
    
    public static void show(int d) {
    	show(Integer.toString(d));
    }
}
