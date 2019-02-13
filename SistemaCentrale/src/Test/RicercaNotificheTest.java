package Test;

import static org.junit.jupiter.api.Assertions.*;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import GestoriTraffico.GestoreClient;
import GestoriTraffico.GestoreNotifiche;
import GestoriTraffico.GestoreTraffico;

class RicercaNotificheTest {

	@Test
	public void testRilevaNotifiche() throws RemoteException {
		GestoreNotifiche tester = new GestoreNotifiche();
		GestoreTraffico gt=new GestoreTraffico();
		GestoreClient gc = new GestoreClient();
		gt.setGestoreClient(gc);
		tester.setGestoreClient(gc);
		tester.setGestoreTraffico(gt);
		ArrayList<String> risultato = new ArrayList<String>();
		risultato=tester.rilevaNotifiche("01 Via Fiori", 1, 200);
		int nNotifiche=risultato.size();
		Timestamp t1 = new Timestamp (System.currentTimeMillis());
		gt.nuovoDato("Via Calvi", "BLOCCATO", t1, 4);
		risultato=tester.rilevaNotifiche("01 Via Barone", 1, 200);
		assertEquals(0 ,nNotifiche);
		assertEquals("BLOCCATO! (precisione elevata)/Via Calvi" ,risultato.get(0));
	}
}
