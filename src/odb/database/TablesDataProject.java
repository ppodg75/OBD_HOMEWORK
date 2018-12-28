package odb.database;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.IntStream;

public class TablesDataProject {

	public static final String STUDENTS_TABLE_NAME = "Uczen";
	public static final String DEGREES_TABLE_NAME = "Ocena";
	public static final String TEACHER_TABLE_NAME = "Nauczyciel";
	public static final String SUBJECT_TABLE_NAME = "Przedmiot";
	public static final String ISSUING_GRADES_TABLE_NAME = "Ocenianie";

	private TableDefinition subjects;
	private TableDefinition teachers;
	private TableDefinition students;
	private TableDefinition degrees;
	private TableDefinition issuingGrades;

	private static TablesDataProject instance = null;

	private TablesDataProject() {
		subjects = new TableDefinition(SUBJECT_TABLE_NAME).addIdColumn("idp").addCharColumn("nazwa", 20);

		teachers = new TableDefinition(TEACHER_TABLE_NAME).addIdColumn("idn").addCharColumn("nazwisko", 30)
				.addCharColumn("imie", 20);

		students = new TableDefinition(STUDENTS_TABLE_NAME).addIdColumn("idu").addCharColumn("nazwisko", 30)
				.addCharColumn("imie", 20);

		degrees = new TableDefinition(DEGREES_TABLE_NAME).addIdColumn("ido").addCharColumn("wartosc_opisowa", 20)
				.addFloatColumn("wartosc_numeryczna", 10, 2);

		issuingGrades = new TableDefinition(ISSUING_GRADES_TABLE_NAME).addIntColumn("idp").addIntColumn("ido")
				.addIntColumn("idn").addIntColumn("idu").addCharColumn("rodzaj_oceny", 1);

	}

	public static TablesDataProject getInstance() {
		if (instance == null) {
			instance = new TablesDataProject();
		}
		return instance;
	}

	public void createStructuresAndInsertData(boolean dropAllTablesBefore, boolean insertData)
			throws SQLException, ClassNotFoundException {
		TablesCreator tablesCreator = new TablesCreator(subjects, teachers, students, degrees, issuingGrades);

		if (dropAllTablesBefore) {
			tablesCreator.dropAllIfExists();
		}

		tablesCreator.createAllIfNotExists(x -> {
			if (insertData) {
				try {
					insertData(x);
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			}
		});

	}

	public void insertData(TableDefinition tableDef) throws ClassNotFoundException, SQLException {
//		System.out.println("insertData into table: "+tableDef.getTableName());
		switch (tableDef.getTableName()) {
		case STUDENTS_TABLE_NAME:
			insertStudents(tableDef);
			break;
		case TEACHER_TABLE_NAME:
			insertTeachers(tableDef);
			break;
		case SUBJECT_TABLE_NAME:
			insertSubjects(tableDef);
			break;
		case DEGREES_TABLE_NAME:
			insertDegrees(tableDef);
			break;
		}
	}

	private void insertStudents(TableDefinition table) throws ClassNotFoundException, SQLException {
		DataCreator.insertWithCheckExists(students).values(1, "Jan", "Kowalski");
		DataCreator.insertWithCheckExists(students).values(2, "Wojtek", "Lutek");
		DataCreator.insertWithCheckExists(students).values(3, "Piotr", "Wielki");
		table.setMinId(1);
		table.setMaxId(3);
	}

	private void insertTeachers(TableDefinition table) throws ClassNotFoundException, SQLException {
		DataCreator.insertWithCheckExists(teachers).values(1, "Kazimierz", "Dolny");
		DataCreator.insertWithCheckExists(teachers).values(2, "Janek", "Niejadek");
		DataCreator.insertWithCheckExists(teachers).values(3, "Emil", "Zaradny");
		table.setMinId(1);
		table.setMaxId(3);
	}

	private void insertSubjects(TableDefinition table) throws ClassNotFoundException, SQLException {
		DataCreator.insertWithCheckExists(subjects).values(1, "Historia sztuki");
		DataCreator.insertWithCheckExists(subjects).values(2, "WF");
		DataCreator.insertWithCheckExists(subjects).values(3, "Judo");
		table.setMinId(1);
		table.setMaxId(3);
	}

	private void insertDegrees(TableDefinition table) throws ClassNotFoundException, SQLException {
		int idDegree = 1;
		for (Double degree : Arrays.asList(Double.valueOf(1.5), Double.valueOf(1.75), Double.valueOf(2),
				Double.valueOf(3), Double.valueOf(4), Double.valueOf(5))) {
			DataCreator.insertWithCheckExists(degrees).values(idDegree, getDegreeDesc(degree), degree);
			idDegree++;
		}
		table.setMinId(1);
		table.setMaxId(5);

	}

	private String getDegreeDesc(Double degree) {
		if (degree == 1.5) {
			return "pa³a +";
		}
		if (degree == 1.75) {
			return "dwa -";
		}
		if (degree == 2) {
			return "dwa";
		}
		if (degree == 3) {
			return "trzy";
		}
		if (degree == 4) {
			return "cztery";
		}
		if (degree == 5) {
			return "piêæ";
		}
		return "";
	}

	public TableDefinition getSubjects() {
		return subjects;
	}

	public TableDefinition getTeachers() {
		return teachers;
	}

	public TableDefinition getStudents() {
		return students;
	}

	public TableDefinition getDegrees() {
		return degrees;
	}

	public TableDefinition getIssuingGrades() {
		return issuingGrades;
	}

}
