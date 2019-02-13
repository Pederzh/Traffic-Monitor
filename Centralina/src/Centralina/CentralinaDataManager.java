package Centralina;

import java.sql.*;
import java.util.ArrayList;

public class CentralinaDataManager {
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
    
    public ArrayList<String> popolaComboBoxPosizione(){
   	 
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<String> vie = new ArrayList<>();
      try {
  		String sql = "SELECT * FROM vie"; //sql da eseguire
	        conn = CentralinaDataManager.connect();
	        stmt = conn.prepareStatement(sql);
	        rs = stmt.executeQuery();
	    	while(rs.next()) {
	    		  vie.add(rs.getString("via"));
	    		  
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
    
    public int getLimiteVelocita(String via) {
    	Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
      try {
  		String sql = "SELECT * FROM vie WHERE via=?"; //sql da eseguire
	        conn = CentralinaDataManager.connect();
	        stmt = conn.prepareStatement(sql);
	        stmt.setString(1, via);
	        rs = stmt.executeQuery();
	    	if(rs.next()) {
    		  System.out.println(via + " ha limite = " + rs.getInt("limite")); 
    		  return rs.getInt("limite");
	    	}
	    	
      }     
      catch (SQLException e) {
    	  
          System.out.println("Errore prelievo del limite di velocità"); 
          
	    } finally {	
			//disconnessione:  https://stackoverflow.com/questions/2225221/closing-database-connections-in-java
	        try { rs.close(); } catch ( Exception e) { /* ignora */ }
	        try { stmt.close(); } catch ( Exception e) { /* ignora */ }
	        try { conn.close(); } catch ( Exception e) { /* ignora */ }
	    }
    	
      return 0;
    }
    
}
