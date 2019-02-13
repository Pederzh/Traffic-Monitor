package Test;

import static org.junit.jupiter.api.Assertions.*;

import java.rmi.RemoteException;

import org.junit.jupiter.api.Test;

import Centralina.Rilevazione;

class FunzionamentoRilevazioneTest {

	@Test
	public void testRilevazione() throws RemoteException {
		Rilevazione tester= new Rilevazione();
		assertEquals(false,tester.getRilevato());
		tester.setT1();
		tester.setT2();
		tester.setRilevato(true);
		assertEquals(true,tester.getRilevato());
	}
}