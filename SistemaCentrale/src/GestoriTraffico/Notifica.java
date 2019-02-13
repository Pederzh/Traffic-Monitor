package GestoriTraffico;

public class Notifica {
	private String testo;
	private String posizione;
	
	public Notifica(String t, String p) 
	{
		setTesto(t);
		setPosizione(p);
	}
	
	public void setTesto(String t) 
	{
		this.testo=t;
	}
	
	public void setPosizione(String p) 
	{
		this.posizione=p;
	}
	
	public String getTesto() 
	{
		return this.testo;
	}
	
	public String getPosizione() 
	{
		return this.posizione;
	}
	
    public void kill() 
    {
    	this.kill();
    }
    
}

