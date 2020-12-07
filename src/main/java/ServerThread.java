import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.inc;

public class ServerThread extends Thread{

    private Socket activeSocket;
    private static final String EXIT="EXIT";
    private static final int timeOutInMS=150000;
    private static final int maxWith=2000; //maximum daily withdrawal limit
    private MongoCollection<Document> clientsCollection;
    private int day, month;
    private String userName;
    private Bson userFilter;

    public ServerThread(Socket aSocket) throws IOException {
        this.activeSocket=aSocket;
        this.clientsCollection= Database.getClients();
        this.day=java.time.LocalDateTime.now().getDayOfMonth();
        this.month=java.time.LocalDateTime.now().getMonthValue();
    }

    public void run(){

        try {
            this.activeSocket.setSoTimeout(timeOutInMS);
            ObjectInputStream inputStream=new ObjectInputStream(this.activeSocket.getInputStream()); //data input stream from client
            DataOutputStream outputStream=new DataOutputStream(this.activeSocket.getOutputStream()); //data output stream to client.

            while (true){

                HashMap<String, Object> request= (HashMap<String, Object>) inputStream.readObject();

                if (request.keySet().contains("validateCredentials")) {
                    ArrayList<Object> items = (ArrayList<Object>) request.get("validateCredentials");
                    outputStream.writeBoolean(validateCredentials((String) items.get(0), (String) items.get(1)));
                    continue;
                }

                if (request.keySet().contains("getBalance")){
                    outputStream.writeDouble(this.getAccountBalance());
                    continue;
                }

                if (request.keySet().contains("deposit")){
                    int amountToDeposit= (int) request.get("deposit");
                    outputStream.writeBoolean(this.deposit(amountToDeposit));
                    continue;
                }
                if (request.keySet().contains("withdraw")){
                    int amountToDeposit= (int) request.get("withdraw");
                    outputStream.writeBoolean(this.withdraw(amountToDeposit));
                    continue;
                }

                if (request.keySet().contains("getName")){
                    outputStream.writeUTF(this.getClientName());
                    continue;
                }
            }

        }
        catch (SocketException socketException) {
            socketException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean validateCredentials(String username, String password){
        Document client = this.clientsCollection.find(new Document("username", username)).first();
        try{
            this.userFilter=eq("username", client.get("username"));
            this.userName=username;
            return client.get("password").equals(password);
        }
        catch (NullPointerException e){
            //client object is null pointer --> mongo returned zero results
            return  false;
        }
    }

    private double getAccountBalance(){
        return (double) this.clientsCollection.find(this.userFilter).first().get("balance");
    }

    private String getClientName(){

        Document client=this.clientsCollection.find(this.userFilter).first();

        Document logLogin = new Document().append("day",this.day).append("month",
        this.month).append("year", LocalDateTime.now().getYear()).append("hour",
        LocalDateTime.now().getHour()).append("minute", LocalDateTime.now().getMinute());

        clientsCollection.updateOne(this.userFilter, Updates.addToSet("loginsLog",logLogin));

        return client.get("firstName").toString()+" "+client.get("lastName");

    }

    private boolean withdraw(int amount){

        ReentrantLock userLock = Server.getUserLock(this.userName);

        userLock.lock();

        boolean transactionSuccess = false;

        try{
            double currentBalance = this.getAccountBalance();
            Document client=this.clientsCollection.find(this.userFilter).first();

            ArrayList<Object> withdrawalsLog =(ArrayList<Object>)client.get("withdrawalsLog");

            int sum=0;
            for(int i=0;i<withdrawalsLog.size();i++) {
                Document transaction= (Document) withdrawalsLog.get(i);
                if((int)transaction.get("day")==this.day & (int)transaction.get("month")==this.month)
                    sum+=(int) transaction.get("amount");
            }

            System.out.println(sum);
            if (amount+sum<=maxWith & amount<=currentBalance) {

                Bson incrementBalance = inc("balance", -amount);
                clientsCollection.updateOne(this.userFilter, incrementBalance);

                Document logTransaction = new Document().append("day",this.day).append("month",
                this.month).append("year",java.time.LocalDateTime.now().getYear()).append("hour",
                java.time.LocalDateTime.now().getHour()).append("minute",java.time.LocalDateTime.now().getMinute()).append("amount",amount);

                clientsCollection.updateOne(this.userFilter, Updates.addToSet("withdrawalsLog",logTransaction));

                transactionSuccess=true;
            }

        }
        catch (NullPointerException e){
            return false;
        }
        finally {
            userLock.unlock();
        }
        return transactionSuccess;
    }

    private boolean deposit(int amount){
        try{

            Bson incrementBalance = inc("balance", amount);
            clientsCollection.updateOne(this.userFilter,incrementBalance);

            Document logTransaction = new Document().append("day",this.day).append("month",
            this.month).append("year",java.time.LocalDateTime.now().getYear()).append("hour",
            java.time.LocalDateTime.now().getHour()).append("minute",java.time.LocalDateTime.now().getMinute()).append("amount",amount);

            clientsCollection.updateOne(this.userFilter, Updates.addToSet("depositsLog",logTransaction));

        }
        catch (NullPointerException e){
            return false;
        }
        return true;
    }
}