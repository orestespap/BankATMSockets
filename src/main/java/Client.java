import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;

public class Client {

    private static final String EXIT="EXIT";
    private static final int maximumLogInAttempts= 3;
    private static String username;

    public static void main(String[] args) {

        try {
            Registry registry = LocateRegistry.getRegistry("localhost",7070);
            WorkerInter stub = (WorkerInter) registry.lookup("Worker");
            BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));

            int attempts = 0;
            boolean successfulLogin = false;

            //Cardholder login phase
            while (attempts < maximumLogInAttempts) {
                System.out.println("ATM log in page (" + (maximumLogInAttempts - attempts) + " attempts remaining):\nType in EXIT to remove card\nUsername: ");
                username = userInputReader.readLine();

                if (username.toUpperCase().equals(EXIT)) {
                    System.out.println("Removing card and exiting ...");
                    attempts = maximumLogInAttempts;
                    continue;
                }

                System.out.println("Password for " + username + ": ");

                String password = userInputReader.readLine();

                boolean credentialsCheck = stub.validateCredentials(username,password);

                if (!credentialsCheck) {
                    System.out.println("Either username doesn't exit or the password you've typed is invalid...");
                    attempts += 1;
                    continue;
                }
                successfulLogin = true;
                break;
            }

            if (successfulLogin) {

                String name = stub.getClientName(username);
                String option = "0";

                //ATM Menu
                System.out.println("Welcome " + name + "!");
                while (!option.equals("4")) {

                    System.out.println("---OPTIONS---\n1.)See balance\n2.)Deposit\n3.)Withdrawal\n4.)Exit\nInput (1-4): ");
                    option = userInputReader.readLine();

                    if (option.equals("4")) {
                        System.out.println("Logging you out!");
                        continue;
                    }

                    if (!Arrays.asList("1", "2", "3").contains(option)) {
                        System.out.println("Wrong input. Please try again.");
                        continue;
                    }

                    if (option.equals("1")) {
                        Double balance = stub.getAccountBalance(username);
                        System.out.println("Account balance: " + balance +" Euro");
                        Thread.sleep(5000);
                        continue;
                    }

                    if (option.equals("2")) {
                        String depositAmount = "0";
                        boolean depositCheck = false;

                        while (!depositCheck) {
                            System.out.println("Type in the amount you wish to deposit or 0 to cancel: ");
                            depositAmount = userInputReader.readLine();
                            depositCheck = depositValidation(depositAmount);
                            if (!depositCheck)
                                System.out.println("Invalid input. Input must be 0 or a positive integer that's a multiple of 5.");
                        }
                        if (depositAmount.equals("0"))
                            continue;

                        boolean response = stub.deposit(username,Integer.parseInt(depositAmount));

                        if (response) {
                            double balance = stub.getAccountBalance(username);
                            System.out.println("Amount deposited successfully.\nAccount balance: "+balance+" Euro");
                        }
                        else
                            System.out.println("Deposit failed. Please try again later.");
                        Thread.sleep(5000);

                        continue;
                    }

                    if (option.equals("3")) {

                        String withdrawalAmount = "0";
                        boolean withdrawalCheck = false;

                        while (!withdrawalCheck) {
                            System.out.println("Type in the amount you wish to withdraw or 0 to cancel: ");

                            withdrawalAmount = userInputReader.readLine();
                            withdrawalCheck = withdrawalValidation(withdrawalAmount);

                            if (!withdrawalCheck)
                                System.out.println("Invalid input. Input must be 0 or a positive integer that's a multiple of either 20 or 50.");
                        }

                        boolean response = stub.withdraw(username,Integer.parseInt(withdrawalAmount));

                        if (response){
                            double balance = stub.getAccountBalance(username);
                            System.out.println("Amount withdrawn successfully.\nAccount balance: "+balance+" Euro");

                        }
                        else
                            System.out.println("Withdrawal failed.\nPossible reasons for failure:\n1.)Bank server is down\n2.)The amount you've requested is greater than your current account balance\n3.)You've exceeded your daily withdrawal limit\n4.)This ATM's funds aren't enough\nPlease try again later.");
                        Thread.sleep(5000);
                        continue;

                    }
                }
            }
            else
                System.out.println("Exiting ...");

            userInputReader.close();
            }

        catch(IOException | InterruptedException | NotBoundException e){
            //e.printStackTrace();
            System.out.println("Failed to connect to the server ...");
        }


    }
    public static boolean depositValidation(String x){
        int amount=0;

        try {
            amount = Integer.parseInt(x);
        }
        catch (NumberFormatException e){
            return false;
        }
        return amount==0 || (amount>0 & amount%5==0);
    }

    public static boolean withdrawalValidation(String x){
        int amount=0;
        try {
            amount = Integer.parseInt(x);
        }
        catch (NumberFormatException e){
            return false;
        }
        return amount==0 || (amount>0 & (amount%20==0 || amount%50==0));
    }
}