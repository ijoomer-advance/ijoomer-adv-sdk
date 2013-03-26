package com.smart.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SmartTable {

	private SQLiteDatabase db;

	private String tblName;
	private int colCount;
	private int rowCount;
	private String[] colNames;

	public SmartTable(SQLiteDatabase db, String tblName) {
		this.db = db;
		this.tblName = tblName;

		Cursor cur = this.db.rawQuery("select * from " + tblName, new String[0]);
		this.colCount = cur.getColumnCount();
		this.rowCount = cur.getCount();
		this.colNames = new String[cur.getColumnCount()];
		cur.moveToFirst();

		for (int idx = 0; idx < this.colCount; idx++) {
			colNames[idx] = cur.getColumnName(idx);
		}

		cur.close();
	}

	public int getColCount() {
		Cursor cur = this.db.rawQuery("select * from " + tblName, new String[0]);
		this.colCount = cur.getColumnCount();
		cur.close();

		return this.colCount;
	}

	public int getRowCount() {
		Cursor cur = this.db.rawQuery("select * from " + tblName, new String[0]);
		this.rowCount = cur.getCount();
		cur.close();
		return this.rowCount;
	}

	public String getTableName() {
		return this.tblName;
	}

	public String[] getColNames() {
		Cursor cur = this.db.rawQuery("select * from " + tblName, new String[0]);
		this.colNames = new String[cur.getColumnCount()];
		cur.close();
		return colNames;
	}

	public List<List<String>> readRows(String[] colNames) {
		List<List<String>> list = new ArrayList<List<String>>();
		Cursor cursor = this.db.query(this.tblName, colNames, null, null, null, null, null);
		this.colCount = cursor.getColumnCount();
		this.rowCount = cursor.getCount();
		if (cursor.moveToFirst()) {
			do {
				List<String> col = new ArrayList<String>();
				for (int i = 0; i < colNames.length; i++) {
					col.add(cursor.getString(i));
				}
				list.add(col);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}

	public ArrayList<HashMap<String, String>> readRowSQL(String rawSQL, String[] rawSQLSelectionArguments) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		Cursor cursor = this.db.rawQuery(rawSQL, rawSQLSelectionArguments);
		this.colCount = cursor.getColumnCount();
		this.rowCount = cursor.getCount();
		if (cursor.moveToFirst()) {
			do {
				HashMap<String, String> col = new HashMap<String, String>();
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					col.put(cursor.getColumnName(i), cursor.getString(i));
				}
				list.add(col);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}

	public List<List<String>> readSpecificRows(String[] colNames, String sel, String[] selArgs, String groupBy, String having, String orderBy) {
		List<List<String>> list = new ArrayList<List<String>>();
		Cursor cursor = this.db.query(this.tblName, colNames, sel, selArgs, groupBy, having, orderBy);
		this.colCount = cursor.getColumnCount();
		this.rowCount = cursor.getCount();
		if (cursor.moveToFirst()) {
			do {
				List<String> col = new ArrayList<String>();
				for (int i = 0; i < colNames.length; i++) {
					col.add(cursor.getString(i));
				}
				list.add(col);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}

	public void insertRow(String[] colNames, String[] values) throws Exception {
		if (colNames.length != values.length) {
			Exception t = new Exception("Number of values do not match with number of columns.");
			throw t;
		}
		String sql = "insert into " + this.tblName + "(";
		String sql_colNames = "";
		String sql_values = "";
		for (int i = 0; i < colNames.length; i++) {
			sql_colNames += colNames[i];
			sql_values += "'" + values[i] + "'";
			if (i != (colNames.length - 1)) {
				sql_colNames += ",";
				sql_values += ",";
			}
		}
		sql += sql_colNames + ") values(" + sql_values + ")";
		db.execSQL(sql);
	}

	public void updateRow(String[] colNames, String[] values, String condition) throws Exception {
		if (colNames.length != values.length) {
			Exception t = new Exception("Number of values do not match with number of columns.");
			throw t;
		}
		String sql = "update " + this.tblName + " set ";
		for (int i = 0; i < colNames.length; i++) {
			sql += colNames[i];
			sql += " = '" + values[i] + "'";
			if (i != (colNames.length - 1)) {
				sql += ",";
			}
		}
		sql += " where " + condition;
		db.execSQL(sql);
	}

	public void deleteRow(String condition) {
		String sql = "delete from " + this.tblName + " where " + condition;
		this.db.execSQL(sql);
	}

	public void deleteTable() {
		this.db.execSQL("DROP TABLE IF EXISTS " + this.tblName);
	}
}
