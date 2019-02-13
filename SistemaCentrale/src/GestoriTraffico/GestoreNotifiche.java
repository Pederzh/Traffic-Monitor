package GestoriTraffico;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.*;

import DatiTraffico.DatoCompleto;
import DatiTraffico.StatoTraffico;
import DatiTraffico.StatoTrafficoPeriodico;
import DatiTraffico.Traffico;

import AppSistema.IRmiApplicazione;

public class GestoreNotifiche extends UnicastRemoteObject implements IRmiGestoreNotifiche{
	
	private static final long serialVersionUID = 7787401541047824619L;
	
	private Map<String ,ArrayList<String>> segmentiAdiacenti;
	private Map<String ,Segmento> infoSegmento;
	private GestoreClient gestoreClient;
	private GestoreTraffico gestoreTraffico;

	
	
	/* COSTRUTTORE */
	public GestoreNotifiche() throws RemoteException
	{
		super();
		this.segmentiAdiacenti=new HashMap<String, ArrayList<String>>();
		// costruzione segmentiAdiacenti tramite file
		this.infoSegmento=new HashMap<String, Segmento>();
		// costruzione infoSegmento tramite file
		letturaMappa();
	}
	
	public void ciao(int num) throws RemoteException
	{
		System.out.println("ciao sono l'app numero: " + num);
	}
	
	
	
	/* SETTER GESTORE CLIENT */
	public void setGestoreClient(GestoreClient g) 
	{
		this.gestoreClient=g;
	}
	
	
	
	/* SETTER GESTORE TRAFFICO */
	public void setGestoreTraffico(GestoreTraffico g) 
	{
		this.gestoreTraffico=g;
	}
	
	
	
	/* METODO PER LA REGISTRAZIONE DI UN CLIENT */
	public void registraClient(int tmp)throws RemoteException
	{
		try {
			Registry registry = LocateRegistry.getRegistry("127.0.0.1", 12346+tmp);
			IRmiApplicazione serverApp = (IRmiApplicazione) registry.lookup("Applicazione");
			DatiClient dc= new DatiClient();
			dc.setApplicazione(serverApp);
			if (gestoreClient.getMappaClient().containsKey(tmp))
			{
				// si elimina il client se ne viene rilevato uno nuovo nella sua porta
				gestoreClient.getMappaClient().get(tmp).kill();
				gestoreClient.getMappaClient().remove(tmp);
				gestoreClient.getMappaClient().put(tmp, dc);
			}
			else
			{
				// si registra il client
				gestoreClient.getMappaClient().put(tmp, dc);
				gestoreClient.getListaClient().add(tmp);
			}
			serverApp.ciao();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	
	
	/* METODO PER L'INSERIMENTO DI UN NUOVO SEGMENTO (TESTING)*/
	public void addSegmento(String nome, int lng, ArrayList<String> adiacenti) 
	{
		if (segmentiAdiacenti.containsKey(nome)==false) {
			ArrayList<String> lista= new ArrayList<String>();
			Segmento tmp=new Segmento(nome, lng);
			infoSegmento.putIfAbsent(nome, tmp);
			segmentiAdiacenti.putIfAbsent(nome, lista);
			if (adiacenti != null) {
				for (int i=0; i<adiacenti.size(); i++) {
					lista.add(adiacenti.get(i));
					segmentiAdiacenti.get(adiacenti.get(i)).add(nome);
				}
			}
		}
	}
	
	
	
	/* METODO DI STAMPA DEI SEGMENTI ADIACENTI (PER IL TESTING DEL CODICE) */
	public void stampa(String nome) {
		for (int i=0; i<segmentiAdiacenti.get(nome).size(); i++)
		{
			System.out.println(segmentiAdiacenti.get(nome).get(i)+ " - ");
		}
	}
	
	
	
	/* METODO PER LA SELEZIONE DELLE VIE E DEGLI STATI DEL TRAFFICO */
	public ArrayList<String> rilevaNotifiche(String posizione, int provenienza, int distanza) throws RemoteException
	{
		ArrayList<String> notifiche=new ArrayList<String>();
		ArrayList<String> listaSegmenti= new ArrayList<String>();
		ArrayList<String> listaVie= new ArrayList<String>();
		// le liste adiacenti vengono posizionate in listaSegmenti
		rilevaVieDintorni(posizione, distanza, listaSegmenti);
		// listaVie contiene i segmenti adiacenti alla posizione attuale
		DatiClient client = this.gestoreClient.getClient(provenienza);
		if (client!= null) client.setPosizione(posizione);
		// si convertono i segmenti in vie
		for (int i=0; i<listaSegmenti.size(); i++)
		{
			if (listaVie.contains(this.infoSegmento.get(listaSegmenti.get(i)).getVia())==false)
			{
				listaVie.add(this.infoSegmento.get(listaSegmenti.get(i)).getVia());
			}
		}
		listaSegmenti.clear();
		StatoTraffico traffico;
		Timestamp t1 = new Timestamp (System.currentTimeMillis());
		Calendar orario=Calendar.getInstance();
		orario.setTime(t1);
		// si creano le notifiche solo per le vie non presenti tra quelle già notificate
		ArrayList<String> oldListaVie;
		if (client == null)
		{
			oldListaVie= new ArrayList<String>();
		}
		else
		{
			oldListaVie=client.getVie();
		}
		for(int i=0; i<listaVie.size(); i++)
		{
			if (oldListaVie.contains(listaVie.get(i))==false)
			{
				traffico=this.gestoreTraffico.getTraffico(listaVie.get(i));
				// se è stato effettuata una rilevazione del traffico sulla via si procede
				if (traffico != null) 
				{
					if (
					traffico.getTraffico()==Traffico.BLOCCATO ||
					traffico.getTraffico()==Traffico.ELEVATO ||
					traffico.getTraffico()==Traffico.MEDIO)
					{
						//analizza ed elabora una notifica
						notifiche.add(analizzaNotifica(traffico,orario)+"/"+listaVie.get(i));
					}	
				}
			}
		}
		if (client!=null) client.clearVie();
		if (client!=null) client.setVie(listaVie);
		return notifiche;
	}
	
	
	
	/* METODO PER LA RICERCA DELLE VIE ADIACENTI */
	public void rilevaVieDintorni(String segmento, int distanza, ArrayList<String> vecchie)
	{
		// l'array di ritorno è l'array parametro
		ArrayList<String> adiac=segmentiAdiacenti.get(segmento);
		for (int i=0; i<adiac.size(); i++) 
		{
			if (vecchie.contains(adiac.get(i))==false && adiac.get(i)!= segmento) {
				vecchie.add(adiac.get(i));
				if (infoSegmento.get(adiac.get(i)).getLunghezza()<distanza) 
				{
					rilevaVieDintorni(adiac.get(i), distanza-infoSegmento.get(adiac.get(i)).getLunghezza(), vecchie);
				}
			}
		}
	}
	
	
	
	/* METODO PER L'ELABORAZIONE DEL TESTO DELLA NOTIFICA */
	public String analizzaNotifica(StatoTraffico traffico, Calendar orario)
	{
		String messaggio=new String();
		Calendar cOld=Calendar.getInstance();
		Calendar cNew=Calendar.getInstance();
		int correttezza=traffico.getCorrettezza();
		cOld.setTime(traffico.getOrario().getTime());
		cNew.setTime(orario.getTime());
		cNew.set(Calendar.MINUTE, cNew.get(Calendar.MINUTE)-10);
		while(cOld.compareTo(cNew)<0 && correttezza>0)
		{
			cNew.set(Calendar.MINUTE, cNew.get(Calendar.MINUTE)-10);
			correttezza--;
		}
		if (traffico.getTraffico()==Traffico.BLOCCATO) 
		{
			messaggio="BLOCCATO! ";
		}
		else if (traffico.getTraffico()==Traffico.ELEVATO) 
		{
			messaggio="ELEVATO! ";
		}
		if (traffico.getTraffico()==Traffico.MEDIO) 
		{
			messaggio="Medio ";
		}
		if (correttezza==1 || correttezza==2) {
			messaggio=messaggio+"(precisione media)";
		}
		else if (correttezza==0) {
			messaggio=messaggio+"(precisione bassa)";
		}
		else
		{
			messaggio=messaggio+"(precisione elevata)";
		}
		return messaggio;
	}
	
	
	
	/* METODO PER LA LETTURA DELLE INFORMAZIONI SULLA MAPPA E SUI SUOI SEGMENTI DA FILE */
	private void letturaMappa()
	{
		 FileReader f=null;
		    try {
				f=new FileReader("./src/GestoriTraffico/infoSegmenti.txt");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		    BufferedReader b;
		    b=new BufferedReader(f);

		    String s=null;
		    String id=null;
		    String tmp="";
		    int cont=0;
		    int lng=0;
		    
		    // lettura dell informazioni dei segmenti
		    while(true) {
		        try {
					s=b.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
		        if(s==null) break;
		        else
		        {
		        	cont=0;
		        	id="";
		        	// si legge l'id fino al prim0 " " (id segmento)
		        	while (s.charAt(cont)!=' ')
		        	{
		        		id=id+s.charAt(cont);
		        		cont++;
		        	} 
		        	id=id+" ";
		        	tmp=new String();
		        	cont++;
		        	// si legge fino alla virgola (id via)
		        	while (s.charAt(cont)!=',')
		        	{
		        		tmp=tmp+s.charAt(cont);
		        		cont++;
		        	}
		        	id=id+this.identificatoreToVia(tmp);
		        	// nome costruito
		        	// si legge la lunghezza del segmento
		        	lng=0;
		        	for (int i=cont+1;i<s.length();i++)
		        	{
		        		lng=lng*10+(s.charAt(i)-48);
		        	}
		        }
		        this.infoSegmento.put(id, new Segmento(id,lng));
		        this.segmentiAdiacenti.put(id, new ArrayList<String>());
		    }
		    // lettura file delle adiacenze
		    try {
				f=new FileReader("./src/GestoriTraffico/adiacenze.txt");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		    b=new BufferedReader(f);
		    s = "";
		    id = "";
		    tmp = "";
		    cont = 0;
		    lng = 0;
		    String tmp2="";
		    while(true) {
		        try {
					s=b.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
		        if(s==null) break;
		        // lettura segmento chiave
		        else
		        {
				    id = "";
				    tmp = "";
				    cont = 0;
				    lng = 0;
		        	cont=0;
		        	id="";
		        	while (s.charAt(cont)!=' ')
		        	{
		        		id=id+s.charAt(cont);
		        		cont++;
		        	} 
		        	id=id+" ";
		        	tmp=new String();
		        	cont++;
		        	// si legge fino alla virgola (id via)
		        	while (s.charAt(cont)!=',' && s.length()>cont+1)
		        	{
		        		tmp=tmp+s.charAt(cont);
		        		cont++;
		        	}
		        	if (s.length()>cont+1)tmp=tmp+s.charAt(cont);
			        id=id+this.identificatoreToVia(tmp);
			        cont ++;
			        tmp=new String();
			        tmp2=new String();
			        // id costruito
			        // ricerca segmenti adiacenti
			        if (s.length()>cont+1)
			        {
			        	for (int i=cont; i<s.length(); i++)
			        	{
			        		if (s.charAt(i)!=',')
			        		{
			        			tmp=tmp+s.charAt(i);
			        		}
			        		if (s.charAt(i)==',' || s.length()==i+1)
			        		{
			        			tmp=this.identificatoreToVia(tmp);
			        			this.segmentiAdiacenti.get(id).add(tmp2+tmp);
			        			tmp=new String();
			        			tmp2=new String();
			        		}
			        		
			        		if (s.charAt(i)==' ')
			        		{
			        			tmp2=tmp;
			        			tmp=new String();
			        		}
			        		
			        	}
		        	}
		       }
		  }
	}
	
	
	
	/* METODO PER LA CONVERSIONE DA INDENTIFICATORE A NOME VIA */
	public String identificatoreToVia(String id)
	{
		if (id.contains("o")) return "Via Amoretti";
		else if (id.contains("U")) return "Via Appiani";
		else if (id.contains("V")) return "Via Anzani";
		else if (id.contains("b")) return "Via Aprica";
		else if (id.contains("J")) return "Via Balestra";
		else if (id.contains("z")) return "Via Barone";
		else if (id.contains("x")) return "Via Calvi";
		else if (id.contains("j")) return "Via Cadorna";
		else if (id.contains("Z")) return "Via Bassetti";
		else if (id.contains("a")) return "Via Caprani";
		else if (id.contains("H")) return "Via Cavour";
		else if (id.contains("G")) return "Via Ciceri";
		else if (id.contains("i")) return "Via Dotti";
		else if (id.contains("F")) return "Via Duomo";
		else if (id.contains("X")) return "Via Durini";
		else if (id.contains("E")) return "Via Einaudi";
		else if (id.contains("f")) return "Via Ferrari";
		else if (id.contains("A")) return "Via Fiori";
		else if (id.contains("B")) return "Via Foscolo";
		else if (id.contains("C")) return "Via Gaggi";
		else if (id.contains("u")) return "Via Gallio";
		else if (id.contains("r")) return "Via Luini";
		else if (id.contains("D")) return "Via Giovio";
		else if (id.contains("Y")) return "Via Imbonati";
		else if (id.contains("s")) return "Via Lazzago";
		else if (id.contains("h")) return "Via Leoni";
		else if (id.contains("q")) return "Via Geno";
		else if (id.contains("p")) return "Via Magistri";
		else if (id.contains("T")) return "Via Magni";
		else if (id.contains("S")) return "Via Milano";
		else if (id.contains("P")) return "Via Naviigo";
		else if (id.contains("Q")) return "Via Ortelli";
		else if (id.contains("I")) return "Via Ovidio";
		else if (id.contains("O")) return "Via Pascoli";
		else if (id.contains("L")) return "Via Pederzani";
		else if (id.contains("N")) return "Via Quasimodo";
		else if (id.contains("M")) return "Via Raimondi";
		else if (id.contains("m")) return "Via Regina";
		else if (id.contains("n")) return "Via Rho";
		else if (id.contains("c")) return "Via Sagnino";
		else if (id.contains("d")) return "Via Tatti";
		else if (id.contains("e")) return "Via Trento";
		else if (id.contains("t")) return "Via Urbano";
		else if (id.contains("v")) return "Via Valleggio";
		else if (id.contains("W")) return "Via Vacchi";
		else if (id.contains("g")) return "Via Moro";
		else if (id.contains("R")) return "Via Napoleona";
		else if (id.contains("l")) return "Via Netta";
		else return "";
	}
	
}




