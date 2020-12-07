import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

public class Client {

    private static final String HOST = "localhost";
    private static final int PORT = 9999;
    private static final String EXIT="EXIT";
    private static ArrayList<Object> requestContent= new ArrayList<Object>();
    private static HashMap<String, Object> requestMap = new HashMap<String, Object>();
    private static final int maximumLogInAttempts= 3;
    private static String username;

    public static void main(String[] args) {

        try {
            Socket s = new Socket(HOST, PORT);
            System.out.println("Successfully connected to server\n");

            DataInputStream din = new DataInputStream(s.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(s.getOutputStream());
            BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));

            //String requestServedBy= "| Request served by server thread: "+Thread.currentThread().getName();
            int attempts = 0;
            boolean successfulLogin = false;
            requestContent.add(0, -1);
            requestContent.add(1, -1);

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
                requestContent.set(0, username.toLowerCase());
                requestContent.set(1, password);
                requestMap.put("validateCredentials", requestContent);

                outputStream.writeObject(requestMap);
                boolean credentialsCheck = din.readBoolean();
                requestMap.clear();

                if (!credentialsCheck) {
                    System.out.println("Either username doesn't exit or the password you've typed is invalid...");
                    attempts += 1;
                    outputStream.reset();
                    continue;
                }

                successfulLogin = true;
                break;
            }

            if (successfulLogin) {
                outputStream.reset();

                requestMap.put("getName", 1);
                outputStream.writeObject(requestMap);

                String name = din.readUTF();
                String option = "0";

                //ATM Menu
                System.out.println("Welcome " + name + "!");
                while (!option.equals("4")) {
                    outputStream.reset(); //make sure that we send fresh and not cached data

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

                    requestMap.clear();
                    if (option.equals("1")) {
                        requestMap.put("getBalance", 1);
                        outputStream.writeObject(requestMap);
                        Double balance = din.readDouble();
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

                        requestMap.put("deposit", Integer.parseInt(depositAmount));

                        outputStream.writeObject(requestMap);

                        boolean response = din.readBoolean();


                        if (response) {
                            requestMap.clear();
                            requestMap.put("getBalance",1);
                            outputStream.reset(); //otherwise it will send a deposit request to the server
                            outputStream.writeObject(requestMap);
                            double balance = din.readDouble();
                            System.out.println("Amount deposited successfully.\nAccount balance: "+balance+" Euro");
                            Thread.sleep(5000);
                        }
                        else
                            System.out.println("Deposit failed. Please try again later.");

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

                        requestMap.put("withdraw", Integer.parseInt(withdrawalAmount));

                        outputStream.writeObject(requestMap);

                        boolean response = din.readBoolean();

                        if (response)
                            System.out.println(".");
                        else
                            System.out.println("Withdrawal failed.\nPossible reasons for failure:\n1.)Bank server is down\n2.)The amount you've requested is greater than your current account balance\n3.)You've exceeded your daily withdrawal limit\n4.)This ATM's funds aren't enough\nPlease try again later.");

                        continue;

                    }
                }
            }
            else
                System.out.println("Exiting ...");


            din.close();
            outputStream.close();
            userInputReader.close();
            s.close();
            }

        catch(IOException | InterruptedException e){
            //e.printStackTrace();
            System.out.println("Failed to connect to the server or thread has been interrupted ...");
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