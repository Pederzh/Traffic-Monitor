package Test;

import static org.junit.jupiter.api.Assertions.*;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Calendar;

import org.junit.jupiter.api.Test;

import Centralina.CentralinaStradaleASensoreSingolo;
import Centralina.ID;
import Centralina.SensoDiMarcia;
import Centralina.Tipologia;
import Centralina.Traffico;

class ElaborazioneFrequenzaTest {

	@Test
	public void testFrequenza() throws RemoteException {
		CentralinaStradaleASensoreSingolo tester = 
		new CentralinaStradaleASensoreSingolo("Via Prova", 50, Tipologia.SECONDARIA, SensoDiMarcia.AB);
		tester.accendi();
		Timestamp t=new Timestamp (System.currentTimeMillis());
		tester.nuovaRilevazione(new Timestamp (System.currentTimeMillis()), ID.A);
		Calendar c=Calendar.getInstance();
		c.setTime(t);
		c.set(Calendar.SECOND, c.get(Calendar.SECOND)+3);
		t.setTime(c.getTimeInMillis());
		Calendar c2=Calendar.getInstance();
		t=new Timestamp (System.currentTimeMillis());
		c2.setTime(t);
		while(c.get(Calendar.SECOND)!=c2.get(Calendar.SECOND))
		{
			t=new Timestamp (System.currentTimeMillis());
			c2.setTime(t);
		}
		tester.nuovaRilevazione(new Timestamp (System.currentTimeMillis()), ID.B);
		tester.analizzaTraffico();
		int risultato2=tester.getFrequenza();
		Traffico risultato=tester.getTraffico();
		assertEquals(Traffico.BLOCCATO,risultato);	
		assertEquals(1,risultato2);
		
		tester.nuovaRilevazione(new Timestamp (System.currentTimeMillis()), ID.A);
		tester.nuovaRilevazione(new Timestamp (System.currentTimeMillis()), ID.B);
		tester.analizzaTraffico();
		tester.nuovaRilevazione(new Timestamp (System.currentTimeMillis()), ID.A);
		tester.nuovaRilevazione(new Timestamp (System.currentTimeMillis()), ID.B);
		tester.analizzaTraffico();
		risultato=tester.getTraffico();
		risultato2=tester.getFrequenza();
		assertEquals(Traffico.SCORREVOLE,risultato);
		assertEquals(8,risultato2);
		
	}
}