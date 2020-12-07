import java.net.ServerSocket;
import java.net.Socket;
import com.mongodb.MongoClient;

public class Server {

    private static final int PORT = 9999;

    public static void main(String[] args) throws Exception{
        MongoClient mongoClient = new MongoClient("localhost", 27017);

        ServerSocket server=new ServerSocket(PORT);
        //passive socket exclusively listening for new connections

        System.out.println("Server is listening at port: "+PORT);

        while (true) {

            System.out.println("Main thread is waiting for clients ...");

            Socket anActiveSocket=server.accept();
            //active sockets handling communication with established connections
            //which corresponds to clients using the bank's ATM machines
            //each tcp connection is associated with a unique socket address
            //(remote address, remote port, local address, local port, protocol type: tcp)

            System.out.println("Client with address "+anActiveSocket.getInetAddress()+" has connected ...");

            ServerThread aWorker = new ServerThread(anActiveSocket);
            aWorker.start();
            if (1==3)
                break;
        }
        System.out.println("Server is shutting down gracefully ...");
        server.close();

    }
}