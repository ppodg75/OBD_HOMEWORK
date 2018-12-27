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
		App app = new App();
		app.run();
		System.out.println("Application exit!");
	}

	public App() {
		createStructuresAndData();
	}

	public void createStructuresAndData() {
		try {
			if (DBConnection.getInstance() != null) {
				if (DBConnection.getInstance().isConnected()) {
					System.out.println("Database connecction - success");
//					TablesDataProject.getInstance().createStructuresAndInsertData(true, false); // params: bool - drop before create tables, bool - insert data
					TablesDataProject.getInstance().createStructuresAndInsertData(false, false);
				}
			}
		} catch (Exception e) {
			System.err.println( e.getMessage() );
		} finally {
			DBConnection.close();
		}
	}

	private void run() {
		UI.showMainMenu();
		String operation = null;
		while (true) {
			operation = UI.selectOperation();
			if (UI.OPERATION_EXIT.equalsIgnoreCase(operation)) {
				UI.close();
				return;
			}
			try {
				doOperation(operation);
			} catch (SQLException e) {
				System.err.println( e.getMessage() );
			}
		}
	}

	private void doOperation(String op) throws SQLException {
		if (UI.OPERATION_LIST_STUDENTS.equals(op)) {
			TableDataList.showList(TablesDataProject.getStudents(), "Lista studentów: ");
		} else if (UI.OPERATION_LIST_TEACHERS.equals(op)) {
			TableDataList.showList(TablesDataProject.getTeachers(), "Lista nauczycieli: ");
		} else if (UI.OPERATION_LIST_DEGREES.equals(op)) {
			TableDataList.showList(TablesDataProject.getDegrees(), "Lista ocen: ");
		} else if (UI.OPERATION_LIST_SUBJECTS.equals(op)) {
			TableDataList.showList(TablesDataProject.getSubjects(), "Lista przedmiotów: ");
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
			Long idu = enterId("studenta");
			Long idn = enterId("nauczyciela");
			Long ido = enterId("oceny");
			Long idp = enterId("przedmiotu");
			String degree = enterDegree();
			if ("S".equalsIgnoreCase(degree) || "C".equalsIgnoreCase(degree)) {
				addDegreeToDatabase(idu, idn, ido, idp, degree);
			} else {
				System.out.println("Podano z³y typ oceny, ocenianie zakoñczone. Spróbuj od nowa!");
			}
		} catch (Exception e) {
			System.err.println( e.getMessage() );
		}
	}

	private Long enterId(String what) {
		System.out.print(String.format("Podaj ID %s: ", what));
		return new Long(UI.readId());
	}

	private String enterDegree() {
		System.out.print(String.format("Wprowadz rodzaj oceny [C - cz¹stkowa, S - semestralna]: "));
		return UI.readChar();
	}

	private void addDegreeToDatabase(Long idu, Long idn, Long ido, Long idp, String degree)
			throws SQLException, DBForeignConstraintViolation {
		DBConnection.getInstance().open();
		try {
			table_foreign_key_constraint_check_or_throw_exception(idu, TablesDataProject.getStudents());
			table_foreign_key_constraint_check_or_throw_exception(idn, TablesDataProject.getTeachers());
			table_foreign_key_constraint_check_or_throw_exception(ido, TablesDataProject.getDegrees());
			table_foreign_key_constraint_check_or_throw_exception(idp, TablesDataProject.getSubjects());
			InsertEngine ie = new DataCreator.InsertEngine(TablesDataProject.getIssuingGrades(), false);
			ie.values(idp, ido, idn, idu, degree);
		} finally {
			DBConnection.getInstance().close();
		}
	}

	private void table_foreign_key_constraint_check_or_throw_exception(Long id, TableDefinition table)
			throws SQLException, DBForeignConstraintViolation {
        SimpleQuery sq = new SimpleQuery(table.getTableName(),
				String.format(" %s = %d", table.getIdColumnName(), id));
		if (!sq.anyRowExists()) {
			throw new DBForeignConstraintViolation(table.getTableName(), id);
		}
	}

}
