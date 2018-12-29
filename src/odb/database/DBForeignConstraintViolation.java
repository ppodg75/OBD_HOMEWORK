package odb.database;

public class DBForeignConstraintViolation extends Exception {
	
	public DBForeignConstraintViolation(String tableName, int id) {
		super(String.format("Naruszono wiêzy spójnoœci - nie znaleziono klucza nadrzêdnego o id=%d w tabeli \"%s\".",id, tableName));
	}

}
