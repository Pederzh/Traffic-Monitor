package GestoriTraffico;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import DatiTraffico.DatoCompleto;
import DatiTraffico.Evento;

public class GestoreClient {

	private Map<Integer,DatiClient> mappaClient;
	private ArrayList<Integer> listaClient;
	
	
	/* COSTRUTTORE */
	public GestoreClient()
	{
		this.mappaClient=new HashMap<Integer, DatiClient>();
		listaClient= new ArrayList<Integer>();
	}
	
	
	
	public void notificaClient(Evento evento, String via) {
		String commento=null;
		for (int i=0; i< listaClient.size(); i++)
		{
			// se un'applicazione si trova nella via o le è stata notificata la via
			if (mappaClient.get(listaClient.get(i)).getPosizione()==via || 
				mappaClient.get(listaClient.get(i)).getVie().contains(via))
			{
				// generazione testo
				if (evento==Evento.AUMENTATOBLOCCATO) commento="BLOCCATO! ";
				else if (evento==Evento.AUMENTATOELEVATO) commento="ELEVATO! ";
				else if (evento==Evento.AUMENTATOMEDIO) commento="medio ";
				else if (evento==Evento.CALATOMEDIO) commento="calato a medio! ";
				else if (evento==Evento.CALATOELEVATO) commento="calato ad elevato ";
				else if (evento==Evento.CALATOSCORREVOLE) commento="calato a scorrevole ";
				else if (evento==Evento.CALATOASSENTE) commento="calato ad assente ";
				else if (evento==Evento.RIMASTOELEVATO) commento="COSTANTEMENTE ELEVATO ";
				else if (evento==Evento.RIMASTOBLOCCATO) commento="COSTANTEMENTE BLOCCATO";
				String notifica= commento + "/" + via;
				try {
					mappaClient.get(listaClient.get(i)).getApplicazione().addNotifica(notifica);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	
	
	public Map<Integer,DatiClient> getMappaClient(){
		return this.mappaClient;
	}
	
	
	
	public ArrayList<Integer> getListaClient(){
		return this.listaClient;
	}
	
	
	
	/* METODO PER IL RITORNO DI UN CLIENT */
	public DatiClient getClient(int nome) {
		if (this.mappaClient.containsKey(nome)==true)
		{
			return this.mappaClient.get(nome);
		}
		else
		{
			return null;
		}
	}
	
	
	
	
}


