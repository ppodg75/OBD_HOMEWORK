package odb.database;

import java.util.function.Function;

public enum Type {
	INT("integer", x -> "'"+String.valueOf(x)+"'" ),
	CHAR("char", x -> "'"+x+"'" ),
	NUMERIC("numeric", x -> "'"+dotToComma(String.valueOf(x))+"'" ),
	TEXT("varchar2", x -> "'"+x+"'" );
	
	private String desc; 
	private Function<Object, String> mapValToStringFunction;
	
	Type(String desc, Function<Object, String> mapValToStringFunction) {
		this.desc = desc;
		this.mapValToStringFunction = mapValToStringFunction;
	}
	
	public String mapValToString(Object value) {
		if (value==null || (value instanceof String && ((String)value).isEmpty())) {
			return "null";
		} else {
		   return mapValToStringFunction.apply(value);
		}
	}

	public String getDesc() {
		return desc;
	}
	
	private static String dotToComma(String val) {
		return val.replace('.',',');
	}
	
	
}