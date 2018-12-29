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
	private static final String password = ""; // wiadomo jakie
	private Connection connection = null;
	private static DBConnection dbconnection;

	private DBConnection() throws ClassNotFoundException {
		init();
	}

	public static DBConnection getInstance() throws ClassNotFoundException {
		if (dbconnection == null) {
			dbconnection = new DBConnection();
		}
		return dbconnection;
	}

	private void init() throws ClassNotFoundException {
		Class c = Class.forName(driverName);
	}

	public void open() throws SQLException {		
		connection = DriverManager.getConnection(url, username, password);		
	}

	public void close() {
		try {
			if (connection != null) {
				connection.close();
				System.out.println("Zamykanie po³¹czenia z baz¹ ... zamkniête!");
			}
			connection = null;
		} catch (SQLException e) {
		}
		
	}

	public Statement getStatement() throws SQLException {
		return connection.createStatement();
	}

	public static SimpleQuery simpleQuery(String tableName, String where) {
		return new SimpleQuery(tableName, where);
	}

}
