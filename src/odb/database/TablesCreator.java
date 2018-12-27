package odb.database;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class TablesCreator {

	List<TableDefinition> tables;

	public TablesCreator(TableDefinition... tables) {
		this.tables = Arrays.asList(tables);
	}
	
	public TablesCreator(List<TableDefinition> tables) {
		this.tables = tables;
	}
	
	public void dropAllIfExists() throws SQLException {
		for (TableDefinition td : tables) {
			if (tableExists(td.getTableName())) {
				dropTable(td);				
			}			
		}
	}

	public void createAllIfNotExists(Consumer<TableDefinition> consumer) throws SQLException {
		for (TableDefinition td : tables) {
			if (!tableExists(td.getTableName())) {
				createTable(td);				
			}
			consumer.accept(td);
		}
	}

	private boolean tableExists(String tableName) throws SQLException {
		return DBConnection.simpleQuery("user_tables", "table_name=upper('" + tableName + "')").anyRowExists();
	}

	private void createTable(TableDefinition table) throws SQLException {
		
		Statement statement = DBConnection.getInstance().getStatement();

		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE ");
		sb.append(table.getTableName());
		sb.append("(");
		boolean isFirst = true;
		for(ColumnDefinition col : table.getColumns()) {
			sb.append("\n");
			if (!isFirst) {
				sb.append(", ");
			}
			sb.append(col.getColumnName() + " ");
			sb.append(col.getColumnTypeDefinition());
			isFirst = false;
		}
		sb.append(")");
		System.out.println(sb.toString());
		statement.execute(sb.toString());

	}
	
	private void dropTable(TableDefinition table) throws SQLException {		

		Statement statement = DBConnection.getInstance().getStatement();

		StringBuilder sb = new StringBuilder();
		sb.append("DROP TABLE ");
		sb.append(table.getTableName());				
		System.out.println(sb.toString());
		statement.execute(sb.toString());
		
	}


}
