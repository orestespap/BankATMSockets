import java.rmi.RemoteException;

public interface WorkerInter extends java.rmi.Remote{
    boolean validateCredentials(String username, String password) throws RemoteException;
    double getAccountBalance() throws RemoteException;
    String getClientName() throws RemoteException;
    boolean withdraw(int amount) throws RemoteException;
    boolean deposit(int amount) throws RemoteException;
}
