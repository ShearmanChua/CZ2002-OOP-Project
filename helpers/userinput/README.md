### `UserInput` Usage

```java
import java.util.Date;
import java.text.SimpleDateFormat;

import helpers.UserInput;

// Getting ints
int i;
//// Get int from user, can be any value (32-bit signed)
i = UserInput.getInt();
//// Get int, any value,  with custom input and error prompts:
i = UserInput.getInt(null, "Enter a number: ", "That is not a number!");
//// Get int, accept only specific values:
int[] acceptVals = {2, 4, 8, 10};
i = UserInput.getInt(acceptVals, "Enter 2, 4, 8 or 10: ", "Invalid table size.");
//// Get int from user, int must be 1, 2, 3, 4 or 5:
i = UserInput.getIntFromRange(1, 5);
i = UserInput.getIntFromRange(5, 1);  // reverse works too

// Getting strings
String s;
//// Get string, default max length of 100, default prompts:
s = UserInput.getString();
//// Get string, max length of 8 characters:
s = UserInput.getString(
        8, 
        "Enter your 8-character discount code: ", 
        "The code you provided is too long.");

// Getting dates
Date d;
//// Get date, default prompts:
d = UserInput.getDate();
//// Get date, using custom input and error prompts:
d = UserInput.getDate(
        "Enter date: dd/mm OR dd/mm/yyyy OR today OR tmr OR tomorrow",
        "Only supports dates from year 1000 to 3999");
```

### Expected output from running Main file:

```
Enter 2, 4, 8 or 10: 2
You entered: 2

Please enter a number between 1 and 123 (inclusive): 444
You have input a number that is not within the range of accepted numbers.
Please enter a number between 1 and 123 (inclusive): 123
You entered: 123

Please enter the number 7423 exactly to continue: 7423
You entered: 7423

Please enter any number: 123123123
You entered: 123123123

Please enter text (up to 100 characters long): asdasdalkjqwe
You entered: asdasdalkjqwe

Please enter text (up to 4 characters long): asdfgh
The text you have entered is too long (expecting only up to 4 characters long).
Please enter text (up to 4 characters long): asdf
You entered: asdf

Please enter 'today/tomorrow/tmr' or a custom date (dd/mm or dd/mm/yyyy): 
today
You entered: 26 Mar 2019

Please enter 'today/tomorrow/tmr' or a custom date (dd/mm or dd/mm/yyyy): 
tmr
You entered: 27 Mar 2019

Please enter 'today/tomorrow/tmr' or a custom date (dd/mm or dd/mm/yyyy): 
22/2/2022
You entered: 22 Feb 2022
```