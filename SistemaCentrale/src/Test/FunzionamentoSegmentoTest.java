package Test;

import static org.junit.jupiter.api.Assertions.*;

import java.rmi.RemoteException;

import org.junit.jupiter.api.Test;

import GestoriTraffico.Segmento;

class FunzionamentoSegmentoTest {

	@Test
	public void testSegmento()throws RemoteException {
		Segmento tester=new Segmento();
		tester.setLunghezza(20);
		tester.setSegmento("01 Via Barone");
		String risultato=tester.getVia();
		assertEquals("Via Barone",risultato);
	}
}
