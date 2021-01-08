import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.inc;

public class Services {
    private static MongoCollection<Document> clientsCollection;
    private static final int maxWith=2000; //maximum daily withdrawal limit

    public static void setUpService(){
        clientsCollection= Database.getClients();
    }

    public static boolean validateCredentials(String username, String password){
        Document client = clientsCollection.find(new Document("username", username)).first();
        try{
            return client.get("password").equals(password);
        }
        catch (NullPointerException e){
            //client object is null pointer --> mongo returned zero results
            return  false;
        }
    }

    public static Double getAccountBalance(String username){
        return (Double) clientsCollection.find(new Document("username", username)).first().get("balance");
    }

    public static String getClientName(String username){

        Document client=clientsCollection.find(new Document("username", username)).first();

        Document logLogin = new Document().append("hour",
                LocalDateTime.now().getHour()).append("minute",
                LocalDateTime.now().getMinute()).append("second",
                LocalDateTime.now().getSecond());

        String subDocument=java.time.LocalDateTime.now().getDayOfMonth()+""+java.time.LocalDateTime.now().getMonthValue()+""+java.time.LocalDateTime.now().getYear();

        clientsCollection.updateOne(eq("username", client.get("username")), Updates.addToSet("loginsLog."+subDocument,logLogin));

        return client.get("firstName").toString()+" "+client.get("lastName");

    }

    public static boolean withdraw(String username, int amount){
        ReentrantLock userLock = Server.getUserLock(username);
        userLock.lock();

        Document client;

        try {
            client = clientsCollection.find(new Document("username", username)).first();
        }
        catch (NullPointerException e){
            //e.printStackTrace();
            return false;
        }

        if(amount>(double)client.get("balance") || amount>maxWith)
            return false;

        boolean transactionSuccess = false;

        try{

            String subDocument=java.time.LocalDateTime.now().getDayOfMonth()+""+java.time.LocalDateTime.now().getMonthValue()+""+java.time.LocalDateTime.now().getYear();

            ArrayList<Object> withdrawalsLog = (ArrayList<Object>) ((Document) client.get("withdrawalsLog")).get(subDocument);

            int sum=0;
            if (withdrawalsLog!=null) // -> mongo returns 0 results because the client has not made any withdrawals on the given date
                for(int i=0;i<withdrawalsLog.size();i++) {
                    Document transaction= (Document) withdrawalsLog.get(i);
                    sum+=(int) transaction.get("amount");
                }

            //System.out.println(sum); //For testing purposes
            if (amount+sum<=maxWith) {

                Bson incrementBalance = inc("balance", -amount);
                clientsCollection.updateOne(eq("username", client.get("username")), incrementBalance);

                Document logTransaction = new Document().append("hour",
                        java.time.LocalDateTime.now().getHour()).append("minute",
                        java.time.LocalDateTime.now().getMinute()).append("second",
                        java.time.LocalDateTime.now().getSecond()).append("amount",
                        amount);

                clientsCollection.updateOne(eq("username", client.get("username")), Updates.addToSet("withdrawalsLog."+subDocument,logTransaction));

                transactionSuccess=true;
            }

        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        finally {
            userLock.unlock();
        }
        return transactionSuccess;
    }

    public static boolean deposit(String username, int amount){
        try{

            Bson incrementBalance = inc("balance", amount);
            clientsCollection.updateOne(new Document("username", username),incrementBalance);

            String subDocument=java.time.LocalDateTime.now().getDayOfMonth()+""+java.time.LocalDateTime.now().getMonthValue()+""+java.time.LocalDateTime.now().getYear();

            Document logTransaction = new Document().append("hour",
                    java.time.LocalDateTime.now().getHour()).append("minute",
                    java.time.LocalDateTime.now().getMinute()).append("second",
                    java.time.LocalDateTime.now().getSecond()).append("amount",
                    amount);

            clientsCollection.updateOne(new Document("username", username), Updates.addToSet("depositsLog."+subDocument,logTransaction));

        }
        catch (NullPointerException e){
            return false;
        }
        return true;
    }

}
