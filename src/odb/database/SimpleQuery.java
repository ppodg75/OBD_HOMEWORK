package odb.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class SimpleQuery {

	private String tableName;
	private String where;

	public SimpleQuery(String tableName, String where) {
		this.tableName = tableName;
		this.where = where;
	}

	public boolean anyRowExists() throws SQLException, ClassNotFoundException {
		DBConnection db = DBConnection.getInstance();
		int count = 0;

		Statement statement = db.getStatement();
		String query = "select 1 from " + tableName + " where " + where;
		ResultSet rs = statement.executeQuery(query);
		return rs.next();

	}

}
