package AppUtente;

import java.sql.*;
import java.util.ArrayList;

public class ApplicazioneDataManager {
	
    private static String url = "jdbc:mysql://localhost:3306/gruppo09"; //stringa di connessione 
    private static String driver = "com.mysql.cj.jdbc.Driver"; 
    private static String username = "gruppo09";   
    private static String password = "gruppo09";
    private static Connection con;  //connessione col db 

    
    public static Connection connect() {
        //Source https://stackoverflow.com/questions/10915375/create-a-class-to-connect-to-any-database-using-jdbc
        try {
            Class.forName(driver);
            try {
            	//tentativo di connessione al db
            	con = DriverManager.getConnection(url, username, password);
            	
            } catch (SQLException ex) {
                // log exception
            	System.out.println("Fallito il tentativo di connessione al database"); 
                
            }
        } catch (ClassNotFoundException ex) {
            // log exception
            System.out.println("Driver non disponibile"); 
            
        }
        return con;
        
    }
    

    public void modificaPosizione(int appid, String posizione) {
    	
    }
    
    
    public String posizioneApp(int appid) {
    	return "";
    }
    
    public String getUltimaPosizione(Utente u) {
    	//connessione al db e controlla se la password è giusta (true)
    	 
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

      try {
  		String sql = "SELECT via FROM utenti WHERE username=?"; //sql da eseguire
	        conn = ApplicazioneDataManager.connect();
	        stmt = conn.prepareStatement(sql);
	        stmt.setString(1, u.getUsername());
	        rs = stmt.executeQuery();
	    	//controllo
		    if ( rs.next() ) {
		    	System.out.println("Via restituita: " + rs.getString("via")); 
		    	return rs.getString("via");
		    }
		    else {
		    	System.out.println("Via non restituita");
		    	return "";
		    }
      }     
      catch (SQLException e) {
    	  
          System.out.println("Recupero ultima posizione: tentativo fallito"); 
          
	    } finally {	
			//disconnessione:  https://stackoverflow.com/questions/2225221/closing-database-connections-in-java
	        try { rs.close(); } catch ( Exception e) { /* ignora */ }
	        try { stmt.close(); } catch ( Exception e) { /* ignora */ }
	        try { conn.close(); } catch ( Exception e) { /* ignora */ }
	    }
    	
      return "";
    }
    
    public ArrayList<String> popolaComboBoxPosizione(){
    	//connessione al db e controlla se la password è giusta (true)
   	 
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<String> vie = new ArrayList<>();
      try {
  		String sql = "SELECT * FROM vie"; //sql da eseguire
	        conn = ApplicazioneDataManager.connect();
	        stmt = conn.prepareStatement(sql);
	        rs = stmt.executeQuery();
	    	while(rs.next()) {
	    		  vie.add(rs.getString("via"));
	    		  //System.out.println(rs.getString("via"));
	    	}
	    	
	    	return vie;
      }     
      catch (SQLException e) {
    	  
          System.out.println("Popolamento combobox delle posizioni: tentativo fallito"); 
          
	    } finally {	
			//disconnessione:  https://stackoverflow.com/questions/2225221/closing-database-connections-in-java
	        try { rs.close(); } catch ( Exception e) { /* ignora */ }
	        try { stmt.close(); } catch ( Exception e) { /* ignora */ }
	        try { conn.close(); } catch ( Exception e) { /* ignora */ }
	    }
    	
      return vie;
    	
    }
    
    public boolean setUltimaPosizione(Utente u, String via) {
    	 
		Connection conn = null;
		PreparedStatement stmt = null;

      try {
  		String sql = "UPDATE utenti SET via = ? WHERE username = ?"; //sql da eseguire
	        conn = ApplicazioneDataManager.connect();
	        stmt = conn.prepareStatement(sql);
	        stmt.setString(1, via);
	        stmt.setString(2, u.getUsername());
	        stmt.executeUpdate();

	    	System.out.println("posizione salavata"); 
	    	return true;
      }
      catch (SQLException e) {
    	  
          System.out.println("Salvataggio ultima posizione: tentativo fallito"); 
          
	    } finally {	
			//disconnessione:  https://stackoverflow.com/questions/2225221/closing-database-connections-in-java
	        try { stmt.close(); } catch ( Exception e) { /* ignora */ }
	        try { conn.close(); } catch ( Exception e) { /* ignora */ }
	    }
    	
      return false;
    }
    
	public boolean autenticaUtente(String username, String password) {
    	//connessione al db e controlla se la password è giusta (true)
 
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

      try {
  		String sql = "SELECT password FROM utenti WHERE username=? AND password=?"; //sql da eseguire
	        conn = ApplicazioneDataManager.connect();
	        stmt = conn.prepareStatement(sql);
	        stmt.setString(1, username);
	        stmt.setString(2, password);
	        rs = stmt.executeQuery();
	    	//controllo
		    if ( rs.next() ) {
		    	System.out.println("password corretta"); 
		    	return true;
		    }
		    else {
		    	System.out.println("password sbagliata"); 
		    	return false;
		    }
      }     
      catch (SQLException e) {
    	  
          System.out.println("Autenticazione: tentativo fallito"); 
          
	    } finally {	
			//disconnessione:  https://stackoverflow.com/questions/2225221/closing-database-connections-in-java
	        try { rs.close(); } catch ( Exception e) { /* ignora */ }
	        try { stmt.close(); } catch ( Exception e) { /* ignora */ }
	        try { conn.close(); } catch ( Exception e) { /* ignora */ }
	    }
    	
      return false;
    }
    
    public boolean registraUtente(String username, String password) {
    	//connessione al db e registra l'utente nel db, se va a buon fine true altrimenti false
    	
    	  String sql = "INSERT INTO utenti(username, password, via) VALUES(?, ?, ?)"; //sql da eseguire
          Connection conn = null;
          PreparedStatement stmt = null;

         try {
       	  
   	        conn = ApplicazioneDataManager.connect();
		    stmt = conn.prepareStatement(sql);
		    stmt.setString(1, username);
		    stmt.setString(2, password);
		    stmt.setString(3, "");		//via di defalut
		    stmt.executeUpdate();
		    System.out.println("l'utente è stato registrato"); 
   	        return true;
   	        
         }
         catch (SQLException e) {
       	  
             System.out.println("Registrazione utente: Tentativo fallito"); 
             
   	    } finally {
   	    	
			//disconnessione:  https://stackoverflow.com/questions/2225221/closing-database-connections-in-java
	        try { stmt.close(); } catch (Exception e) { /* ignora */ }
	        try { conn.close(); } catch (Exception e) { /* ignora */ }
    		
   	    }
         return false;
    	
    }
    
    public boolean isUtenteRegistrato(String username) {
		//connessione al db e se esiste l'username true
    	   String sql = "SELECT username FROM utenti WHERE username=? LIMIT 1;"; //sql da eseguire
           Connection conn = null;
           PreparedStatement stmt = null;
           ResultSet rs = null;

          try {

    	        conn = ApplicazioneDataManager.connect();
    	        stmt = conn.prepareStatement(sql);
    	        stmt.setString(1, username);
    	        rs = stmt.executeQuery();
    	        
  
    	        if ( rs.next() ) {
    	        	System.out.println("esiste"); 
    	        	return true;
    	        	
    	        }
    	        else {
    	        	System.out.println("non esiste"); 
    	        	return false;
    	        }
    	        
          }
          catch (SQLException e) {
        	  
        	  System.out.println("Check se l'utente è registrato: Tentativo fallito"); 
              
              
    	    } finally {

				//disconnessione:  https://stackoverflow.com/questions/2225221/closing-database-connections-in-java
		        try { rs.close(); } catch ( Exception e) { /* ignora */ }
		        try { stmt.close(); } catch ( Exception e) { /* ignora */ }
		        try { conn.close(); } catch ( Exception e) { /* ignora */ }


    	    }
          return false;
	}
    
    
    
}

