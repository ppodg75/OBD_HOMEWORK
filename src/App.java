import java.sql.SQLException;

import odb.database.DBConnection;
import odb.database.DBForeignConstraintViolation;
import odb.database.DataCreator;
import odb.database.DataCreator.InsertEngine;
import odb.database.SimpleQuery;
import odb.database.TableDefinition;
import odb.database.TablesDataProject;
import odb.ui.TableDataList;
import odb.ui.UI;

public class App {

	public static void main(String... args) {
		try {
			App app = new App();
			app.run();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		System.out.println("Wyj�cie z aplikacji!");
	}

	public App() {
	}

	public void createStructuresAndData() throws Exception {
		System.out.println("Zaczekaj. Tworzenie struktur i wprowadzanie przyk�adowych danych.");
		TablesDataProject tdp = TablesDataProject.getInstance();
		tdp.createStructuresAndInsertData(true, true);
	}

	private void run() throws Exception {
		DBConnection db = null;
		try {
			db = DBConnection.getInstance();
			if (db != null) {
				db.open();
				if (db.isConnected()) {
					System.out.println("Uda�o pod��czy� si� do bazy!");
					createStructuresAndData();
					doOperations();
				} else {
					throw new Exception("Nie uda�o si� pod��czy� do bazy!");
				}
			}
		} finally {
			db.close();
		}
	}
	
	private void doOperations() throws Exception {
		UI.showMainMenu();
		String operation = null;
		while (true) {
			operation = UI.selectOperation();
			if (UI.OPERATION_EXIT.equalsIgnoreCase(operation)) {
				UI.close();
				return;
			}
			doOperation(operation);
		}
	}

	private void doOperation(String op) throws Exception {
		TablesDataProject tdp = TablesDataProject.getInstance();
		if (UI.OPERATION_LIST_STUDENTS.equals(op)) {
			TableDataList.showList(tdp.getStudents(), "Lista student�w: ");
		} else if (UI.OPERATION_LIST_TEACHERS.equals(op)) {
			TableDataList.showList(tdp.getTeachers(), "Lista nauczycieli: ");
		} else if (UI.OPERATION_LIST_DEGREES.equals(op)) {
			TableDataList.showList(tdp.getDegrees(), "Lista ocen: ");
		} else if (UI.OPERATION_LIST_SUBJECTS.equals(op)) {
			TableDataList.showList(tdp.getSubjects(), "Lista przedmiot�w: ");
		} else if (UI.OPERATION_LIST_ISSUED_GRADES.equals(op)) {
			TableDataList.showListIssuedGrades("Lista wystawionych ocen:");
		} else if (UI.OPERATION_ISSUING_GRADES.equals(op)) {
			issueGrades();
		} else if (UI.OPERATION_SHOW_MENU.equals(op)) {
			UI.showMainMenu();
		}
		;
	}

	private void issueGrades() {
		System.out.println("Podaj odpowiednie identyfikatory do ocenienia studenta: ");
		try {
			int idu = enterId("studenta");
			int idn = enterId("nauczyciela");
			int ido = enterId("oceny");
			int idp = enterId("przedmiotu");
			String degree = enterDegree();
			if ("S".equalsIgnoreCase(degree) || "C".equalsIgnoreCase(degree)) {
				addDegreeToDatabase(idu, idn, ido, idp, degree);
				System.out.println("Ocena zosta�a wprowadzona do systemu!");
			} else {
				System.out.println("Podano z�y typ oceny, ocenianie zako�czone. Spr�buj od nowa!");
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	private int enterId(String what) {
		System.out.print(String.format("Podaj ID %s: ", what));
		return UI.readId();
	}

	private String enterDegree() {
		System.out.print(String.format("Wprowadz rodzaj oceny [C - cz�stkowa, S - semestralna]: "));
		return UI.readChar();
	}

	private void addDegreeToDatabase(int idu, int idn, int ido, int idp, String degree) throws Exception {
		TablesDataProject tdp = TablesDataProject.getInstance();
		
		table_foreign_key_constraint_check_or_throw_exception(idu, tdp.getStudents());
		table_foreign_key_constraint_check_or_throw_exception(idn, tdp.getTeachers());
		table_foreign_key_constraint_check_or_throw_exception(ido, tdp.getDegrees());
		table_foreign_key_constraint_check_or_throw_exception(idp, tdp.getSubjects());

		InsertEngine ie = new DataCreator.InsertEngine(tdp.getIssuingGrades(), false);
		
		ie.values(idp, ido, idn, idu, degree);
		
	}

	private void table_foreign_key_constraint_check_or_throw_exception(int id, TableDefinition table)
			throws Exception {
		//		if (!table.hasId(id))
		SimpleQuery sq = new SimpleQuery(table.getTableName(), String.format(" %s = %d", table.getIdColumnName(), id));
		if (!sq.anyRowExists())
		{
			throw new DBForeignConstraintViolation();
		}
	}

}
