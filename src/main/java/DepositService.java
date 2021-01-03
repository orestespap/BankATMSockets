import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import io.grpc.stub.StreamObserver;
import org.bankatm.grpc.depositRequest;
import org.bankatm.grpc.depositResponse;
import org.bankatm.grpc.depositServiceGrpc;
import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.inc;

public class DepositService extends depositServiceGrpc.depositServiceImplBase{

    private MongoCollection<Document> clientsCollection;

    public DepositService() {
        this.clientsCollection= Database.getClients();
    }

    @Override
    public void deposit(depositRequest request, StreamObserver<depositResponse> responseObserver) {
        boolean success=false;
        try{
            String username = request.getUsername();
            Document client = this.clientsCollection.find(new Document("username", username)).first();
            Bson incrementBalance = inc("balance", request.getAmount());
            clientsCollection.updateOne(eq("username", client.get("username")),incrementBalance);

            String subDocument=java.time.LocalDateTime.now().getDayOfMonth()+""+java.time.LocalDateTime.now().getMonthValue()+""+java.time.LocalDateTime.now().getYear();

            Document logTransaction = new Document().append("hour",
                    java.time.LocalDateTime.now().getHour()).append("minute",
                    java.time.LocalDateTime.now().getMinute()).append("second",
                    java.time.LocalDateTime.now().getSecond()).append("amount",
                    request.getAmount());

            clientsCollection.updateOne(eq("username", client.get("username")), Updates.addToSet("depositsLog."+subDocument,logTransaction));
            success=true;
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

        depositResponse response = depositResponse.newBuilder().setResult(success).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
