package odb.database;

import java.util.ArrayList;
import java.util.List;

public class TableDefinition {
	
	private String tableName;
	private List<ColumnDefinition> columns = new ArrayList<>();
	private int minId = 0;
	private int maxId = 0;
	
	public TableDefinition(String tableName) {
		this.tableName = tableName;			
	}
	
	public TableDefinition addIdColumn(String columnName) {		
		columns.add(ColumnDefinition.createId(columnName) );		
		return this;
	}
	
	public TableDefinition addIntColumn(String columnName) {
		columns.add(ColumnDefinition.createInt(columnName) );
		return this;
	}	
	
	public TableDefinition addTextColumn(String columnName, int length) {
		columns.add(ColumnDefinition.createText(columnName, length) );
		return this;
	}
	
	public TableDefinition addCharColumn(String columnName, int length) {
		columns.add(ColumnDefinition.createChars(columnName, length) );
		return this;
	}
	
	public TableDefinition addFloatColumn(String columnName, int length, int precision) {
		columns.add(ColumnDefinition.createFloat(columnName) );
		return this;
	}
	
	public String getTableName() {
		return tableName;
	}
			
	public List<ColumnDefinition> getColumns() {
		return columns;
	}
	
	public String getIdColumnName() {
		return columns.stream().filter(ColumnDefinition::isId).map(ColumnDefinition::getColumnName).findFirst().orElse("");
	}

	public int getMinId() {
		return minId;
	}

	public void setMinId(int minId) {
		this.minId = minId;
	}

	public int getMaxId() {
		return maxId;
	}

	public void setMaxId(int maxId) {
		this.maxId = maxId;
	}
	
	public boolean hasId(int id) {
		return (id>=minId && id<=maxId);
	}

}
