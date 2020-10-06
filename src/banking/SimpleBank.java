package banking;

import java.util.*;

public class SimpleBank {

    private final Scanner input = new Scanner(System.in);
    private final Random randomNumber = new Random();
    private final Database database;
    private static final String IIN = "400000";

    public SimpleBank(Database database) {
        this.database = database;
    }

    public void init() {
        while (true) {
            System.out.print("1. Create an account\n" +
                    "2. Log into account\n" +
                    "0. Exit\n");
            int option = input.nextInt();
            switch (option) {
                case 1:
                    createCard();
                    break;
                case 2:
                    logIn();
                    break;
                case 0:
                    System.out.println("Bye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid input provided.");
                    break;
            }
        }
    }

    protected void createCard() {
        String cardNumber = "0";
        do {
            long restOfTheNumber = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
            cardNumber = IIN + restOfTheNumber;
        } while (!validateCardNumber(cardNumber));
        int pinNumber = randomNumber.nextInt(10000);
        database.createTable();
        database.insert(cardNumber, String.valueOf(pinNumber), 0);
        System.out.println();
        System.out.println("Your card has been created");
        System.out.println("Your card number: ");
        System.out.println(cardNumber);
        System.out.println("Your card PIN:");
        System.out.printf("%04d%n", pinNumber);
    }

    protected boolean validateCardNumber(String accountNumber) {
        long sum = 0;
        for (int i = 0; i < accountNumber.length(); i++){
            char tmp = accountNumber.charAt(i);
            long digit = tmp - '0';
            long product;
            if (i % 2 != 0){
                product = digit * 1;
            }
            else{
                product = digit * 2;
            }
            if (product > 9)
                product -= 9;
            sum += product;
        }
        return (sum % 10 == 0);
    }

    private void addIncome(String cardNumber) {
        System.out.println("Enter income:");
        int amount  = input.nextInt();
        database.increaseBalance(amount, cardNumber);
        System.out.println("Income was added!");
    }

    private void sendMoney(String cardNumber) {
        System.out.print("Transfer\n" +
                "Enter card number:\n");

        String recipientCardNumber = input.next();
        if (!validateCardNumber(recipientCardNumber)) {
            System.out.println("Probably you made mistake in the card number. Please try again!");
            return;
        } else if (!database.isThereAClient(recipientCardNumber)) {
            System.out.println("Such a card does not exist.");
            return;
        } else if (recipientCardNumber.equals(cardNumber)) {
            System.out.println("You can't transfer money to the same account!");
            return;
        }

        System.out.println("Enter how much money you want to transfer:");
        int amount = input.nextInt();
        if (database.getBalance(cardNumber) > amount) {
            database.decreaseBalance(amount, cardNumber);
            database.increaseBalance(amount, recipientCardNumber);
            System.out.println("Success");
        } else {
            System.out.println("Not enough money!");
        }
    }
    private void logIn() {
        System.out.println("Enter your card number:");
        long userNumber = input.nextLong();
        System.out.println("Enter your PIN:");
        int userPIN = input.nextInt();
        if (!database.isThereACardNumber(String.valueOf(userNumber), String.valueOf(userPIN))) {
            System.out.println("Wrong card number or PIN!");
            init();
        } else {
            System.out.println("You have successfully logged in!");
            logInOptions(String.valueOf(userNumber));
        }
    }

    private void logInOptions(String cardNumber) {
        while (true) {
            System.out.print("1. Balance\n" +
                    "2. Add income\n" +
                    "3. Do Transfer\n" +
                    "4. Close account\n" +
                    "5. Log out\n" +
                    "0. Exit\n");

            int logInOption = input.nextInt();
            switch (logInOption) {
                case 1:
                    System.out.print("Balance: " + database.getBalance(cardNumber) + "\n");
                    break;
                case 2:
                    addIncome(cardNumber);
                    break;
                case 3:
                    sendMoney(cardNumber);
                    break;
                case 4:
                    closeAccount(cardNumber);
                    return;
                case 5:
                    System.out.println("You have successfully logged out!");
                    return;
                case 0:
                    System.out.println("Bye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid input provided.");
                    break;
            }
        }
    }

    private void closeAccount(String cardNumber) {
        database.removeAccount(cardNumber);
        System.out.println("The account has been closed!");
    }
}