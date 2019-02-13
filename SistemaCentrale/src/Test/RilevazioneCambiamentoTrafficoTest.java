package Test;

import static org.junit.jupiter.api.Assertions.*;

import java.rmi.RemoteException;
import java.sql.Timestamp;

import org.junit.jupiter.api.Test;

import DatiTraffico.Evento;
import DatiTraffico.StatoTraffico;
import DatiTraffico.Traffico;
import GestoriTraffico.GestoreClient;
import GestoriTraffico.GestoreTraffico;

class RilevazioneCambiamentoTrafficoTest {

	@Test
	public void testRilevaCambiamentoTraffico() throws RemoteException {
		GestoreTraffico tester=new GestoreTraffico();
		GestoreClient gc = new GestoreClient();
		Timestamp t1 = new Timestamp (System.currentTimeMillis());
		tester.setGestoreClient(gc);
		StatoTraffico tmp1= new StatoTraffico(0, Traffico.ELEVATO, 1, t1);
		StatoTraffico tmp2= new StatoTraffico(0, Traffico.SCORREVOLE, 1, t1);
		Evento risultato=tester.rilevaCambiamentoTraffico(tmp1, tmp2);
		assertEquals(Evento.CALATOSCORREVOLE,risultato);
	}
}
