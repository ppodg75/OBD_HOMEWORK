package odb.ui;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class UI {

	private static final Scanner sc = new Scanner(System.in);

	public final static String OPERATION_EXIT = "W";
	public final static String OPERATION_ISSUING_GRADES = "O";
	public final static String OPERATION_SHOW_MENU = "M";
	public final static String OPERATION_LIST_STUDENTS = "LS";
	public final static String OPERATION_LIST_TEACHERS = "LN";
	public final static String OPERATION_LIST_DEGREES = "LO";
	public final static String OPERATION_LIST_SUBJECTS = "LP";
	public final static String OPERATION_LIST_ISSUED_GRADES = "LW";

	private final static List<String> AVAILABLE_OPERATIONS = Arrays.asList(OPERATION_SHOW_MENU, OPERATION_LIST_STUDENTS,
			OPERATION_LIST_TEACHERS, OPERATION_LIST_DEGREES, OPERATION_LIST_SUBJECTS, OPERATION_ISSUING_GRADES,
			OPERATION_LIST_ISSUED_GRADES, OPERATION_EXIT);

	public static void close() {
		sc.close();
	}

	public static void showMainMenu() {
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("Lista poleceñ:");
		System.out.println("------------------");
		System.out.println("M - poka¿ TO menu");
		System.out.println("LS - Lista studentów");
		System.out.println("LN - Lista nauczycieli");
		System.out.println("LO - Lista ocen");
		System.out.println("LP - Lista przedmiotów");
		System.out.println("LW - Lista wystawionych ocen");
		System.out.println("O - Ocenianie");
		System.out.println("W - wyjœcie");
	}

	public static String selectOperation() {
		String op;

		System.out.println("Wprowad¿ polecenie: ");
		do {
			String operation = sc.nextLine();
			op = operation.toUpperCase();
			if (!AVAILABLE_OPERATIONS.contains(op)) {
				System.out.println("Nieznane polecenie!");
			} else {
				System.out.println("wykonujê polecenie: " + op);
			}
		} while (!AVAILABLE_OPERATIONS.contains(op));

		return op;

	}

	public static int readId() {
		int id = 0;
		id = sc.nextInt();
		return id;
	}

	public static String readChar() {
		String c;
		sc.nextLine();
		c = sc.nextLine();
		return c.substring(0, 1).toUpperCase();
	}

}
