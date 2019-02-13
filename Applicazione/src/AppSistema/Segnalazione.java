package AppSistema;

import java.rmi.RemoteException;
import java.sql.Timestamp;

import javax.swing.JOptionPane;

import Common.Traffico;
import GestoriTraffico.IRmiGestoreTraffico;

public class Segnalazione {
	
	private IRmiGestoreTraffico server;
	
	public Segnalazione() {
	}

	public void setServer(IRmiGestoreTraffico se) {
		this.server = se;
	}
	
	public IRmiGestoreTraffico getServer() { return this.server; }
	
	public void notify(Traffico t, String posizione, int importanza) {
		Timestamp orario = new Timestamp (System.currentTimeMillis());
		try {
			getServer().nuovoDato(posizione.substring(3), t.toString(), orario, importanza);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "Segnalazione inviata per la: " + posizione.substring(3));
	}
	
}
