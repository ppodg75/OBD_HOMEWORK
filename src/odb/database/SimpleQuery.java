package odb.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SimpleQuery {

	private String tableName;
	private String where;

	public SimpleQuery(String tableName, String where) {
		this.tableName = tableName;
		this.where = where;
	}

	public boolean anyRowExists() throws SQLException {
		Statement statement = DBConnection.getInstance().getStatement();
		String query = "select count(1) from " + tableName + " where " + where;
		ResultSet rs = statement.executeQuery(query);
		int count = 0;
		while (rs.next()) {
			count = rs.getInt(1);
		}
		return count > 0;
	}
	
}
