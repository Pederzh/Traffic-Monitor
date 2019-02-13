package DatiTraffico;

import java.sql.Timestamp;

abstract public class Dato {

    protected Timestamp orario;
    protected String posizione;
    
    public Dato(){
        setOrarioCorrente();
        setPosizione(null);
    }
    
    public Dato(Timestamp t, String p){
        setOrario(t);
        setPosizione(p);
    }

    public void setOrarioCorrente() {
        this.orario=new Timestamp (System.currentTimeMillis());
    }
    
    public void setOrario(Timestamp t) {
        this.orario=t;
    }
    
    public void setPosizione(String p) {
    	this.posizione=p;
    }

    public Timestamp getOrario() {
        return this.orario;
    }
    
    public String getPosizione() {
    	return this.posizione;
    }

    public void kill() {
    	//this.kill();
    }
}
