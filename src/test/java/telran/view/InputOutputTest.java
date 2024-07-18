package telran.view;


import java.time.LocalDate;

import org.junit.jupiter.api.Test;


record User(String userName, String password, LocalDate dateLastLogin, 
			String phoneNumber, int numberOfLogins) {}

class InputOutputTest {
	InputOutput io = new SystemInputOutput();

	@Test
	void readOblectTest() {
		User user = io.readObject("Enter user in format<username>#<password>#<dateLastLogin>#<phone number>#<number of logins>",
					"Wrong input format", str -> {
						String[] tokens = str.split("#");
						return new User(tokens[0], tokens[1], LocalDate.parse(tokens[2]),
								tokens[3], Integer.parseInt(tokens[4]));
					});
		io.writeLine(user);
	}
	
	@Test
	void readUserByFields() {
		//TODO
		//create user object from separate fields and display out
		//userName at least 6 ASCII letters - first Capital, others Lower case
		String userName = io.readStringPredicate("Enter username:",
		            "UserName must consist 6 ASCII letters - first Capital",
		            str -> checkUserName(str));
		//password at least 8 symbols, at least one Capital letter, 
		//at least one Lower case letter, at least one digit, 
		//at least one symbol from "#$*&%"
		String password = io.readStringPredicate("Enter password:",
	            "Password must consist 8 symbols, one capital letter, one digit, one symbol from \"#$*&%\"",
	            str -> checkPassword(str));
		//phone number - Israel mobile phone
		String phoneNumber = io.readStringPredicate("Enter phone number: ",
		            "Enter phone number should be in format 05********",
		            str -> checkPhoneNumber(str));
		//dateLastLogin not after current date
		LocalDate dateLastLogin = io.readIsoDateRange("Enter date of last login: ",
	            "Invalid date format or date is after current date",
	            LocalDate.MIN, LocalDate.now().plusDays(1));
		//number of logins any positive number
		 int numberOfLogins = io.readNumberRange("Enter number of logins:",
		            "Invalid number of logins format",
		            1, Integer.MAX_VALUE).intValue();
		 	 
		 User user = new User(userName, password, dateLastLogin, phoneNumber, numberOfLogins);		 
		 io.writeLine(user);
	}

	private boolean checkUserName(String str) {
		return str.matches("^[A-Z][a-z]{5,}$");
	}
	
	private boolean checkPassword(String str) {
		boolean res = false;
		if(str.length()==8 && str.chars().anyMatch(Character::isUpperCase)
	            			&& str.chars().anyMatch(Character::isLowerCase)
	            			&& str.chars().anyMatch(ch -> "#$*&%".indexOf(ch) != -1)){
			res = true;
		}
		return res;
	}

	private boolean checkPhoneNumber(String str) {
		return str.matches("^05[0-9]{8}$");
	}

}
