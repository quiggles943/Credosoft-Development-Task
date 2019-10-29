import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
   
public class Database {

	public static void Create() throws ClassNotFoundException
	{
		Class.forName("org.sqlite.JDBC");
		Connection connection = null;
		
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
			
			Statement statement = connection.createStatement();
	         statement.setQueryTimeout(30);  // set timeout to 30 sec.

	         //Create Pipe Integrity table and insert test data
	         statement.executeUpdate("DROP TABLE IF EXISTS Pipe_Integrity");
	         statement.executeUpdate("CREATE TABLE Pipe_Integrity (id INTEGER, diameter LONG)");
	         
	         int ids [] = {1,2,3,4,5};
	         double diameters [] = {0.258,0.458,0.520,1.23,2.46};
	         
	         for(int i=0;i<ids.length;i++){
	              statement.executeUpdate("INSERT INTO Pipe_Integrity values(' "+ids[i]+"', '"+diameters[i]+"')");   
	         }
	         //Create Inspection_Interval table and insert test data 
	         statement.executeUpdate("DROP TABLE IF EXISTS Inspection_Interval");
	         statement.executeUpdate("CREATE TABLE Inspection_Interval (id INTEGER, inspectionDate NUMERIC)");
	        	   
	         int pipeIds [] = {1,2,3,4,5};
	         long inspectionDates [] = { 1572272988000L,1566993600000L,1568462400000L,1569672000000L,1562932800000L};
	         
	         for(int i=0;i<ids.length;i++){
	              statement.executeUpdate("INSERT INTO Inspection_Interval values(' "+pipeIds[i]+"', '"+inspectionDates[i]+"')");   
	         }
	         
	         //Create Pipe Location table and insert test data
	         statement.executeUpdate("DROP TABLE IF EXISTS Pipe_Location");
	         statement.executeUpdate("CREATE TABLE Pipe_Location (id INTEGER, location STRING)");
	        	   
	         int pipeIds1 [] = {1,2,3,4,5};
	         String Locations [] = { "Edinburgh","South Queensferry","Leith","Broxburn","Grangemouth"};
	         
	         for(int i=0;i<ids.length;i++){
	              statement.executeUpdate("INSERT INTO Pipe_Location values(' "+pipeIds1[i]+"', '"+Locations[i]+"')");   
	         }
	         
		} catch (SQLException e) {
			
			e.printStackTrace();
		}

	}
	
	public static void addToDatabase(String table, String arg1, String arg2) throws ClassNotFoundException
	{
		Class.forName("org.sqlite.JDBC");
		Connection connection = null;
		
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
			
			Statement statement = connection.createStatement();
	         statement.setQueryTimeout(30);  // set timeout to 30 sec.
	         statement.executeUpdate("INSERT INTO "+ table +" values(' "+arg1+"', '"+arg2+"')");   
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	public static void removeFromDatabase(String table, String id) throws ClassNotFoundException
	{
		Class.forName("org.sqlite.JDBC");
		Connection connection = null;
		
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
			
			Statement statement = connection.createStatement();
	         statement.setQueryTimeout(30);  // set timeout to 30 sec.
	         statement.executeUpdate("DELETE FROM "+ table +" WHERE id ="+id);   
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}

	public static ArrayList<String> getTables() throws ClassNotFoundException {
		ArrayList<String> tables = new ArrayList<String>();
		Class.forName("org.sqlite.JDBC");
		Connection connection = null;
		
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
			
			Statement statement = connection.createStatement();
	        statement.setQueryTimeout(30);  // set timeout to 30 sec.
	        DatabaseMetaData metaData = connection.getMetaData();
			String[] types = {"TABLE"};
			ResultSet rs = metaData.getTables(null, null, "%", types);
            while (rs.next()) {
                tables.add(rs.getString(3));
            }
	         
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		/*if(tables.isEmpty())
		{
			Create();
			tables= new ArrayList<String>();
			getTables();
		}*/
		return tables;
		
	}
	
	public static boolean exists() throws ClassNotFoundException
	{
		Class.forName("org.sqlite.JDBC");
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
			
			Statement statement = connection.createStatement();
	        statement.setQueryTimeout(30);  // set timeout to 30 sec.
	        DatabaseMetaData metaData = connection.getMetaData();
			String[] types = {"TABLE"};
			ResultSet rs = metaData.getTables(null, null, "%", types);
            if(rs.next()) {
            	return true;

            }
            else{
            	return false;
            }
	         
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return false;
	}

}
