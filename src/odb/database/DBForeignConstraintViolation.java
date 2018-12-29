package odb.database;

public class DBForeignConstraintViolation extends Exception {
	
	public DBForeignConstraintViolation(String tableName, int id) {
		super(String.format("Naruszono wi�zy sp�jno�ci - nie znaleziono klucza nadrz�dnego o id=%d w tabeli \"%s\".",id, tableName));
	}

}
