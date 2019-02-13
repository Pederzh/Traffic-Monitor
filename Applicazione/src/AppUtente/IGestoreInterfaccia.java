package AppUtente;

import Common.Traffico;

interface IGestoreInterfaccia {
	
	public void accedi(String username, String password);
	public void segnalaTraffico(Traffico t);
	//public void mostraDettagliNotifica(int id);
	public void nascondiNotifica();
	//public void nascondiNotifiche();
	
}
