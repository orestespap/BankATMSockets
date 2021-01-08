import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.util.UUID;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

public class Client implements AutoCloseable {

    private static final String EXIT="EXIT";
    private static ArrayList<Object> requestContent= new ArrayList<Object>();
    private static HashMap<String, Object> requestMap = new HashMap<String, Object>();
    private static final int maximumLogInAttempts= 3;
    private static String username;
    private Connection connection;
    private Channel channel;
    private String mainQueueName = "connectionQueue";

    //request format -> {request_name:[item1, item2, ... itemN]} or {request_name: aString}

    public Client() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public static void main(String[] args) {

        try {

            Client atm = new Client();
            BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));

            int attempts = 0;
            boolean successfulLogin = false;
            requestContent.add(0, -1);
            requestContent.add(1, -1);

            //Cardholder login phase
            Serializer ser = new Serializer();
            while (attempts < maximumLogInAttempts) {
                System.out.println("ATM log in page (" + (maximumLogInAttempts - attempts) + " attempts remaining):\nType in EXIT to remove card\nUsername: ");
                username = userInputReader.readLine();

                if (username.toUpperCase().equals(EXIT)) {
                    System.out.println("Removing card and exiting ...");
                    attempts = maximumLogInAttempts;
                    atm.channel.close();
                    atm.connection.close();
                    continue;
                }

                System.out.println("Password for " + username + ": ");

                String password = userInputReader.readLine();
                requestContent.set(0, username.toLowerCase());
                requestContent.set(1, password);
                requestMap.put("validateCredentials", requestContent);
                ByteArrayOutputStream out = ser.serialize(requestMap);

                String credentialsCheck = (String) atm.call(out);
                requestMap.clear();
                if (credentialsCheck.equals("false")) {
                    System.out.println("System.out.println(\"Either username doesn't exit or the password you've typed is invalid...\");");
                    continue;
                }
                successfulLogin = true;
                break;
            }

            if (successfulLogin) {

                requestMap.put("getName", username);
                String name = (String) atm.call(ser.serialize(requestMap));
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

                    requestMap.clear();

                    if (option.equals("1")) {
                        requestMap.put("getBalance", username);
                        String balance = (String) atm.call(ser.serialize(requestMap));
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


                        requestContent.set(1, Integer.parseInt(depositAmount));
                        requestMap.put("deposit", requestContent);

                        String response = (String) atm.call(ser.serialize(requestMap));


                        if (response.equals("true")) {
                            requestMap.clear();
                            requestMap.put("getBalance",username);
                            String balance = (String) atm.call(ser.serialize(requestMap));
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

                        requestContent.set(1, Integer.parseInt(withdrawalAmount));
                        requestMap.put("withdraw", requestContent);

                        String response = (String) atm.call(ser.serialize(requestMap));

                        if (response.equals("true")){
                            requestMap.clear();
                            requestMap.put("getBalance",username);
                            String balance = (String) atm.call(ser.serialize(requestMap));
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


            atm.close();
            userInputReader.close();
            try {
                ser.closeConnections();
            }
            catch (NullPointerException e){
                //e.printStackTrace();
            }

        } catch (IOException | InterruptedException | TimeoutException e ) {
            e.printStackTrace(); //For testing purposes
            System.out.println("Failed to connect to the server or thread has been interrupted ...");
        } catch (Exception e) {
            //e.printStackTrace();
        }

        System.out.println("Bye!");
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

    public Object call(ByteArrayOutputStream out) throws IOException, InterruptedException {
        final String corrId = UUID.randomUUID().toString();

        String replyQueueName = channel.queueDeclare().getQueue(); //unique queue, where the server should respond
        System.out.println(replyQueueName);
        AMQP.BasicProperties properties = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();


        //default direct exchange to main queue
        channel.basicPublish("", mainQueueName, properties, out.toByteArray());

        final BlockingQueue<Object> response = new ArrayBlockingQueue<>(1);

        //consuming server response to client request
        String consumer_Tag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response.offer(new String(delivery.getBody(), "UTF-8"));
            }
        }, consumerTag -> {
        });

        String result =(String) response.take();
        channel.basicCancel(consumer_Tag);

        return result;
    }

    @Override
    public void close() throws Exception {
        channel.close();
        connection.close();
    }
}