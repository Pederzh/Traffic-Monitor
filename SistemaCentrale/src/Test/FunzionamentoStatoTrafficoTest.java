package Test;

import static org.junit.jupiter.api.Assertions.*;

import java.rmi.RemoteException;

import org.junit.jupiter.api.Test;

import DatiTraffico.DatoCompleto;
import DatiTraffico.Mittente;
import DatiTraffico.StatoTrafficoPeriodico;
import DatiTraffico.Traffico;

class FunzionamentoStatoTrafficoTest {

	@Test
	public void testStatoTraffico()throws RemoteException {
		StatoTrafficoPeriodico tester =new StatoTrafficoPeriodico();
		DatoCompleto dc=new DatoCompleto();
		dc.setMittente(Mittente.APPLICAZIONE);
		dc.setOrarioCorrente();
		dc.setPosizione("Via Barone");
		dc.setTraffico(Traffico.ELEVATO);
		tester.setDato(dc);
		tester.setPrimoOrarioCalendar(tester.getOrario());
		tester.diminuisciCorrettezza(Mittente.CENTRALINASTRADALE, Traffico.SCORREVOLE);
		int risultato=tester.getCorrettezza();
		Traffico risultato2=tester.getTraffico();
		assertEquals(4,risultato);
		assertEquals(Traffico.SCORREVOLE,risultato2);
	}
}
