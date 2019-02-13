package Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.runner.JUnitCore;
import org.junit.runners.JUnit4;

import AppUtente.ApplicazioneDataManager;

class autenticaUtenteTest {

	@Test
	public void testAutenticaUtente() {
		ApplicazioneDataManager tester = new ApplicazioneDataManager();
		
		if(! tester.isUtenteRegistrato("test")) {
			//inserisco l'utente di test
			tester.registraUtente("test", "test");
		}
		
		//verifico se l'autenticazione funziona
		assertEquals(true, tester.autenticaUtente("test", "test"), "deve essere vero");
		
	}
}
