package Test;

import static org.junit.jupiter.api.Assertions.*;

import java.rmi.RemoteException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import GestoriTraffico.GestoreClient;
import GestoriTraffico.GestoreNotifiche;
import GestoriTraffico.GestoreTraffico;

class RicercaVieAdiacentiTest {

	@Test
	public void testRilevaVieDintorni() throws RemoteException {
		GestoreNotifiche tester = new GestoreNotifiche();
		GestoreTraffico gt=new GestoreTraffico();
		GestoreClient gc = new GestoreClient();
		gt.setGestoreClient(gc);
		tester.setGestoreClient(gc);
		tester.setGestoreTraffico(gt);
		ArrayList<String> risultato = new ArrayList<String>();
		tester.rilevaVieDintorni("01 Via Barone", 200, risultato);
		assertEquals("01 Via Calvi",risultato.get(0));
		assertEquals("01 Via Balestra",risultato.get(1));
		assertEquals("03 Via Barone",risultato.get(2));
		assertEquals("02 Via Calvi",risultato.get(3));
		assertEquals("02 Via Barone",risultato.get(4));
	}
}

