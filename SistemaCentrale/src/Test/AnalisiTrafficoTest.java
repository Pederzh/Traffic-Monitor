package Test;

import static org.junit.jupiter.api.Assertions.*;

import java.rmi.RemoteException;
import java.sql.Timestamp;

import org.junit.jupiter.api.Test;

import DatiTraffico.StatoTraffico;
import DatiTraffico.Traffico;
import GestoriTraffico.GestoreClient;
import GestoriTraffico.GestoreTraffico;

class AnalisiTrafficoTest {

	@Test
	public void testNuovoDato() throws RemoteException {
		GestoreTraffico tester=new GestoreTraffico();
		GestoreClient gc = new GestoreClient();
		tester.setGestoreClient(gc);
		Timestamp t1 = new Timestamp (System.currentTimeMillis());
		tester.nuovoDato("Via Barone", "BLOCCATO", t1, 1);
		tester.nuovoDato("Via Barone", "ASSENTE", t1, 4);
		Traffico risultato=tester.getTraffico("Via Barone").getTraffico();
		int risultato2=tester.getTraffico("Via Barone").getCorrettezza();
		StatoTraffico risultato3=tester.getTraffico("Via Fiori");
		assertEquals(Traffico.ASSENTE,risultato);
		assertEquals(3,risultato2);
		assertEquals(null, risultato3);
	}
}