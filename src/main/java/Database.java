import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Database {
    private static MongoCollection<Document> clientsCollection;

    public static void connectToDatabase(){
        MongoClientURI uri = new MongoClientURI("mongodb://");
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase bankDB = mongoClient.getDatabase("bankDB");
        clientsCollection = bankDB.getCollection("clients");
        System.out.println("Database connection established!");
    }

    public static MongoCollection<Document> getClients(){
        return clientsCollection;
    }
}


//Database schema (fields):
//username
//password
//firstName
//lastName
//phoneNumber
//email
//withdrawalsLog : { DDMMYYYY:[{hour: XX, minute: YY, second: ZZ, amount: INTEGER }, ... ], DDMMYYYY: [  ], ...}
//depositsLog : { DDMMYYYY:[{hour: XX, minute: YY, second: ZZ, amount: INTEGER }, ... ], DDMMYYYY: [  ], ...}
//loginsLog : { DDMMYYYY:[{hour: XX, minute: YYm second: ZZ}, ... ], DDMMYYYY: [  ], ...}
