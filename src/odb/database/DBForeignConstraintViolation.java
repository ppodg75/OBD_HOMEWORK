package odb.database;

public class DBForeignConstraintViolation extends Exception {
	
	public DBForeignConstraintViolation() {
		super("ORA-02291: naruszono wi�zy sp�jno�ci (PPODGORS.SYS_C00703428) - nie znaleziono klucza nadrz�dnego.");
	}

}
