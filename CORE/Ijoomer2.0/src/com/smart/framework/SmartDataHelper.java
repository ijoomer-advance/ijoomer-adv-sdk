package com.smart.framework;

import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SmartDataHelper {

	private static String DATABASE_NAME;
	private static int DATABASE_VERSION;
	private static String DATABASE_SQL;

	private SmartArrayList tables = new SmartArrayList();

	private Context context;
	private SQLiteDatabase db;

	public SmartDataHelper(Context context, String dbName, int dbVersion, String dbSQL, SmartVersionHandler smartVersionHandler) throws IOException {
		DATABASE_NAME = dbName;
		DATABASE_VERSION = dbVersion;
		DATABASE_SQL = dbSQL;
		this.context = context;
		SmartOpenHelper openHelper = new SmartOpenHelper(this.context, DATABASE_NAME, DATABASE_VERSION, DATABASE_SQL, smartVersionHandler);
		this.db = openHelper.getWritableDatabase();
		grabTables();
	}

	@SuppressWarnings("unchecked")
	public void grabTables() {
		Cursor cur = this.db.rawQuery("SELECT * FROM sqlite_master", new String[0]);
		cur.moveToFirst();
		String tableName;

		while (cur.getPosition() < cur.getCount()) {
			tableName = cur.getString(cur.getColumnIndex("name"));
			System.out.println("Table Name = " + tableName);
			if (!tableName.equals("android_metadata") && !tableName.equals("sqlite_sequence") && (!cur.getString(cur.getColumnIndex("type")).equalsIgnoreCase("index"))) {
				this.tables.add(new SmartTable(this.db, tableName));
			}
			cur.moveToNext();
		}
		cur.close();
	}

	public SmartArrayList getTableList() throws Exception {
		if (this.tables.size() == 0) {
			Exception t = new Exception("There are no tables to show.");
			throw t;
		}
		return this.tables;
	}

	public SQLiteDatabase getDB() {
		return db;
	}

	@SuppressWarnings("unchecked")
	public void addTable(String tableName) {
		this.tables.add(new SmartTable(this.db, tableName));
	}
}
