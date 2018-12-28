package odb.database;

public class ColumnDefinition {
	
	private boolean isId;

	private String columnName;
	private ColumnType columnType;	
	
	public ColumnDefinition(String columnName, ColumnType columnType, boolean isId) {		
		this.isId = isId;
		this.columnName = columnName;
		this.columnType = columnType;
	}

	public boolean isId() {
		return isId;
	}
	
	public static ColumnDefinition createId(String name) {
		  return new ColumnDefinition(name, ColumnType.intColumn(), true);
	}
	
	public static ColumnDefinition createInt(String name) {
		  return new ColumnDefinition(name, ColumnType.intColumn(), false);
	}
	
	public static ColumnDefinition createText(String name, int maxLenght) {
		  return new ColumnDefinition(name, ColumnType.textColumn(maxLenght), false);
	}
	
	public static ColumnDefinition createNumeric(String name, int lenght, int precision) {
		  return new ColumnDefinition(name, ColumnType.numericColumn(lenght, precision), false);
	}
	
	public static ColumnDefinition createChars(String name, int lenght) {
		  return new ColumnDefinition(name, ColumnType.charsColumn(lenght), false);
	}
	
	public static ColumnDefinition createFloat(String name) {
		  return new ColumnDefinition(name, ColumnType.floatColumn(), false);
	}

	public String getColumnName() {
		return columnName;
	}

	public ColumnType getColumnType() {
		return columnType;
	}
	
	public String getColumnTypeDefinition() {
		return columnType.getTextDefinition();
	}
	
	public Integer getLength() {
		return columnType.getLength();
	}
		
	
}
