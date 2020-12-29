import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class Server {

    private static final int PORT = 9999;
    private static HashMap<String, ReentrantLock> userLocks =  new HashMap<String, ReentrantLock>();;

    public static void main(String[] args) throws IOException {


        ServerSocket server=new ServerSocket(PORT);
        //passive socket exclusively listening for new connection
        System.out.println("Server is listening at port: "+PORT);
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

        while (it.hasNext()) {
            Document client = (Document) it.next();
            userLocks.put((String) client.get("username"), new ReentrantLock());
        }

        while (true) {

            System.out.println("Main thread is waiting for clients ...");

            Socket anActiveSocket=server.accept();
            //active sockets handling communication with established connections
            //which corresponds to clients using the bank's ATM machines

            System.out.println("Client with address "+anActiveSocket.getInetAddress()+" has connected ...");

            ServerThread aWorker = new ServerThread(anActiveSocket);
            aWorker.start();
        }
        //System.out.println("Server is shutting down gracefully ...");
        //server.close();

    }

    public static ReentrantLock getUserLock(String username){
        return userLocks.get(username);
    }

}