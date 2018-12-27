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

	private static TableDefinition subjects;
	private static TableDefinition teachers;
	private static TableDefinition students;
	private static TableDefinition degrees;
	private static TableDefinition issuingGrades;
	
	private static TablesDataProject instance = null;

	private TablesDataProject() {
		subjects = new TableDefinition(SUBJECT_TABLE_NAME).addIdColumn("idp").addTextColumn("nazwa", 20);

		teachers = new TableDefinition(TEACHER_TABLE_NAME).addIdColumn("idn").addTextColumn("nazwisko", 30)
				.addTextColumn("imie", 20);

		students = new TableDefinition(STUDENTS_TABLE_NAME).addIdColumn("idu").addTextColumn("nazwisko", 30)
				.addTextColumn("imie", 20);

		degrees = new TableDefinition(DEGREES_TABLE_NAME).addIdColumn("ido").addTextColumn("wartosc_opisowa", 20)
				.addFloatColumn("wartosc_numeryczna", 10, 2);

		issuingGrades = new TableDefinition(ISSUING_GRADES_TABLE_NAME).addIntColumn("idp").addIntColumn("ido")
				.addIntColumn("idn").addIntColumn("idu").addCharColumn("rodzaj_oceny", 1);

	}
	
	
	public static TablesDataProject getInstance() {
		if (instance==null) {
			instance = new TablesDataProject();
		}
		return instance;
	}

	public void createStructuresAndInsertData(boolean dropAllTablesBefore, boolean insertData) throws SQLException {
		TablesCreator tablesCreator = new TablesCreator(subjects, teachers, students, degrees, issuingGrades);
		
		if (dropAllTablesBefore) {
			tablesCreator.dropAllIfExists();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		tablesCreator.createAllIfNotExists(x -> {
		 if (insertData) {	insertData(x); }
		});
	}

	public void insertData(TableDefinition tableDef) {
		System.out.println("insertData into table: "+tableDef.getTableName());
		switch (tableDef.getTableName()) {
		case STUDENTS_TABLE_NAME:
			insertStudents(); break;
		case TEACHER_TABLE_NAME:
			insertTeachers(); break;
		case SUBJECT_TABLE_NAME:
			insertSubjects(); break;
		case DEGREES_TABLE_NAME:
			insertDegrees(); break;
		}
	}

	private void insertStudents() {
		DataCreator.insertWithCheckExists(students).values(1, "Jan", "Kowalski");
		DataCreator.insertWithCheckExists(students).values(2, "Wojtek", "Lutek");
		DataCreator.insertWithCheckExists(students).values(2, "Kazimierz", "Wielki"); // uczen nie powinien zostaæ
																						// dodany
		DataCreator.insertWithCheckExists(students).values(3, "Piotr", "Wielki");

		IntStream.range(10, 20)
				.forEach(x -> DataCreator.insertWithCheckExists(students).values(x, "Student " + x, "Zaradny"));

	}

	private void insertTeachers() {
		DataCreator.insertWithCheckExists(teachers).values(1, "Wojtek", "Psikus");
		DataCreator.insertWithCheckExists(teachers).values(2, "Janek", "Niejadek");
		DataCreator.insertWithCheckExists(teachers).values(4, "Emil", "Zaradny");
		IntStream.range(5, 10)
				.forEach(x -> DataCreator.insertWithCheckExists(teachers).values(x, "Nauczyciel " + x, "nieznany"));
	}

	private void insertSubjects() {
		DataCreator.insertWithCheckExists(subjects).values(1, "Historia sztuki");
		DataCreator.insertWithCheckExists(subjects).values(2, "WF");
		DataCreator.insertWithCheckExists(subjects).values(3, "Judo");
		IntStream.range(4, 7).forEach(x -> DataCreator.insertWithCheckExists(subjects).values(x, "Przedmiot " + x));
	}

	private void insertDegrees() {
		int idDegree = 1;
		for (Double degree : Arrays.asList(Double.valueOf(1.5), Double.valueOf(1.75), Double.valueOf(2),
				Double.valueOf(3), Double.valueOf(4), Double.valueOf(5))) {
			DataCreator.insertWithCheckExists(degrees).values(idDegree, getDegreeDesc(degree), degree);
			idDegree++;
		}

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


	public static TableDefinition getSubjects() {
		return subjects;
	}


	public static TableDefinition getTeachers() {
		return teachers;
	}


	public static TableDefinition getStudents() {
		return students;
	}


	public static TableDefinition getDegrees() {
		return degrees;
	}


	public static TableDefinition getIssuingGrades() {
		return issuingGrades;
	}
	
	

}
