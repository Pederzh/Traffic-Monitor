package Centralina;

public class Via {

	private String nome;
	private int limiteVelocita;
	private Tipologia tipologiaStrada;
	private SensoDiMarcia direzione;
	private int capienza;
	
	private int MAXLIMITE=130;
	private int MINLIMITE=5;
	
	public Via(String nomeVia, int limite, Tipologia tipo, SensoDiMarcia senso, int capacita) {
		setNome(nomeVia);
		setLimiteVelocita(limite);
		setTipologiaStrada(tipo);
		setDirezione(senso);
		setCapienza(capacita);
	}
	
	private void setCapienza(int c)
	{
		this.capienza=c;
	}
	
	private void setNome(String n) {
		this.nome=n;
	}
	
	private void setLimiteVelocita(int l) {
		if (l<MINLIMITE)  this.limiteVelocita=MINLIMITE;
		else if (l>MAXLIMITE) this.limiteVelocita=MAXLIMITE;
		else {
			int tmp=l%5;
			this.limiteVelocita=l-tmp;	
		}
	}
	
	private void setTipologiaStrada(Tipologia t) {
		this.tipologiaStrada=t;
	}
	
	private void setDirezione(SensoDiMarcia d) {
		this.direzione=d;
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public int getCapienza()
	{
		return this.capienza;
	}
	
	public int getLimiteVelocita() {
		return this.limiteVelocita;
	}
	
	public Tipologia getTipologiaStrada() {
		return this.tipologiaStrada;
	}
	
	public SensoDiMarcia getDirezione() {
		return this.direzione;
	}
	
    public void kill() {
    	this.kill();
    }
}
