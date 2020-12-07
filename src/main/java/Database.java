import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Database {
    private static MongoDatabase bankDB;
    private static MongoCollection<Document> clientsCollection;

    public static void connectToDatabase(){
        MongoClientURI uri = new MongoClientURI("mongodb://atm:#1821_1951!1997@145.14.157.133:27017/?authSource=admin");
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase bankDB = mongoClient.getDatabase("bankDB");
        clientsCollection = bankDB.getCollection("clients");
        System.out.println("Database connection established!");
    }

    public static MongoCollection<Document> getClients(){
        return clientsCollection;
    }

    public static void getWithdrawalLock(){

    }
}
