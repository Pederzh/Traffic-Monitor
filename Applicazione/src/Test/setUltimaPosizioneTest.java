package Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import AppUtente.ApplicazioneDataManager;
import AppUtente.Utente;

class setUltimaPosizioneTest {

	@Test
	public void testSetUltimaPosizioneTest() {
		
		ApplicazioneDataManager tester = new ApplicazioneDataManager();
		
		if(! tester.isUtenteRegistrato("test")) {
			//inserisco l'utente di test
			tester.registraUtente("test", "test");
		}
		
		Utente u = new Utente("test", "test");
		tester.setUltimaPosizione(u, "01 Via Bassetti");
		assertEquals("01 Via Bassetti", tester.getUltimaPosizione(u), "se il setter funziona, "
				+ "il getter ritorna lo stesso valore");
	}

}
