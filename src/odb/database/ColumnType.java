package odb.database;

import java.util.function.Function;

import javax.xml.soap.Text;

public class ColumnType {
	
	private Type type;
	private int length;
	private int precision;
	
	public ColumnType(Type type, int length, int precision) {		
		this.type = type;
		this.length = length;
		this.precision = precision;
	}
	
	public ColumnType(Type type, int length) {		
		this.type = type;
		this.length = length;
		this.precision = 0;
	}
	
	public ColumnType(Type type) {		
		this.type = type;
		this.length = 0;
		this.precision = 0;
	}
	
	public Type getTypeName() {
		return type;
	}
	
	public int getLength() {
		return length;
	}
	
	public String valToDbVal(Object o) {
		return type.mapValToString(o);
	}
	
	public String getTextDefinition() {
		String typeName = type.getDesc();
		switch (type) {
		case CHAR:
		case TEXT: return typeName+"("+length+")";		  
		case INT: return typeName; 
		case NUMERIC :return typeName+"("+length+","+precision+")";
		}
		return "";
	}	

	
	public static ColumnType intColumn() { return new ColumnType(Type.INT); }
	public static ColumnType textColumn(int maxLength) { return new ColumnType(Type.TEXT, maxLength); }
	public static ColumnType charsColumn(int fixedLength) { return new ColumnType(Type.CHAR, fixedLength); }
	public static ColumnType numericColumn(int length, int precision) { return new ColumnType(Type.NUMERIC, length, precision); }

}
