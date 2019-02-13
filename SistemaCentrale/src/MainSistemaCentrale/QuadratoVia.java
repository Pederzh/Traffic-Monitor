package MainSistemaCentrale;

public class QuadratoVia {
	
	private int x1;
	private int x2;
	private int x3;
	private int x4;
	private int y1;
	private int y2;
	private int y3;
	private int y4;
	private int xTabella;
	private int yTabella;
	private String nome;
	
	public QuadratoVia()
	{
		x1=0;
		x2=0;
		x3=0;
		x4=0;
		y1=0;
		y2=0;
		y3=0;
		y4=0;
		nome="";
	}
	
	public void setNome(String parola)
	{
		this.nome=parola;
	}
	
	public void setNome(char parola)
	{
		this.nome=new String();
		this.nome+=parola;
	}
	
	public void setCoordX(int numero, int x) 
	{
		if (numero==1) this.x1=x;
		if (numero==2) this.x2=x;
		if (numero==3) this.x3=x;
		if (numero==4) this.x4=x;
	}
	
	public void setCoordY(int numero, int y) 
	{
		if (numero==1) this.y1=y;
		if (numero==2) this.y2=y;
		if (numero==3) this.y3=y;
		if (numero==4) this.y4=y;
	}
	
	public void setXYTabella( int x, int y)
	{
		this.xTabella=x;
		this.yTabella=y;
	}
	
	public int getXTabella() {
		return this.xTabella;
	}
	
	public int getYTabella() {
		return this.yTabella;
	}
	
	public void setX(int c1, int c2, int c3, int c4)
	{
		this.x1=c1;
		this.x2=c2;
		this.x3=c3;
		this.x4=c4;
	}
	
	public void setY(int c1, int c2, int c3, int c4)
	{
		this.y1=c1;
		this.y2=c2;
		this.y3=c3;
		this.y4=c4;
	}
	
	public int getX(int n)
	{
		if (n==1) return this.x1;
		if (n==2) return this.x2;
		if (n==3) return this.x3;
		if (n==4) return this.x4;
		else return 0;
	}
	
	public int getY(int n)
	{
		if (n==1) return this.y1;
		if (n==2) return this.y2;
		if (n==3) return this.y3;
		if (n==4) return this.y4;
		else return 0;
	}
	
	public String getNome()
	{
		return this.nome;
	}
	
	
}
