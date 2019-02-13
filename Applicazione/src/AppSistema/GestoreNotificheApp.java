package AppSistema;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import javax.swing.DefaultListModel;

import AppUtente.GestoreInterfaccia;
import GestoriTraffico.IRmiGestoreNotifiche;
import MainPackage.MainGUI;

public class GestoreNotificheApp extends UnicastRemoteObject implements IRmiApplicazione {
	
	private static final long serialVersionUID = 7787401541047824619L;
	
	private GestoreInterfaccia gInterfaccia;
	private IRmiGestoreNotifiche server;
	//TODO private IRmiGestoreClient clientManager;
	private ArrayList<Notifica> arrayNotifiche;
	private MainGUI gui;
	private int io;
	
	
	public GestoreNotificheApp() throws RemoteException {
		//TODO this.server = new IRmiGestoreNotifiche();
		//TODO this.clientManager = new IRmiGestoreClient();
		super();
		this.arrayNotifiche = new ArrayList<Notifica>();
	}
	
	public void setGInterfaccia(GestoreInterfaccia gi) {
		this.gInterfaccia = gi;
	}
	
	public void setServer(IRmiGestoreNotifiche s) {
		this.server=s;
	}
	
	public IRmiGestoreNotifiche getServer(){
		return this.server;
	}
	
	public void setClientManager() {
		
	}
	
	public void setGUI(MainGUI gui) {
		this.gui = gui;
	}
	
	public GestoreInterfaccia getGInterfaccia() { return this.gInterfaccia; }
	
	public MainGUI getGUI() { return this.gui; }
	
	public ArrayList<Notifica> getNotifiche() { return this.arrayNotifiche; }
	
	
	//TODO public IRmiGestoreNotifiche getServer() { return this.server; }
	
	
	//TODO public IRmiGestoreClient getClientManager() { return this.clientManager; }
	
	//implementazione dell'interfaccia
	
	public void addNotifica(String notifica) throws RemoteException{	
		
		//notifica ricevuta "compressa" come stringa con le info separate da /
		String[] tmp = notifica.split("/");
		
		Notifica n = new Notifica(tmp[1], tmp[0]);
		this.arrayNotifiche.add(n);
		
		//aggiorna lista notifiche (grafica)
		getGUI().getListaNotifiche().addElement(n.toString());   
		//mostra notifica push (grafica)
		getGInterfaccia().aggiungiNotificaPush(n); 
		
	}
	
	public void setIo(int indirizzo)
	{
		this.io=indirizzo;
	}
	
	public void ciao() throws RemoteException
	{
		System.out.println("Ciao, sono il server");
	}
	
	public void eliminaNotifica(int id) {
		this.arrayNotifiche.remove(id);
	}
	
	public void eliminaNotifiche() {
		this.arrayNotifiche.clear();
		getGUI().getListaNotifiche().clear();
	}
	
	public void rilevaNotifiche(String via){
		
		eliminaNotifiche(); //elimina le notifiche relative alla posizione precedente
		
		try {
			//quando cambia la posizione chiama l'rmi del sist centrale
			for (String el : this.server.rilevaNotifiche(via, this.io, 200)){
				
				addNotifica(el); //aggiunge le nuove notifiche
				
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
	}

	public void registraClient(String posizione) {}
	
}
