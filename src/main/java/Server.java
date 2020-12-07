import java.net.ServerSocket;
import java.net.Socket;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.xml.crypto.Data;

public class Server {

    private static final int PORT = 9999;

    public static void main(String[] args) throws Exception{

        ServerSocket server=new ServerSocket(PORT);
        //passive socket exclusively listening for new connection
        System.out.println("Server is listening at port: "+PORT);
        Database.connectToDatabase();

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
            if (1==4)
                break;
        }
        System.out.println("Server is shutting down gracefully ...");
        server.close();

    }
}