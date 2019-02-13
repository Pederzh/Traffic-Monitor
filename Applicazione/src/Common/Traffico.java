package Common;

public enum Traffico {
	ASSENTE(0),
	SCORREVOLE(1),
	MEDIO(2),
	ELEVATO(3),
	BLOCCATO(4);
	
	private int index;
	
	private Traffico(int i) {
		setIndex(i);
	}
	
	public void setIndex(int i) {
		this.index = i;
	}
	
	public int getIndex() { return this.index; }
	
}
