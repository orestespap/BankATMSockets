import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import io.grpc.ServerBuilder;
import org.bson.Document;

public class Server {

    private static final int PORT = 9999;
    private static HashMap<String, ReentrantLock> userLocks =  new HashMap<String, ReentrantLock>();;

    public static void main(String[] args) throws InterruptedException, IOException {

        Database.connectToDatabase();

        MongoCollection<Document> clientsCollection= Database.getClients();
        FindIterable<Document> allUsers=clientsCollection.find();

        Iterator it= allUsers.iterator();
        while (it.hasNext()) {
            Document client = (Document) it.next();
            userLocks.put((String) client.get("username"), new ReentrantLock());
        }

        io.grpc.Server grpcServer = ServerBuilder.forPort(Server.PORT).
                addService(new ValidateCredentialsService()).
                addService(new GetAccountBalanceService()).
                addService(new DepositService()).
                addService(new WithdrawService(userLocks)).
                build();

        grpcServer.start();
        grpcServer.awaitTermination();

    }

}