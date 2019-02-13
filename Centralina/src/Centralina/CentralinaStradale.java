package Centralina;

import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

public abstract class CentralinaStradale extends CentralinaGenerica {
	
	protected Traffico traffico;
	protected Via strada;
	protected Timer timer;
	protected int lunghMediaAuto=4500; // lunghezza media delle auto in millimetri
	protected Timestamp periodo;		 // orario di inizio delle rilevazioni
	
	
	
	/* CREATORE TIMER */
	public void costruttoreTimer()
	{
		// viene analizzato il traffico ogni minuto
		timer = new Timer();
        timer.schedule(new TimerTask() {
        
            @Override
            public void run() {
                analizzaTraffico();
            }
        }, 1000*5, 1000*5);
	}
	
	
	
	// METODO DI ANALISI DEL TRAFFICO (DA COSTRUIRE) */
	public void analizzaTraffico()
	{
		
	}
	
	
	
	/* SETTER DEL PERIODO AL TEMPO CORRENTE */
	public void setPeriodo()
	{
		this.periodo = new Timestamp (System.currentTimeMillis());
	}

	
	
	/* DISTRUTTORE TIMER */
	public void killTimer() 
	{
		timer.cancel();
	}

	
	
	/* GETTER TRAFFICO */
	public Traffico getTraffico()
	{
		return this.traffico;
	}
	
	
	
	/* METODO PER LA SPEDIZIONE DEI DATI AL SISTEMA CENTRALE */
	public void spedizione()
	{
		
	}

	
	
	/* SETTER FREQUENZA */
	public void setFrequenza(int f)
	{
		this.frequenza=f;
	}
	
	
		
	/* SETTER TRAFFICO */
	public void setTraffico(Traffico t)
	{
		this.traffico=t;
	}
	
	
}