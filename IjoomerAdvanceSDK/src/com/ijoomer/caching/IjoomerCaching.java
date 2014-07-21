package com.ijoomer.caching;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ijoomer.common.classes.CoreCachingConstants;

/**
 * This Class Contains Method IjoomerCaching.
 * 
 * @author tasol
 * 
 */
public final class IjoomerCaching extends CoreCachingConstants {

	private Context mContext;

	public ArrayList<HashMap<String, String>> rows;
	private HashMap<String, String> finalRow;

	private static String databaseName;
	private static int databaseVersion;
	private static String databaseSql;

	private String reqObject;

	private ArrayList<String> extraColumns = new ArrayList<String>();
	private ArrayList<String> extraValues = new ArrayList<String>();

	private IjoomerCachingInsertListener cachingInsertListener;

	/**
	 * This method used to get caching insert listener.
	 * @return {@link IjoomerCachingInsertListener}
	 */
	public IjoomerCachingInsertListener getCachingInsertListener() {
		return cachingInsertListener;
	}

	/**
	 * This method used to set caching insert listener.
	 * @param cachingInsertListener represented caching insert listener
	 */
	public void setCachingInsertListener(IjoomerCachingInsertListener cachingInsertListener) {
		this.cachingInsertListener = cachingInsertListener;
	}

	/**
	 * This method used to add extra column in database table data insert.
	 * @param columnName represented column name
	 * @param value represented column value
	 */
	public void addExtraColumn(String columnName, String value) {
		extraColumns.add(columnName);
		extraValues.add(value);
	}

	/**
	 * This method used to get req object.
	 * @return represented {@link String}
	 */
	private String getReqObject() {
		return reqObject;
	}

	/**
	 * This method used to set req object.
	 * @param reqObject represented req object.
	 */
	public void setReqObject(String reqObject) {
		this.reqObject = reqObject;
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 *            {@link Context}
	 */
	public IjoomerCaching(Context context) {
		mContext = context;
	}

	/**
	 * This method used to get database name.
	 * 
	 * @return {@link String}
	 */
	public static String getDatabaseName() {
		return databaseName;
	}

	/**
	 * This method used to set database name.
	 * 
	 * @param databaseName
	 *            represented database name
	 */
	public static void setDatabaseName(String databaseName) {
		IjoomerCaching.databaseName = databaseName;
	}

	/**
	 * This method used to get database version.
	 * 
	 * @return {@link Integer}
	 */
	public static int getDatabaseVersion() {
		return databaseVersion;
	}

	/**
	 * This method used to set database version.
	 * 
	 * @param databaseVersion
	 *            represented database version
	 */
	public static void setDatabaseVersion(int databaseVersion) {
		IjoomerCaching.databaseVersion = databaseVersion;
	}

	/**
	 * This method used to get database Sql.
	 * 
	 * @return {@link String}
	 */
	public static String getDatabaseSql() {
		return databaseSql;
	}

	/**
	 * This method used to set database Sql.
	 * 
	 * @param databaseSql
	 *            represented database Sql name
	 */
	public static void setDatabaseSql(String databaseSql) {
		IjoomerCaching.databaseSql = databaseSql;
	}

	public void cacheRowData(String responseObject) {

		try {
			SQLiteDatabase sd = IjoomerDataHelper.getInstance(mContext).getDB();
			String query = "CREATE TABLE IF NOT EXISTS ROWCACHE (reqObject TEXT PRIMARY KEY  NOT NULL , responseObject TEXT)";
			sd.execSQL(query);

			IjoomerDataHelper.getInstance(mContext).addTable("ROWCACHE");
			ContentValues con = new ContentValues();
			con.put("reqObject", getReqObject());
			con.put("responseObject", responseObject);
			sd.beginTransaction();
			sd.insertWithOnConflict("ROWCACHE", null, con, SQLiteDatabase.CONFLICT_REPLACE);
			sd.setTransactionSuccessful();
			sd.endTransaction();

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public String getCachedRowData() {
		try {
			ArrayList<HashMap<String, String>> arrayList = IjoomerDataHelper.getInstance(mContext).getTableList().get("ROWCACHE")
					.readRowSQL("SELECT * FROM ROWCACHE where reqObject='" + getReqObject() + "'", null);
			return arrayList.get(0).get("responseObject");
		} catch (Throwable e) {
		}
		return null;
	}

	/**
	 * This method used to cache data.
	 * 
	 * @param data
	 *            represented {@link JSONArray} data
	 * @param deleteOldRecords
	 *            represented (true - for remove old data,false- for not remove
	 *            old data)
	 * @param tableName
	 *            represented database table name
	 * @return {@link ArrayList<HashMap<String, String>>}
	 */
	public ArrayList<HashMap<String, String>> cacheData(JSONArray data, boolean deleteOldRecords, String tableName) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("data", data);

			return cacheData(obj, deleteOldRecords, tableName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method used to is table exists or not.
	 * 
	 * @param tableName
	 *            represented database name.
	 * @return {@link boolean}
	 */
	public boolean isTableExists(String tableName) {
		try {
			if (IjoomerDataHelper.getInstance(mContext).getTableList().get(tableName) != null) {
				return true;
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * This method used to cache data.
	 * 
	 * @param data
	 *            represented {@link JSONObject} data
	 * @param deleteOldRecords
	 *            represented (true - for remove old data,false- for not remove
	 *            old data)
	 * @param tableName
	 *            represented database table name
	 * @return {@link ArrayList<HashMap<String, String>>}
	 */
	@SuppressWarnings("unused")
	public ArrayList<HashMap<String, String>> cacheData(JSONObject data, boolean deleteOldRecords, String tableName) {
		long startTime;
		long endTime;
		startTime = Calendar.getInstance().getTimeInMillis();
		rows = new ArrayList<HashMap<String, String>>();
		finalRow = new LinkedHashMap<String, String>();

		generateSchema(data);
		rows.add(finalRow);
		int size = rows.size();
		for (int i = size - 2; i >= 0; i--) {

			if (rows.get(i).size() != finalRow.size() || rows.get(i).keySet().hashCode() != finalRow.keySet().hashCode()) {

				Iterator<String> itr = finalRow.keySet().iterator();
				while (itr.hasNext()) {
					String rowKey = itr.next();
					if (!rows.get(i).containsKey(rowKey)) {
						rows.get(i).put(rowKey, finalRow.get(rowKey));
					}
				}

			} else {
				break;
			}
		}
		createTable(rows, deleteOldRecords, tableName);
		return rows;
	}

	/**
	 * This method used to parse data.
	 * 
	 * @param data
	 *            represented {@link JSONArray} data
	 * @return {@link ArrayList<HashMap<String, String>>}
	 */
	public ArrayList<HashMap<String, String>> parseData(JSONArray data) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("", data);

			return parseData(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method used to parse data.
	 * 
	 * @param data
	 *            represented {@link JSONObject} data
	 * @return {@link ArrayList<HashMap<String, String>>}
	 */
	public ArrayList<HashMap<String, String>> parseData(JSONObject data) {

		long startTime;
		long endTime;
		startTime = Calendar.getInstance().getTimeInMillis();
		rows = new ArrayList<HashMap<String, String>>();
		finalRow = new HashMap<String, String>();

		generateSchema(data);
		rows.add(finalRow);
		int size = rows.size();
		for (int i = size - 2; i >= 0; i--) {

			if (rows.get(i).size() != finalRow.size() || rows.get(i).keySet().hashCode() != finalRow.keySet().hashCode()) {

				if (rows.get(i).size() < finalRow.size()) {
					Iterator<String> itr = finalRow.keySet().iterator();
					while (itr.hasNext()) {
						String rowKey = itr.next();
						if (!rows.get(i).containsKey(rowKey)) {
							if (IjoomerCachingConstants.unRepetedFields
									.containsKey(rowKey)) {
								rows.get(i).put(rowKey, "");
							} else {
								rows.get(i).put(rowKey, finalRow.get(rowKey));
							}
						}
					}
				} else {
					Iterator<String> itr = rows.get(i).keySet().iterator();
					while (itr.hasNext()) {
						String rowKey = itr.next();
						if (!finalRow.containsKey(rowKey)) {
							if (IjoomerCachingConstants.unRepetedFields
									.containsKey(rowKey)) {
								finalRow.put(rowKey, "");
							} else {
								finalRow.put(rowKey, rows.get(i).get(rowKey));
							}
						}
					}
				}

			} else {
				break;
			}
		}
		endTime = Calendar.getInstance().getTimeInMillis();
		System.out.println("Parsing Complete in: " + (endTime - startTime));
		return rows;
	}

	/**
	 * This method used to get data from table.
	 * 
	 * @param tableName
	 *            represented database table name
	 * @return {@link ArrayList<HashMap<String, String>>}
	 */
	public ArrayList<HashMap<String, String>> getDataFromCache(String tableName) {
		try {
			return IjoomerDataHelper.getInstance(mContext).getTableList().get(tableName).readRowSQL("SELECT * FROM " + tableName + "", null);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method used to get data from table
	 * 
	 * @param tableName
	 *            represented database table name
	 * @param query
	 *            represented database query
	 * @return {@link ArrayList<HashMap<String, String>>}
	 */
	public ArrayList<HashMap<String, String>> getDataFromCache(String tableName, String query) {

		try {
			return IjoomerDataHelper.getInstance(mContext).getTableList().get(tableName).readRowSQL(query, null);
		} catch (Throwable e) {
		}
		return null;
	}

	/**
	 * This method used to drop table in database.
	 * 
	 * @param tableName
	 *            represented database table name
	 */
	public void droapTable(String tableName) {
		SQLiteDatabase sd = IjoomerDataHelper.getInstance(mContext).getDB();
		sd.execSQL("DROP TABLE IF EXISTS " + tableName + "");
	}

	/**
	 * This method used to update table.
	 * 
	 * @param query
	 *            represented update query
	 */
	public void updateTable(String query) {
		SQLiteDatabase sd = IjoomerDataHelper.getInstance(mContext).getDB();
		sd.execSQL(query);
	}

	/**
	 * This method used to create table in database
	 * 
	 * @param data
	 *            represented {@link ArrayList<HashMap<String, String>>} data
	 * @param deleteOld
	 *            represented (true - for remove old data,false- for not remove
	 *            old data)
	 * @param tableName
	 *            represented database table name
	 */
	private void createTable(ArrayList<HashMap<String, String>> data, boolean deleteOld, String tableName) {
		if (data != null && data.size() > 0) {

			SQLiteDatabase sd = IjoomerDataHelper.getInstance(mContext).getDB();

			if (deleteOld) {
				sd.execSQL("DROP TABLE IF EXISTS " + tableName + "");
			}
			String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (";
			Iterator<String> itr = data.get(0).keySet().iterator();
			ArrayList<String> pks = new ArrayList<String>();
			while (itr.hasNext()) {

				String columnName = itr.next();
				query = query + columnName + " TEXT,";

				if (columnName.toLowerCase().endsWith("id")) {
					pks.add(columnName);

				}
			}
			if (getReqObject() != null) {
				query = query + "reqObject" + " TEXT,";
			}

			for (String column : extraColumns) {

				if (column.toLowerCase().endsWith("id")) {
					pks.add(column);
				}
				query = query + " " + column + " TEXT,";
			}

			if (pks.size() > 0) {
				if (getReqObject() != null) {
					query += "PRIMARY KEY(reqObject,";
				} else {
					query += "PRIMARY KEY(";
				}
				int size = pks.size();
				for (int i = 0; i < size - 1; i++) {
					query += pks.get(i) + ",";
				}
				query += pks.get(pks.size() - 1) + "));";
			} else {
				query = query.substring(0, query.length() - 1) + ");";
			}

			System.out.println("query : " + query);
			sd.execSQL(query);

			if (getReqObject() != null) {
				try {
					query = "DELETE  FROM " + tableName + " where reqObject='" + getReqObject() + "'";
					sd.execSQL(query);
				} catch (Throwable e) {
				}
			}

			IjoomerDataHelper.getInstance(mContext).addTable(tableName);
			ContentValues con = new ContentValues();
			sd.beginTransaction();

			if (getCachingInsertListener() == null) {
				for (int i = 0; i < data.size(); i++) {
					con.clear();
					HashMap<String, String> row = data.get(i);
					Iterator<String> it = row.keySet().iterator();

					while (it.hasNext()) {

						String columnName = it.next();
						con.put(columnName, row.get(columnName));
					}
					if (getReqObject() != null) {
						con.put("reqObject", getReqObject());
					}
					for (int j = 0; j < extraColumns.size(); j++) {
						con.put(extraColumns.get(j), extraValues.get(j));
					}

					sd.insertWithOnConflict(tableName, null, con, SQLiteDatabase.CONFLICT_REPLACE);

				}
			} else {
				for (int i = 0; i < data.size(); i++) {
					con.clear();
					HashMap<String, String> row = data.get(i);
					Iterator<String> it = row.keySet().iterator();

					while (it.hasNext()) {

						String columnName = it.next();
						con.put(columnName, row.get(columnName));
					}
					if (getReqObject() != null) {
						con.put("reqObject", getReqObject());
					}
					for (int j = 0; j < extraColumns.size(); j++) {
						con.put(extraColumns.get(j), extraValues.get(j));
					}
					try {
						getCachingInsertListener().onBeforeInsert(con);
					} catch (Exception e) {
					}
					sd.insertWithOnConflict(tableName, null, con, SQLiteDatabase.CONFLICT_REPLACE);

				}
			}
			sd.setTransactionSuccessful();
			sd.endTransaction();
		}
	}

	/**
	 * This method used to delete from table or not.
	 * 
	 * @param query
	 *            represented database query
	 * @return {@link boolean}
	 */
	public boolean deleteDataFromCache(String query) {

		try {
			SQLiteDatabase sd = IjoomerDataHelper.getInstance(mContext).getDB();
			sd.execSQL(query);
			return true;
		} catch (Throwable e) {
			return false;
		}

	}

	/**
	 * This method used to create table or insert data in table if table not
	 * exists then create table otherwise insert data in table
	 * 
	 * @param data
	 *            represented {@link ArrayList<HashMap<String, String>>} data
	 * @param tableName
	 *            represented database table name
	 */
	public void createTable(ArrayList<HashMap<String, String>> data, String tableName) {

		SQLiteDatabase sd = IjoomerDataHelper.getInstance(mContext).getDB();

		String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (";
		Iterator<String> itr = data.get(0).keySet().iterator();

		ArrayList<String> pks = new ArrayList<String>();
		while (itr.hasNext()) {

			String columnName = itr.next();
			query = query + columnName + " TEXT,";

			if (columnName.toLowerCase().endsWith("id")) {
				pks.add(columnName);
			}
		}

		if (pks.size() > 0) {

			query += "PRIMARY KEY(";
			int size = pks.size();
			for (int i = 0; i < size - 1; i++) {
				query += pks.get(i) + ",";
			}
			query += pks.get(pks.size() - 1) + "));";
		} else {
			query = query.substring(0, query.length() - 1) + ");";
		}

		System.out.println("query : " + query);
		sd.execSQL(query);

		IjoomerDataHelper.getInstance(mContext).addTable(tableName);
		ContentValues con = new ContentValues();
		sd.beginTransaction();
		int size = data.size();
		for (int i = 0; i < size; i++) {
			con.clear();
			HashMap<String, String> row = data.get(i);
			Iterator<String> it = row.keySet().iterator();

			while (it.hasNext()) {
				String columnName = it.next();
				con.put(columnName, row.get(columnName));
			}
			sd.insertWithOnConflict(tableName, null, con, SQLiteDatabase.CONFLICT_REPLACE);
		}
		sd.setTransactionSuccessful();
		sd.endTransaction();
	}

	/**
	 * This method used table schema.
	 * 
	 * @param data
	 *            represented {@link JSONObject} data
	 */
	@SuppressWarnings("unchecked")
	private void generateSchema(JSONObject data) {

		if (data != null) {
			Iterator<String> it = data.keys();
			while (it.hasNext()) {
				String key = it.next();
				if (finalRow.containsKey(key)) {
					rows.add(finalRow);
					int size = rows.size();
					for (int i = size - 2; i >= 0; i--) {

						if (rows.get(i).size() != finalRow.size() || rows.get(i).keySet().hashCode() != finalRow.keySet().hashCode()) {

							Iterator<String> itr = finalRow.keySet().iterator();
							while (itr.hasNext()) {
								String rowKey = itr.next();
								if (!rows.get(i).containsKey(rowKey)) {
									if (IjoomerCachingConstants.unRepetedFields
											.containsKey(rowKey)) {
										rows.get(i).put(rowKey, "");
									} else {
										rows.get(i).put(rowKey,
												finalRow.get(rowKey));
									}
								}
							}

						} else {
							break;
						}
					}
					finalRow = new HashMap<String, String>();

				}
				try {
					if (IjoomerCachingConstants.unNormalizeFields.containsKey(key)) {
						finalRow.put(key, data.getString(key));
					} else if (data.get(key) instanceof JSONArray) {
						JSONArray arry = data.getJSONArray(key);
						int size = arry.length();
						for (int i = 0; i < size; i++) {
							generateSchema(arry.getJSONObject(i));
						}
					} else if (data.get(key) instanceof JSONObject) {
						generateSchema(data.getJSONObject(key));
					} else {
						finalRow.put(key, data.getString(key));
					}
				} catch (Throwable e) {
					try {
						finalRow.put(key, data.getString(key));
					} catch (Exception ee) {
					}
					e.printStackTrace();

				}
			}
		}
	}

	/**
	 * This method used to update table data in database.
	 * 
	 * @param data
	 *            represented {@link ArrayList<HashMap<String, String>>}
	 * @param tableName
	 *            represented database table name
	 */
	public void updateTable(ArrayList<HashMap<String, String>> data, String tableName) {

		if (data.get(0).containsKey("id")) {
			try {
				SQLiteDatabase sd = IjoomerDataHelper.getInstance(mContext).getDB();
				ContentValues dataToUpdate = new ContentValues();
				for (HashMap<String, String> row : data) {
					dataToUpdate.put("value", row.get("value"));
					sd.update(tableName, dataToUpdate, "id='" + row.get("id") + "'", null);
				}

			} catch (Throwable e) {
				e.printStackTrace();
			}
		} else {
			try {
				SQLiteDatabase sd = IjoomerDataHelper.getInstance(mContext).getDB();
				ContentValues dataToUpdate = new ContentValues();
				for (HashMap<String, String> row : data) {
					dataToUpdate.put("value", row.get("value"));
					sd.update(tableName, dataToUpdate, "name='" + row.get("name") + "'", null);
				}

			} catch (Throwable e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * This method used to reset database
	 */
	public void resetDataBase() {
		SQLiteDatabase db = IjoomerDataHelper.getInstance(mContext).getDB();
		String Query = "select 'drop table  ' || name || ';' from sqlite_master where type = 'table'";
		Cursor c = db.rawQuery(Query, null);
		try {
			c.moveToFirst();
			while (c.getPosition() < c.getCount()) {
				if ((!c.getString(0).contains("android_metadata")) && (!c.getString(0).contains("applicationConfig")) && (!c.getString(0).contains("menus"))) {
					db.execSQL(c.getString(0));
				}
				c.moveToNext();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		c.close();
	}
}
