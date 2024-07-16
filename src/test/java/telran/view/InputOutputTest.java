package telran.view;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

record User(String username, String password,
        LocalDate dateLastLogin, String phoneNumber, int numberOfLogins) {}

class InputOutputTest {
    InputOutput io = new SystemInputOutput();

    @Test
    void readObjectTest() {
        User user = io.readObject("Enter user in format <username>#<password>#<dateLastLogin>"
                + "#<phone number>#<number of logins>", "Wrong user input format", str -> {
            String[] tokens = str.split("#");
            return new User(tokens[0], tokens[1],
                    LocalDate.parse(tokens[2]), tokens[3], Integer.parseInt(tokens[4]));
        });
        io.writeLine(user);
    }

    @Test
    void readUserByFields() {
        String username = io.readStringPredicate("Enter username (first letter uppercase, at least 6 characters): ",
                "Mistake in username", str -> Pattern.matches("^[A-Z][a-z]{5,}$", str));

        String password = io.readStringPredicate("Enter password (at least 8 characters, one uppercase, one lowercase, one digit, one special character): ",
                "Mistake in password", this::isValidPassword);

        String phoneNumber = io.readStringPredicate("Enter phone number (Israel mobile phone format): ",
                "Mistake in phone number", str -> Pattern.matches("^05\\d{8}$", str));

        LocalDate dateLastLogin = io.readObject("Enter last login date (yyyy-mm-dd): ",
                "Your date is grater then current date", str -> {
            LocalDate date = LocalDate.parse(str);
            if (date.isAfter(LocalDate.now())) {
                throw new RuntimeException("Your date is grater, then current date");
            }
            return date;
        });
        int numberOfLogins = io.readInt("Enter number of logins (positive integer): ",
                "Mistake in number of logins");

        
        User user = new User(username, password, dateLastLogin, phoneNumber, numberOfLogins);
        io.writeLine(user);
    }
    
    
    private boolean isValidPassword(String password) {
    	boolean res = false;
        if (password.length() >= 8) res = true;

        boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowercase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecialChar = password.chars().anyMatch(ch -> "#$*&%".indexOf(ch) != -1);

        return res && hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }
}

