package Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import AppUtente.ApplicazioneDataManager;

class isUtenteRegistratoTest {

	@Test
	public void testIsUtenteRegistrato() {
		
		ApplicazioneDataManager tester = new ApplicazioneDataManager();
		
		//verifico se la verifica dell'utente se è registrato funziona
		assertEquals(false, tester.isUtenteRegistrato(" "), "l'utente ' ' "
				+ "non può essere stato registrato");
	}

}
