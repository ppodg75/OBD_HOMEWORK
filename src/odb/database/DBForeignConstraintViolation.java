package odb.database;

public class DBForeignConstraintViolation extends Exception {
	
	public DBForeignConstraintViolation(String tableName, Long id) {
		super(String.format("ORA-02291: naruszono wi�zy sp�jno�ci (PPODGORS.SYS_C00703428) - nie znaleziono klucza nadrz�dnego (brak id=%d w tabeli %s)", id, tableName));
	}

}
