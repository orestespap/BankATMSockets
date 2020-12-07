import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.xml.crypto.Data;
import java.io.DataOutputStream;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.inc;

public class ServerThread extends Thread{

    private Socket activeSocket;
    private static final String EXIT="EXIT";
    private static final int timeOutInMS=150000;
    private static final int maxWith=2000; //maximum daily withdrawal limit
    private MongoCollection<Document> clientsCollection;
    private String userName;

    public ServerThread(Socket aSocket) throws IOException {
        this.activeSocket=aSocket;
        this.clientsCollection= Database.getClients();
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
            this.userName=username;
            return client.get("password").equals(password);
        }
        catch (NullPointerException e){
            //client object is null pointer --> mongo returned zero results
            return  false;
        }
    }

    private double getAccountBalance(){
        return (double) this.clientsCollection.find(new Document("username", this.userName)).first().get("balance");
    }

    private String getClientName(){
        Document client=this.clientsCollection.find(new Document("username", this.userName)).first();
        return client.get("firstName").toString()+" "+client.get("lastName");

    }

    private boolean withdraw(int amount){
        return true;
    }

    private boolean deposit(int amount){
        try{
            Bson filter = eq("username", userName);
            Bson incrementBalance = inc("balance", amount);
            clientsCollection.updateOne(filter,incrementBalance);
        }
        catch (NullPointerException e){
            return false;
        }
        return true;
    }
}