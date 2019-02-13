package Centralina;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

import GestoriTraffico.IRmiGestoreTraffico;


//import GestoreTraffico;

public class CentralinaStradaleASensoreSingolo extends CentralinaStradale{
	
	private float velocitaGenerale;
	private float velocitaMedia; 	 // velocità media rilevata in funzione del numero delle auto passate
	private int nRilevazioni; 		 // numero rilevazioni	
	private Rilevazione rilevazione; // oggetto contenente inizio e fine della rilevazione di una vettura
	private boolean hoLavorato;
	private boolean possoRicevere;
	private IRmiGestoreTraffico server;

	/* COSTRUTTORE */
	public CentralinaStradaleASensoreSingolo(String nome, int limite, Tipologia tipo, SensoDiMarcia senso)
	{
		this.rilevazione=new Rilevazione();
		this.strada=new Via(nome, limite, tipo, senso, 0);
		this.possoRicevere=false;
	}
	
	public void setServer(IRmiGestoreTraffico s)
	{
		this.server=s;
		System.out.println("Server impostato");
	}
	
	public IRmiGestoreTraffico getServer()
	{
		return this.server;
	}
	
	/* METODO PER L'ATTIVAZIONE DELLA CENTRALINA */
	public void accendi()
	{
		this.traffico=Traffico.ASSENTE;
		this.possoRicevere=true;
		this.hoLavorato=false;
		this.velocitaMedia=0;
		this.nRilevazioni=0;
		this.velocitaGenerale=this.strada.getLimiteVelocita();
		setFrequenza(5);
		setPeriodo();
		costruttoreTimer();
	}
	
	
	
	/* METODO PER LO SPEGNIMENTO DELLA CENTRALINA */
	public void spegni()
	{
		killTimer();
		this.possoRicevere=false;
	}
	
	
	
	/* METODO PER IL CALCOLO DELLA VELOCITA DATI T1, T2 E LUNGHEZZA */
	public void calcolo()
	{
		long i1=this.rilevazione.getT1().getTime();
		long i2=this.rilevazione.getT2().getTime();
		float t=i2-i1;
		float v=(this.lunghMediaAuto*36)/(t*10);
		if (v>this.strada.getLimiteVelocita())
		{
			v=this.strada.getLimiteVelocita();
		}
		this.velocitaMedia+=v;
		this.nRilevazioni++;	
	}
	
	
	
	/* METODO PER IL RITORNO DEI RILEVATI */
	public int getRilevazioni()
	{
		return this.nRilevazioni;
	}
	
	
	/* METODO D'ANALISI DELLO STATO DEL TRAFFICO ESEGUITO OGNI MINUTO */
	public void analizzaTraffico()
	{
		if (this.rilevazione.getRilevato()==true)
		{
			long i1=this.rilevazione.getT1().getTime();
			long i2=getNow();
			float t=i1-i2;
			float v=(this.lunghMediaAuto*36)/(t*10);
			if (nRilevazioni==0 || v<this.velocitaMedia)
			{
				this.nRilevazioni++;
				this.velocitaMedia+=v;
			}
		}
		if (this.nRilevazioni!=0)
		{
			this.hoLavorato=true;
			float vpm=this.velocitaMedia*100/this.strada.getLimiteVelocita();  // velocità media in percentuale
			float vgm=this.velocitaGenerale*100/this.strada.getLimiteVelocita(); // velocità generale in percentuale
			if ((vgm-vpm)>50)
			{
				this.velocitaGenerale=(this.velocitaGenerale+this.velocitaMedia)/2;
			}
			else if ((vgm-vpm)>25)
			{
				this.velocitaGenerale=this.velocitaGenerale*25/100+this.velocitaMedia*75/100;
			}
			else if ((vgm-vpm)>10)
			{
				this.velocitaGenerale=this.velocitaGenerale*10/100+this.velocitaMedia*90/100;
			}
			else if ((vgm-vpm)>0)
			{
				this.velocitaGenerale=this.velocitaMedia;
			}
			if ((vgm-vpm)>-10)
			{
				this.velocitaGenerale=this.velocitaMedia;
			}
			else if ((vgm-vpm)>-25)
			{
				this.velocitaGenerale=this.velocitaGenerale*10/100+this.velocitaMedia*90/100;
			}
			else if ((vgm-vpm)>-50)
			{
				this.velocitaGenerale=this.velocitaGenerale*25/100+this.velocitaMedia*75/100;
			}
			else
			{
				this.velocitaGenerale=(this.velocitaGenerale+this.velocitaMedia)/2;
			}
			vgm=this.velocitaGenerale*100/this.strada.getLimiteVelocita();
			if (vgm>95) 
			{
				setFrequenza(14);
				this.traffico=Traffico.ASSENTE;
			}
			else if (vgm>85)
			{
				setFrequenza(8);
				this.traffico=Traffico.SCORREVOLE;
			}
			else if (vgm>60)
			{
				setFrequenza(4);
				this.traffico=Traffico.MEDIO;
			}
			else if (vgm>20)
			{
				setFrequenza(2);
				this.traffico=Traffico.ELEVATO;
			}
			else
			{
				setFrequenza(1);
				this.traffico=Traffico.BLOCCATO;
			}
		}
		else
		{
			setFrequenza(14);
			this.traffico=Traffico.ASSENTE;
			hoLavorato=true;
		}
		// notifica ogni frequenza*minuto
		if (getNow()-this.periodo.getTime()>=this.frequenza*10000) {
			System.out.println(this.traffico);
			if (this.hoLavorato==true)
			{
				spedizione();
				this.hoLavorato=false;
			}
			setPeriodo();
		}
		this.velocitaMedia=0;
		this.nRilevazioni=0;
	}
	
	
	
	/* METODO PER LA SPEDIZIONE DEL DATO AL SISTEMA CENTRALE */
	public void spedizione()
	{
		String messaggio = new String();
		if (traffico==Traffico.BLOCCATO) messaggio="BLOCCATO";
		else if (traffico==Traffico.ELEVATO) messaggio="ELEVATO";
		else if (traffico==Traffico.MEDIO) messaggio="MEDIO";
		else if (traffico==Traffico.SCORREVOLE) messaggio="SCORREVOLE";
		else if (traffico==Traffico.ASSENTE) messaggio="ASSENTE";
		try {
			this.server.nuovoDato(this.strada.getNome(), messaggio, getNowTimestamp(), 4);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/* METODO DELL'INTERFACCIA PER L'INSERIMENTO DI UNA NUOVA RILEVAZIONE */
	public void nuovaRilevazione(Timestamp orario, ID momento)
	{
		if (this.possoRicevere==true)
		{
			if (momento==ID.A)
			{
				this.rilevazione.setT1(orario);
				this.rilevazione.setRilevato(true);
			}
			else
			{
				this.rilevazione.setT2(orario);
				this.rilevazione.setRilevato(false);
				calcolo();
			}
		}
	}
	
	
	
	/* MTODO DI OTTENIMENTO DELL'ORARIO ATTUALE FORMATO LONG */
	public long getNow()
	{
		Timestamp t=new Timestamp (System.currentTimeMillis());
		return t.getTime();
	}
	
	
	/* MTODO DI OTTENIMENTO DELL'ORARIO ATTUALE FORMATO TIMESTAMP */
	public Timestamp getNowTimestamp()
	{
		return new Timestamp (System.currentTimeMillis());
	}
	
	
	
	/* GETTER VELOCITA' MEDIA */
	public Float getVMedia()
	{
		return this.velocitaMedia;
	}
	
	
}

