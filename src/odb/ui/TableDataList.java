package odb.ui;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import odb.database.DBConnection;
import odb.database.TableDefinition;
import odb.database.Type;

public class TableDataList {

	public static void showList(TableDefinition table, String prompt) throws SQLException, ClassNotFoundException {
		DBConnection db = DBConnection.getInstance();

		Statement statement = db.getStatement();
		List<Integer> orderedColumnLengthList = new ArrayList<>();
		String query = makeQuerySelectAll(table, orderedColumnLengthList);
//		System.out.println("showList::query=" + query);
		ResultSet rs = statement.executeQuery(query);
		try {
			while (rs.next()) {
				System.out.print("ID=" + padLeft(rs.getString(1), 5) + " >>> ");
				for (int i = 2; i <= table.getColumns().size(); i++) {
					System.out.print(padLeft(rs.getString(i), orderedColumnLengthList.get(i - 2)) + " | ");
				}
				System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String padLeft(String s, int n) {
		if (n == 0) {
			return String.format("%1$10s", s);
		} else {
			return String.format("%1$" + n + "s", s);
		}
	}

	private static String makeQuerySelectAll(TableDefinition table, List<Integer> orderedColumnList) {
		StringBuilder sb = new StringBuilder();
		String idColumnName = table.getIdColumnName();
		sb.append("SELECT ");
		sb.append(idColumnName + " AS ID");
		table.getColumns().stream().forEach(column -> {
			if (!column.isId()) {
				if (column.getColumnType().getTypeName() == Type.FLOAT) {
					sb.append(", to_char(" + column.getColumnName() + ") AS F_" + column.getColumnName());
				} else {
					sb.append(", " + column.getColumnName());
				}
				orderedColumnList.add(column.getLength());
			}
		});
		sb.append(" FROM ");
		sb.append(table.getTableName());
		sb.append(" ORDER BY ");
		sb.append(idColumnName);
		return sb.toString();
	}

	public static void showListIssuedGrades(String prompt) throws SQLException, ClassNotFoundException {
		System.out.println(prompt);
		DBConnection db = DBConnection.getInstance();

		Statement statement = db.getStatement();
		StringBuilder query = new StringBuilder();
		query.append("SELECT ");
		query.append(make("S", "STUDENT", "NAZWISKO", "IMIE")).append(", ");
		query.append(make("N", "NAUCZYCIEL", "NAZWISKO", "IMIE")).append(", ");
		query.append(make("O", "OCENA", "WARTOSC_OPISOWA", "WARTOSC_NUMERYCZNA")).append(", ");
		query.append("P.NAZWA AS PRZEDMIOT").append(", ");
		query.append("DECODE(OC.RODZAJ_OCENY,'C','CZASTKOWA','S','SEMESTRALNA') AS OCENIANIE");
		query.append(" FROM OCENIANIE OC");
		query.append(" INNER JOIN UCZEN S ON (S.IDU = OC.IDU)");
		query.append(" INNER JOIN NAUCZYCIEL N ON (N.IDN = OC.IDN)");
		query.append(" INNER JOIN OCENA O ON (O.IDO = OC.IDO)");
		query.append(" INNER JOIN PRZEDMIOT P ON (P.IDP = OC.IDP)");
//            System.out.println("showListIssuedGrades::q="+query.toString());  

		StringBuilder sb = new StringBuilder();
		sb.append(disp("STUDENT", 51));
		sb.append(disp("NAUCZYCIEL", 51));
		sb.append(disp("OCENA", 25));
		sb.append(disp("PRZEDMIOT", 25));
		sb.append(disp("Czast/Sem.", 20));
		System.out.println(sb.toString());

		ResultSet rs = statement.executeQuery(query.toString());
		while (rs.next()) {
			sb = new StringBuilder();
			sb.append(disp(rs.getString(1), 51));
			sb.append(disp(rs.getString(2), 51));
			sb.append(disp(rs.getString(3), 25));
			sb.append(disp(rs.getString(4), 25));
			sb.append(disp(rs.getString(5), 20));
			System.out.println(sb.toString());

			;
		}

	}

	private static String disp(String s, int spaces) {
		return padLeft(s, spaces) + " | ";
	}

	private static String make(String alias, String asFieldName, String... fields) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String f : fields) {
			if (!first) {
				sb.append("||' '||");
			}
			sb.append(alias).append(".").append(f);
			first = false;
		}
		sb.append(" AS ").append(asFieldName);
		return sb.toString();
	}

}
