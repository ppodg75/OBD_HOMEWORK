package odb.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

	private static final String driverName = "oracle.jdbc.driver.OracleDriver";
	private static final String url = "jdbc:oracle:thin:@ora3.elka.pw.edu.pl:1521:ora3inf";
	private static final String username = "ppodgors";
	private static final String password = "ppodgors"; // wiadomo jakie
	private Connection connection = null;
	private static DBConnection dbconnection;

	private DBConnection() throws ClassNotFoundException, SQLException {		
		init();
	}

	public static DBConnection getInstance() throws ClassNotFoundException, SQLException {
		if (dbconnection == null) {
			dbconnection = new DBConnection();
		}
		return dbconnection;
	}

	private void init() throws ClassNotFoundException, SQLException {
	    Class c = Class.forName(driverName);		
	}

	public void open() throws SQLException {
		if (connection == null) {
			connection = DriverManager.getConnection(url, username, password);
		}
	}

	public void close() throws SQLException {
		System.out.println("Zamykanie po³¹czenia!");
		if (!isConnected()) {
			return;
		}
     	connection.close();	
     	connection = null;
	}

	public Statement getStatement() throws SQLException {
		return connection.createStatement();
	}

	public boolean isConnected() {
		return connection != null;
	}

	public static SimpleQuery simpleQuery(String tableName, String where) {
		return new SimpleQuery(tableName, where);
	}

	
}
