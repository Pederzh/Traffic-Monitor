package AppSistema;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IRmiApplicazione extends Remote{
	
	public void addNotifica(String notifica) throws RemoteException;
	
	
	public void ciao() throws RemoteException;
}
