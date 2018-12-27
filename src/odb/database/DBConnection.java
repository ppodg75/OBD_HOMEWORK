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
	private static Connection connection = null;
	private static DBConnection dbconnection;

	private DBConnection() {		
		init();
	}

	public static DBConnection getInstance() {
		if (dbconnection == null) {
			dbconnection = new DBConnection();
		}
		return dbconnection;
	}

	private void init() {

		try {
			Class c = Class.forName(driverName);
			open();
//    	System.out.println("AutoCommit: "+connection.getAutoCommit());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void open() throws SQLException {
		if (connection == null) {
			connection = DriverManager.getConnection(url, username, password);
		}
	}

	public static void close() {
		if (!isConnected()) {
			return;
		}
		try {
			connection.close();
			connection = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Statement getStatement() throws SQLException {
		return connection.createStatement();
	}

	public static boolean isConnected() {
		return connection != null;
	}

	public static SimpleQuery simpleQuery(String tableName, String where) {
		return new SimpleQuery(tableName, where);
	}

}
