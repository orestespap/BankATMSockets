import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class Server {

    public static void main(String[] args) throws IOException {

        Database.connectToDatabase();
        MongoCollection<Document> clientsCollection= Database.getClients();
        FindIterable<Document> allUsers=clientsCollection.find();

        Iterator it= allUsers.iterator();

        //When the server is launched, it creates a key value pair structure where each key corresponds to a client username,
        //and the value to its personal withdrawal lock. In the event that the user simultaneously log ins from two different places
        //(so two threads) and tries to withdraw money, then one of the two threads will acquire the lock and the other one will have to wait
        //for the first to release it. Unlike the deposit function, where the DBMS takes care of concurrency control, the withdrawal function
        //retrieves data from the database to make sure that the requested amount is eligible for withdrawal. So, without a lock, given some effort
        //and luck, a client would have been able to withdraw (once, before depositing money again) more money than his/her account has. As aforementioned,
        //locks are personal, so one client trying to withdraw money won't block a different client from withdrawing money at the same time.

        HashMap<String, ReentrantLock> userLocks =  new HashMap<String, ReentrantLock>();

        while (it.hasNext()) {
            Document client = (Document) it.next();
            userLocks.put((String) client.get("username"), new ReentrantLock());
        }

        WorkerInter atmf = new WorkerImpl(userLocks);
        Registry registry = LocateRegistry.getRegistry("localhost",7070);
        registry.rebind("Worker",atmf);
        //adds ATM Functions server to the RMI Registry

        System.out.println("Added ATM Function to RMI Registry located @ localhost:7070");
    }

}