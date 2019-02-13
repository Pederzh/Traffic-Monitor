package GestoriTraffico;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IRmiGestoreNotifiche extends Remote{

	public ArrayList<String> rilevaNotifiche(String posizione, int provenienza, int distanza) throws RemoteException;
	
	
	public void registraClient(int tmp)throws RemoteException;
	
	
	public void ciao(int tmp) throws RemoteException;
	
}
