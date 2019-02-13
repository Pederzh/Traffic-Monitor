package Centralina;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Timestamp;

public interface IRmiGestoreTrafficotmp extends Remote {

	public void nuovoDato( String via, Traffico stato, Timestamp orario, Mittente m ) throws RemoteException;
	
}
