package GestoriTraffico;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import AppSistema.IRmiApplicazione;

public class DatiClient {
	private String posizione;
	private ArrayList<String> vieNotificate;
	private IRmiApplicazione applicazione;
	
	public void setApplicazione(IRmiApplicazione app)
	{
		this.applicazione=app;
	}
	
	public IRmiApplicazione getApplicazione()
	{
		return this.applicazione;
	}
	
	public DatiClient(String via) 
	{
		setPosizione(via);
		this.vieNotificate= new ArrayList<String>();
	}
	
	public DatiClient() 
	{
		setPosizione(null);
		this.vieNotificate= new ArrayList<String>();
	}
	
	public DatiClient(String v, Calendar c) 
	{
		setPosizione(v);
		this.vieNotificate= new ArrayList<String>();
	}
	
	public void setPosizione(String v) 
	{
		this.posizione=v;
	}	
	
	public String getPosizione() 
	{
		return this.posizione;
	}
	
	public void addVia(String via) 
	{
		this.vieNotificate.add(via);
	}
	
	public void setVie(ArrayList<String> v) 
	{
		this.vieNotificate=v;
	}
	
	public void clearVie() 
	{
		this.vieNotificate.clear();
	}
	
	public ArrayList<String> getVie()
	{
		return this.vieNotificate;
	}
	
    public void kill() 
    {
    	this.kill();
    }
	
}
