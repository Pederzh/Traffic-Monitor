package GestoriTraffico;

public class Segmento {
	private String segmento;
	private int lng;
	private int SEZIONESEGMENTO=3;
	
	public Segmento() 
	{
		setSegmento("");
		setLunghezza(0);
	}
	
	public Segmento(String s, int l)
	{
		setSegmento(s);
		setLunghezza(l);
	}
	
	public void setSegmento(String s)
	{
		this.segmento=s;
	}
	
	public void setLunghezza(int l)
	{
		this.lng=l;
	}
	
	public String getSegmento() 
	{
		return this.segmento;
	}
	
	public int getLunghezza() 
	{
		return this.lng;
	}
	
	public String getVia() 
	{
		String s=getSegmento();
		String tmp="";
		for (int i=SEZIONESEGMENTO; i<s.length(); i++)
		{
			tmp+=s.charAt(i);
		}
		return tmp;
		
	}

    public void kill() 
    {
    	this.kill();
    }

}

