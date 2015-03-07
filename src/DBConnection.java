import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost/ttdb";
	
	//  Database credentials
	static final String USER = "root";
	static final String PASSWORD = "";
	
	private static Connection connection = null;
	
	public static Connection getConnection(){
		if (connection != null)
            return connection;
		else{
			try{
				  //Register JDBC driver
			      Class.forName("com.mysql.jdbc.Driver");
			      
			      //Open a connection
			      connection = DriverManager.getConnection(DB_URL,USER,PASSWORD);
			}
			catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }  
		}
		return connection;
	}
	
	
}
