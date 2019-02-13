package GestoriTraffico;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Timestamp;

import DatiTraffico.Mittente;
import DatiTraffico.Traffico;

public interface IRmiGestoreTraffico extends Remote {

	public void nuovoDato( String via, String traffico, Timestamp orario, int m ) throws RemoteException;
	
	public void ciao() throws RemoteException;
	
}