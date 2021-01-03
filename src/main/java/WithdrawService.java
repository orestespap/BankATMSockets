import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import io.grpc.stub.StreamObserver;
import org.bankatm.grpc.withdrawRequest;
import org.bankatm.grpc.withdrawResponse;
import org.bankatm.grpc.withdrawServiceGrpc;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.inc;

public class WithdrawService extends withdrawServiceGrpc.withdrawServiceImplBase {

    private static final int maxWith=2000; //maximum daily withdrawal limit
    private MongoCollection<Document> clientsCollection;
    private static HashMap<String, ReentrantLock> userLocks;

    public WithdrawService(HashMap<String, ReentrantLock> locks){
        this.clientsCollection= Database.getClients();
        WithdrawService.userLocks=locks;
    }

    @Override
    public void withdraw(withdrawRequest request, StreamObserver<withdrawResponse> responseObserver) {
        String username=request.getUsername();
        ReentrantLock userLock = WithdrawService.userLocks.get(username);
        userLock.lock();

        int amount=request.getAmount();
        Document client;

        client = this.clientsCollection.find(eq("username", username)).first();

        boolean transactionSuccess = false;

        try {
            if(amount<=(double)client.get("balance") && amount<=maxWith) {
                String subDocument = java.time.LocalDateTime.now().getDayOfMonth() + "" + java.time.LocalDateTime.now().getMonthValue() + "" + java.time.LocalDateTime.now().getYear();
                ArrayList<Object> withdrawalsLog = (ArrayList<Object>) ((Document) client.get("withdrawalsLog")).get(subDocument);
                int sum = 0;
                if (withdrawalsLog != null) // -> mongo returns 0 results because the client has not made any withdrawals on the given date
                    for (int i = 0; i < withdrawalsLog.size(); i++) {
                        Document transaction = (Document) withdrawalsLog.get(i);
                        sum += (int) transaction.get("amount");
                    }

                //System.out.println(sum); //for testing purposes

                if (amount + sum <= maxWith) {

                    Bson incrementBalance = inc("balance", -amount);
                    clientsCollection.updateOne(eq("username", username), incrementBalance);

                    Document logTransaction = new Document().append("hour",
                            java.time.LocalDateTime.now().getHour()).append("minute",
                            java.time.LocalDateTime.now().getMinute()).append("second",
                            java.time.LocalDateTime.now().getSecond()).append("amount",
                            amount);

                    clientsCollection.updateOne(eq("username", username), Updates.addToSet("withdrawalsLog." + subDocument, logTransaction));

                    transactionSuccess = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            transactionSuccess=false;
        } finally {
            userLock.unlock();
            withdrawResponse response = withdrawResponse.newBuilder().setResult(transactionSuccess).build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }

    }
}
