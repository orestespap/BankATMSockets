import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import io.grpc.stub.StreamObserver;
import org.bankatm.grpc.validateCredentialsRequest;
import org.bankatm.grpc.validateCredentialsResponse;
import org.bankatm.grpc.validateCredentialsServiceGrpc;
import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;

import java.time.LocalDateTime;

public class ValidateCredentialsService extends validateCredentialsServiceGrpc.validateCredentialsServiceImplBase {

    private MongoCollection<Document> clientsCollection;

    public ValidateCredentialsService() {
        this.clientsCollection= Database.getClients();
    }

    @Override
    public void validateCredentials(validateCredentialsRequest request, StreamObserver<validateCredentialsResponse> responseObserver) {

        String username = request.getUsername();
        String password= request.getPassword();

        Document client = this.clientsCollection.find(new Document("username", username)).first();
        boolean result = false;

        try{
            result=client.get("password").equals(password);
        }
        catch (NullPointerException e){
            //client object is null pointer --> mongo returned zero results
            System.out.println("Username '"+username+"' not found in the database.");
        }

        String clientName="0";

        if (result) {
            //log successful log-in attempt

            Document logLogin = new Document().append("hour",
                    LocalDateTime.now().getHour()).append("minute",
                    LocalDateTime.now().getMinute()).append("second",
                    LocalDateTime.now().getSecond());

            String subDocument = java.time.LocalDateTime.now().getDayOfMonth() + "" + java.time.LocalDateTime.now().getMonthValue() + "" + java.time.LocalDateTime.now().getYear();

            clientsCollection.updateOne(eq("username", client.get("username")), Updates.addToSet("loginsLog." + subDocument, logLogin));
            clientName = client.get("firstName").toString()+" "+client.get("lastName");
        }

        validateCredentialsResponse response = validateCredentialsResponse.newBuilder().setResult(result).setClientName(clientName).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


}