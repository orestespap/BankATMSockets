import java.io.DataOutputStream;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerThread extends Thread{

    private Socket activeSocket;
    private static final String EXIT="EXIT";
    private static final int timeOutInMS=150000;
    private Object response;

    public ServerThread(Socket aSocket) throws IOException {
        this.activeSocket=aSocket;
    }


    public void run(){


        try {
            this.activeSocket.setSoTimeout(timeOutInMS);
            ObjectInputStream inputStream=new ObjectInputStream(this.activeSocket.getInputStream()); //data input stream from client
            DataOutputStream dout=new DataOutputStream(this.activeSocket.getOutputStream()); //data output stream to client.

            while (true){
                HashMap<String, Object> request= (HashMap<String, Object>) inputStream.readObject();

            }

        }
        catch (SocketException socketException) {
            socketException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private boolean validateCredentials(String username, String password){


    }

    private float getAccountBalance(String username){


    }

    private String getName(String username){


    }

    private boolean withdraw(int amount){


    }

    private boolean deposit(int amount){



    }


}