import com.mongodb.client.MongoCollection;
import io.grpc.stub.StreamObserver;
import org.bankatm.grpc.getAccountBalanceRequest;
import org.bankatm.grpc.getAccountBalanceResponse;
import org.bankatm.grpc.getAccountBalanceServiceGrpc;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class GetAccountBalanceService extends getAccountBalanceServiceGrpc.getAccountBalanceServiceImplBase{

    private MongoCollection<Document> clientsCollection;

    public GetAccountBalanceService() {
        this.clientsCollection= Database.getClients();
    }

    @Override
    public void getAccountBalance(getAccountBalanceRequest request, StreamObserver<getAccountBalanceResponse> responseObserver) {
        String username = request.getUsername();
        Document client = this.clientsCollection.find(new Document("username", username)).first();

        double accountBalance = (double) this.clientsCollection.find(eq("username", client.get("username"))).first().get("balance");
        //System.out.println(accountBalance);
        getAccountBalanceResponse response = getAccountBalanceResponse.newBuilder().setAccountBalance(accountBalance).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
