package Centralina;

import java.sql.Timestamp;

public class Rilevazione {
	
	private Timestamp t1;
	private Timestamp t2;
	private boolean rilevato;
	
	public Rilevazione()
	{
		setRilevato(false);
	}
	
	public void setRilevato(boolean val)
	{
		this.rilevato=val;
	}
	
	public boolean getRilevato()
	{
		return this.rilevato;
	}
	
	public void setT1()
	{
		this.t1 = new Timestamp (System.currentTimeMillis());
	}
	
	public void setT1(Timestamp t)
	{
		this.t1 = t;
	}
	
	public void setT2()
	{
		this.t2 = new Timestamp (System.currentTimeMillis());
	}
	
	public void setT2(Timestamp t)
	{
		this.t2 = t;
	}
	
	public Timestamp getT1()
	{
		return this.t1;
	}
	
	public Timestamp getT2()
	{
		return this.t2;
	}
}
