import java.rmi.RemoteException;

public interface WorkerInter extends java.rmi.Remote{
    boolean validateCredentials(String username, String password) throws RemoteException;
    double getAccountBalance(String username) throws RemoteException;
    String getClientName(String username) throws RemoteException;
    boolean withdraw(String username, int amount) throws RemoteException;
    boolean deposit(String username, int amount) throws RemoteException;
}
