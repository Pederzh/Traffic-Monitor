package Test;

import static org.junit.jupiter.api.Assertions.*;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Calendar;

import org.junit.jupiter.api.Test;

import DatiTraffico.StatoTraffico;
import DatiTraffico.Traffico;
import GestoriTraffico.GestoreClient;
import GestoriTraffico.GestoreNotifiche;
import GestoriTraffico.GestoreTraffico;

class ElaborazioneEventoTest {

	@Test
	public void testAnalizzaNotifica() throws RemoteException {
		GestoreNotifiche tester = new GestoreNotifiche();
		GestoreTraffico gt=new GestoreTraffico();
		GestoreClient gc = new GestoreClient();
		gt.setGestoreClient(gc);
		tester.setGestoreClient(gc);
		tester.setGestoreTraffico(gt);
		Timestamp t1 = new Timestamp (System.currentTimeMillis());
		Calendar c1 =Calendar.getInstance();
		Calendar c2 =Calendar.getInstance();
		c1.setTime(t1);
		c2.setTime(t1);
		c1.set(Calendar.MINUTE, c1.get(Calendar.MINUTE)-12);
		StatoTraffico st = new StatoTraffico(40, Traffico.BLOCCATO, 1, t1);
		st.setOrarioCalendar(c1);
		String risultato=tester.analizzaNotifica(st, c2);
		assertEquals("BLOCCATO! (precisione bassa)" ,risultato);
	}
}
