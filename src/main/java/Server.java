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

        WorkerInter skeleton = new WorkerImpl(userLocks);
        //https://docs.oracle.com/javase/1.5.0/docs/guide/rmi/hello/hello-world.html

        //The static method UnicastRemoteObject.exportObject exports the supplied remote object to receive incoming remote method
        //invocations on an anonymous TCP port and returns the stub for the remote object to pass to clients. As a result of the
        //exportObject call, the runtime may begin to listen on a new server socket or may use a shared server socket to accept
        //incoming remote calls for the remote object. The returned stub implements the same set of remote interfaces as the remote
        //object's class and contains the host name and port over which the remote object can be contacted.

        //Since it (WorkerImpl) extends UnicastRemoteObject -> this creates a stub that communicates with the remote object.
        //Creates a passive socket that waits for client requests. Since the skeleton object is multithreaded, each client
        //is served by a different skeleton object.

        //Using the RMI registry, which is a server of its own, the RMI server binds the stub object to  a physical name (a string).
        //The stub contains information about which socket the remote object listens on and implements argument marshaling and result
        //unmarshalling.

        //The client gets the stub from the RMI registry server, and once it does it can make calls on the remote object, which will
        //be served by the associated skeleton thread (active socket). The received stub is casted to the remote interface that both
        //the server and client use as a mutual contract. When the client makes a remote call, the client stub takes care of the
        //marshaling of the arguments and sending the data to the associated skeleton.

        //The skeleton object implements the remote interface's methods. Each skeleton method unmarshalls the received arguments
        //and invokes the remote object's method, waits for the remote object to finish the required computation and returns the result,
        //or a thrown exception, and then marshals the result/exception thus sending it to the client stub.

        //The client stub receives the data from the skeleton, unmarshalls it and sends it to the application layer.

        Registry registry = LocateRegistry.getRegistry("localhost",7070);
        registry.rebind("Worker",skeleton); //adds the skeleton object to the RMI Registry

        System.out.println("Added skeleton object for WorkerImpl.java to RMI Registry located @ localhost:7070");
    }

}