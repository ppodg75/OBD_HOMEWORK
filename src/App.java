import java.sql.SQLException;
import java.sql.SQLRecoverableException;
import java.util.InputMismatchException;

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
		App app = new App();
		app.run();
		System.out.println("Wyjœcie z aplikacji");
	}

	private void run() {
		DBConnection db = null;
		try {
			db = DBConnection.getInstance();
			if (db != null) {
				db.open();
				System.out.println("Próba po³¹czenia z baz¹ zakoñczona sukcesem!");
				createStructuresAndData();
				doOperations();
			} else {
				System.out.print("Próba po³¹czenia z baz¹ zakoñczona niepowodzeniem!");
				return;
			}
		} catch (ClassNotFoundException e) {
			System.out.println("Nie znaleziono odpowiedniego drivera do obs³ugi bazy!");
		} catch (SQLRecoverableException e) {
			System.out.println("Brak po³¹czenia z baz¹!");
		} catch (SQLException e) {
			System.out.println("Problem z nawi¹zaniem po³¹czenia z baz¹!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null)
				db.close();
		}
	}

	public void createStructuresAndData() throws ClassNotFoundException, SQLException {
		System.out.println("Zaczekaj. Tworzenie struktur i wprowadzanie przyk³adowych danych.");
		TablesDataProject tdp = TablesDataProject.getInstance();
		tdp.createStructuresAndInsertData(true, true);
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
			if (operation != null) {
				try {
					doOperation(operation);
				} catch (DBForeignConstraintViolation e) {
				} catch (InputMismatchException e) {
					System.out.println("Podano nieprawid³ow¹ liczbê dla ID! Powtórz polecenie od pocz¹tku.");
					UI.clearScanner();
				} catch (StringIndexOutOfBoundsException e) {
					System.out.println("Podano nieprawid³owy rodzaj oceny!");					
				} catch (Exception e) {
					throw e;
				}
			}
		}
	}

	private void doOperation(String op) throws Exception {
		TablesDataProject tdp = TablesDataProject.getInstance();
		if (UI.OPERATION_LIST_STUDENTS.equals(op)) {
			TableDataList.showList(tdp.getStudents(), "Lista studentów: ");
		} else if (UI.OPERATION_LIST_TEACHERS.equals(op)) {
			TableDataList.showList(tdp.getTeachers(), "Lista nauczycieli: ");
		} else if (UI.OPERATION_LIST_DEGREES.equals(op)) {
			TableDataList.showList(tdp.getDegrees(), "Lista ocen: ");
		} else if (UI.OPERATION_LIST_SUBJECTS.equals(op)) {
			TableDataList.showList(tdp.getSubjects(), "Lista przedmiotów: ");
		} else if (UI.OPERATION_LIST_ISSUED_GRADES.equals(op)) {
			TableDataList.showListIssuedGrades("Lista wystawionych ocen:");
		} else if (UI.OPERATION_ISSUING_GRADES.equals(op)) {
			issueGrades();
		} else if (UI.OPERATION_SHOW_MENU.equals(op)) {
			UI.showMainMenu();
		}
		;
	}

	private void issueGrades() throws Exception {
		System.out.println("Podaj odpowiednie identyfikatory do ocenienia studenta: ");

		int idu = enterId("ucznia");
		int idn = enterId("nauczyciela");
		int ido = enterId("oceny");
		int idp = enterId("przedmiotu");
		UI.clearScanner();
		String degree = enterDegree();
		if ("S".equalsIgnoreCase(degree) || "C".equalsIgnoreCase(degree)) {
			try {
				System.out.println();				
				addDegreeToDatabase(idu, idn, ido, idp, degree);
				System.out.println("Ocena zosta³a wprowadzona do systemu!");
			} catch (DBForeignConstraintViolation e) {
//				System.out.println("Ocena nie zosta³a wprowadzona do systemu z powodu poni¿szego b³êdu:");
				System.out.println(e.getMessage());
				throw e;
			}
		} else {
			System.out.println("Podano z³y typ oceny, ocenianie zakoñczone. Spróbuj od nowa!");
		}

	}

	private int enterId(String what) {
		System.out.print(String.format("Podaj ID %s: ", what));
		int id = UI.readId();
		return id;
	}

	private String enterDegree() {
		System.out.print(String.format("Wprowadz rodzaj oceny [C - cz¹stkowa, S - semestralna]: "));
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

	private void table_foreign_key_constraint_check_or_throw_exception(int id, TableDefinition table) throws Exception {
		SimpleQuery sq = new SimpleQuery(table.getTableName(), String.format(" %s = %d", table.getIdColumnName(), id));
		if (!sq.anyRowExists()) {
			throw new DBForeignConstraintViolation(table.getTableName(), id);
		}
	}

}
