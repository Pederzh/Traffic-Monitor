package GestoriTraffico;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.*;

import DatiTraffico.*;
import MainSistemaCentrale.GraficaSistemaCentrale;

public class GestoreTraffico extends UnicastRemoteObject implements IRmiGestoreTraffico{

	private static final long serialVersionUID = 7787401541047824619L;
	
	private Map<String ,ArrayList<DatoCompleto>> mappaDati;
	private Map<String ,StatoTrafficoPeriodico> mappaTraffico;
	private Map<String ,StatoTraffico> mappaTrafficoElaborato;
	private GestoreClient gestoreClient;
	private GraficaSistemaCentrale grafica;
	
	
	
	/* COSTRUTTORE */
 	public GestoreTraffico () throws RemoteException
 	{
 		super();
		this.mappaDati=new HashMap<String, ArrayList<DatoCompleto>>();
		this.mappaTraffico=new HashMap<String, StatoTrafficoPeriodico>();
		this.mappaTrafficoElaborato=new HashMap<String, StatoTraffico>();
		this.grafica=new GraficaSistemaCentrale();
 	}
	
 	
 	
 	/* SETTER GRAFICA */
 	public void setGrafica(GraficaSistemaCentrale finestra)
 	{
 		this.grafica=finestra;
 	}
 	
 	/* SETTER PER IL GESTORE CLIENT */
	public void setGestoreClient(GestoreClient g)
	{
		this.gestoreClient=g;
	}
	
	
	
	public void ciao() throws RemoteException
	{
		System.out.println("ciao sono la centralina");
	}
	
	/* METODO PER LA GESTIONE DEI DATI IN INGRESSO */
	public void nuovoDato( String via, String statoTraffico, Timestamp orario, int newMittente ) throws RemoteException
	{
		Mittente mittente = null;
		Traffico stato = null;
		if (statoTraffico.equals("BLOCCATO"))
		{
			stato=Traffico.BLOCCATO;
		}
		else if (statoTraffico.equals("ELEVATO"))
		{
			stato=Traffico.ELEVATO;
		}
		else if (statoTraffico.equals("MEDIO"))
		{
			stato=Traffico.MEDIO;
		}
		else if (statoTraffico.equals("SCORREVOLE"))
		{
			stato=Traffico.SCORREVOLE;
		}
		else if (statoTraffico.equals("ASSENTE"))
		{
			stato=Traffico.ASSENTE;
		}
		if (newMittente==1)
		{
			mittente=Mittente.APPLICAZIONE;
		}
		else if (newMittente==4)
		{
			mittente=Mittente.CENTRALINASTRADALE;
		}
		DatoCompleto nuovoDato= new DatoCompleto(orario, via, stato, mittente);
		ArrayList<DatoCompleto> lista;
		// AGGIUNGE LA CHIAVE (VIA) ALLA HASH MAP, SE ASSENTE 							
		if ( mappaDati.containsKey(via) == false ) 
		{
			mappaDati.putIfAbsent(via, new ArrayList<DatoCompleto>());
		}
		lista = mappaDati.get(via);
		// ELABORA SOLO SE LISTA VUOTA
		if (lista.isEmpty() == true) 
		{
			System.out.println("dato in calcolo");
			lista.add( nuovoDato );
			// ELABORA FINO A QUANDO LA LISTA NON è VUOTA
			while(lista.isEmpty() == false) 
			{
				analizzaTraffico( nuovoDato );
				nuovoDato=lista.get(0);
				lista.remove(0);
				nuovoDato.kill();
			}
		}
		// SE LISTA!=VUOTA ACCODA IL DATO
		else 
		{
			System.out.println("dato accodato");
			lista.add(nuovoDato);
		}
	}
	
	
	
	/* METODO PER L'ANALISI DELLA VARIAZIONE NELLO STATO DEL TRAFFICO */
	private void analizzaTraffico(DatoCompleto d)
	{	
		String via= d.getPosizione();
		StatoTrafficoPeriodico trafficoNew;
		boolean nuovoDato=false;
		//viene prelevato il traffico da hash map
		if (mappaTraffico.containsKey(via)==true) 
		{
			trafficoNew=mappaTraffico.get(via);
		}
		else 
		{
			nuovoDato=true;
			trafficoNew=new StatoTrafficoPeriodico();
			trafficoNew.setDato(d);
			mappaTraffico.putIfAbsent(via, trafficoNew);
		}
		// la correttezza viene ridotta di 1 ogni 10 minuti
		trafficoNew.setOrario(d.getOrario());
		Calendar cOld=Calendar.getInstance();
		Calendar cNew=Calendar.getInstance();
		cOld=trafficoNew.getPrimoOrario();
		cNew.setTime(trafficoNew.getOrario().getTime());
		cNew.set(Calendar.MINUTE, cNew.get(Calendar.MINUTE)-10);
		while(cOld.compareTo(cNew)<0 && trafficoNew.getCorrettezza()>0)
		{
			//System.out.println(trafficoNew.getCorrettezza());
			trafficoNew.diminuisciCorrettezza();
			cNew.set(Calendar.MINUTE, cNew.get(Calendar.MINUTE)-10);
		}
		// vengono impostati i nuovi valori dello stato del traffico
		if (trafficoNew.getTraffico()==d.getTraffico()) 
		{
			trafficoNew.aumentaCorrettezza(d.getMittente());
		}
		else 
		{
			trafficoNew.diminuisciCorrettezza(d.getMittente(), d.getTraffico());	
		}
		// si verifica se notificare solo se la correttezza è >= 2
		if (trafficoNew.getCorrettezza()>=1 || nuovoDato==true)
		{
			StatoTraffico trafficoOld= new StatoTraffico();
			if (mappaTrafficoElaborato.containsKey(via)==true)
			{
				trafficoOld=mappaTrafficoElaborato.get(via);
			}
			else
			{
				trafficoOld=null;
			}
			trafficoNew.modulazioneOra();
			// se non sono stati registrati dati elaborati sul traffico
			if (trafficoOld==null) 
			{
				if (trafficoNew.getTraffico()==Traffico.ELEVATO) 
				{
					gestoreClient.notificaClient(Evento.AUMENTATOELEVATO, via);
				}
				else if (trafficoNew.getTraffico()==Traffico.BLOCCATO) 
				{
					gestoreClient.notificaClient(Evento.AUMENTATOBLOCCATO, via);
				}
				else if (trafficoNew.getTraffico()==Traffico.MEDIO) 
				{
					gestoreClient.notificaClient(Evento.AUMENTATOMEDIO,  via);
				}
			}
			// ricerca l'esistenza di un evento comparando old con new
			else
			{
				Evento evento=rilevaCambiamentoTraffico(trafficoOld, trafficoNew);
				//notifica l'evento
				if (evento!=null) 
				{
					gestoreClient.notificaClient(evento, via);
				}
			}
			// memorizza il dato su mappaTrafficoElaborato sostituendo orario con primoOrario
			mappaTrafficoElaborato.remove(via);
			trafficoOld=new StatoTraffico();
			trafficoOld.setCorrettezza(trafficoNew.getCorrettezza());
			trafficoOld.setOrarioCalendar(trafficoNew.getPrimoOrario());
			trafficoOld.setTraffico(trafficoNew.getTraffico());
			mappaTrafficoElaborato.put(via, trafficoOld);
			this.grafica.ridisegna();
		}	
	}
	
	
	
	/* METODO PER LA RILEVAZIONE DEGLI EVENTI */
	public Evento rilevaCambiamentoTraffico(StatoTraffico t1, StatoTraffico t2) 
	{
		if (t1.getTraffico()==t2.getTraffico()) {
			if (t1.getTraffico()==Traffico.ASSENTE || 
				t1.getTraffico()==Traffico.SCORREVOLE ||
				t1.getTraffico()==Traffico.MEDIO) 
			{
				return null;
			}
			else if (t1.getTraffico()==Traffico.ELEVATO)
			{
				return Evento.RIMASTOELEVATO;
			}
			else {
				return Evento.RIMASTOBLOCCATO;
			}
		}
		else 
		{
			// casi in cui il traffico è aumentato
			if (t2.getTraffico()==Traffico.BLOCCATO) 
			{
				return Evento.AUMENTATOBLOCCATO;
			}
			if (t2.getTraffico()==Traffico.ELEVATO && 
				t1.getTraffico()!=Traffico.BLOCCATO) 
			{
				return Evento.AUMENTATOELEVATO;
			}
			if (t2.getTraffico()==Traffico.MEDIO &&
				t1.getTraffico()!=Traffico.BLOCCATO &&
				t1.getTraffico()!=Traffico.ELEVATO) 
			{
				return Evento.AUMENTATOMEDIO;
			}
			// casi in cui il  traffico è diminuito
			if (t1.getTraffico()!=Traffico.ASSENTE && t1.getTraffico()!=Traffico.SCORREVOLE) 
			{
				if (t2.getTraffico()==Traffico.ASSENTE) 
				{
					return Evento.CALATOASSENTE;
				}
				if (t2.getTraffico()==Traffico.SCORREVOLE)
				{
					return Evento.CALATOSCORREVOLE;
				}
				if (t2.getTraffico()==Traffico.MEDIO)
				{
					return Evento.CALATOMEDIO;
				}
				if (t2.getTraffico()==Traffico.ELEVATO &&
					t1.getTraffico()!=Traffico.MEDIO) 
				{
					return Evento.AUMENTATOMEDIO;
				}
			}
		}
		return null;
	}
	
	
	
	/* METODO PER IL PRELIEVO DEL TRAFFICO IN UNA VIA */
	public StatoTraffico getTraffico(String via) 
	{
		if (mappaTrafficoElaborato.containsKey(via)==false) return null;
		return mappaTrafficoElaborato.get(via);
	}

}

