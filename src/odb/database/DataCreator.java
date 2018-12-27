package odb.database;

import java.sql.Statement;
import java.util.Map;

public class DataCreator {

	public static InsertEngine insertWithCheckExists(TableDefinition def) {
		return new InsertEngine(def, true);
	}

	public static class InsertEngine {

		private TableDefinition def;
		private boolean checkIdExists;

		public InsertEngine(TableDefinition def, boolean checkIdExists) {
			this.def = def;
			this.checkIdExists = checkIdExists;
		}

		public boolean values(Object... objects) {
	
			int colIndex = 0;

			StringBuilder colNames = new StringBuilder();
			StringBuilder colVals = new StringBuilder();
			String valId = "";
			String colNameId = "";
			for (ColumnDefinition col : def.getColumns()) {
				if (colIndex > 0) {
					colNames.append(", ");
					colVals.append(", ");
				}
				colNames.append(col.getColumnName());
				colVals.append(col.getColumnType().valToDbVal(objects[colIndex]));

				if (col.isId()) {
					valId = col.getColumnType().valToDbVal(objects[colIndex]);
					colNameId = col.getColumnName();
				}
				colIndex++;

			}

			if (checkIdExists && idExists(def.getTableName(), colNameId, valId)) {
				System.out.printf("DB> Table: %s - row with id: %s exists! The insert has been skipped!\n", def.getTableName(), valId);
				return true;
			}

			StringBuilder insertDML = new StringBuilder();

			insertDML.append("insert into " + def.getTableName() + " (");
			insertDML.append(colNames);
			insertDML.append(") values (");
			insertDML.append(colVals);
			insertDML.append(")");

			System.out.println(insertDML);
			
			try {
				Statement stat = DBConnection.getInstance().getStatement();
				stat.execute(insertDML.toString());
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		private boolean idExists(String tableName, String colNameId, String val) {
			try {
				String where = colNameId + " = " + val;
				SimpleQuery sq = new SimpleQuery(tableName, where);
				return sq.anyRowExists();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

	}
}
