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

        WorkerInter stub = new WorkerImpl(userLocks);
        //https://docs.oracle.com/javase/1.5.0/docs/guide/rmi/hello/hello-world.html

        //The static method UnicastRemoteObject.exportObject exports the supplied remote object to receive incoming remote method
        //invocations on an anonymous TCP port and returns the stub for the remote object to pass to clients. As a result of the
        //exportObject call, the runtime may begin to listen on a new server socket or may use a shared server socket to accept
        //incoming remote calls for the remote object. The returned stub implements the same set of remote interfaces as the remote
        //object's class and contains the host name and port over which the remote object can be contacted (+ the remote object's unique reference/hash).


        Registry registry = LocateRegistry.getRegistry("localhost",7070);
        //registry -> is a stub for the remote object Registry located at localhost:7070, therefore it implements a remote interface
        //Since registry is a stub, rebind is a remote method call; the registry stub marshals the WorkerImpl stub object
        //and sends it to the corresponding stub at the actual registry's virtual machine, where the WorkerImpl stub object
        //will be unmarshalled and the actual registry.rebind() call will be performed.

        //Much like a client asking for the stub from the registry, the registry needs the .class files of
        //all the remote interfaces implemented by the stub that we are sending to it, in order for it 
        //unmarshall the stub and then proceed with binding it with a physical name. 

        
        registry.rebind("Worker",stub);
        //The WorkerImpl stub is stored in the registry binded with the name Worker. Clients can now get the
        //stub from the registry, and then will be able to send requests to the RMI Server launched via
        //the rmi.server.UnReOb extension.

        System.out.println("Added stub for WorkerImpl.java remote object to RMI Registry located @ localhost:7070");
    }

}