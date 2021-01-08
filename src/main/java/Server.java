import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.rabbitmq.client.*;
import org.bson.Document;

public class Server {

    private static HashMap<String, ReentrantLock> userLocks =  new HashMap<String, ReentrantLock>();
    private static String mainQueueName = "connectionQueue";

    public static void startServer() throws IOException, TimeoutException {

        Database.connectToDatabase();
        Services.setUpService();

        MongoCollection<Document> clientsCollection= Database.getClients();
        FindIterable<Document> allUsers=clientsCollection.find();

        Iterator it= allUsers.iterator();


        while (it.hasNext()) {
            Document client = (Document) it.next();
            userLocks.put((String) client.get("username"), new ReentrantLock());
        }

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");


        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(mainQueueName, false, false, false, null);
        channel.queuePurge(mainQueueName);

        channel.basicQos(1);

        Serializer ser = new Serializer();
        Object monitor = new Object();
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(delivery.getProperties().getCorrelationId())
                    .build();

            Object result= null;

            try {
                byte[] byteArray = delivery.getBody();

                ObjectInputStream is= ser.deserialize(byteArray);

               ;

                try {

                    HashMap<String, Object> request = (HashMap<String, Object>) is.readObject();

                    while (true) {
                        if (request.keySet().contains("validateCredentials")) {
                            ArrayList<Object> items = (ArrayList<Object>) request.get("validateCredentials");
                            result = Services.validateCredentials((String) items.get(0), (String) items.get(1));
                            break;
                        }
                        if (request.keySet().contains("getBalance")) {
                            result = Services.getAccountBalance((String) request.get("getBalance")).toString();
                            break;
                        }
                        if (request.keySet().contains("deposit")) {
                            ArrayList<Object> items = (ArrayList<Object>) request.get("deposit");
                            result = Services.deposit((String) items.get(0), (int) items.get(1));
                            break;
                        }
                        if (request.keySet().contains("withdraw")) {
                            ArrayList<Object> items = (ArrayList<Object>) request.get("withdraw");
                            result = Services.withdraw((String) items.get(0), (int) items.get(1));
                            break;
                        }
                        if (request.keySet().contains("getName")) {
                            result = Services.getClientName((String) request.get("getName"));
                            break;
                        }
                    }

                } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                }
                catch (Exception e){
                    e.printStackTrace();
                }


            } catch (RuntimeException e) {
                try {
                    ser.closeConnections();
                }
                catch (NullPointerException e2){
                    //e2.printStackTrace();
                }

                System.out.println(" [.] " + e.toString());
            } finally {
                channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, result.toString().getBytes(StandardCharsets.UTF_8));
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                // RabbitMq consumer worker thread notifies the RPC server owner thread
                synchronized (monitor) {
                    monitor.notify();
                }
            }
        };
        channel.basicConsume(mainQueueName, false, deliverCallback, (consumerTag -> { }));

        System.out.println("RabbitMQ rpc server is up.\nListening for client requests ...");

    }

    public static ReentrantLock getUserLock(String username){
        return userLocks.get(username);
    }

}