package com.ijoomer.caching;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This Class Contains Method IjoomerAdvance Database Table.
 * @author tasol
 *
 */
public class IjoomerTable {

    private SQLiteDatabase db;

    private String tblName;
    private int colCount;
    private int rowCount;
    private String[] colNames;

    /**
     * Constructor
     * @param db {@link SQLiteDatabase}
     * @param tblName represented table name
     */
    public IjoomerTable(SQLiteDatabase db, String tblName) {
        this.db = db;
        this.tblName = tblName;

        Cursor cur = this.db.rawQuery("select * from " + tblName, new String[0]);
        this.colCount = cur.getColumnCount();
        this.rowCount = cur.getCount();
        this.colNames = new String[cur.getColumnCount()];
        cur.moveToFirst();

        int size = this.colCount;
        for (int idx = 0; idx < size; idx++) {
            colNames[idx] = cur.getColumnName(idx);
        }

        cur.close();
    }

    /**
     * This method used to get table data count.
     * @return {@link Integer}
     */
    public int getColCount() {
        Cursor cur = this.db.rawQuery("select * from " + tblName, new String[0]);
        this.colCount = cur.getColumnCount();
        cur.close();

        return this.colCount;
    }

    /**
     * This method used to get table row count.
     * @return {@link Integer}
     */
    public int getRowCount() {
        Cursor cur = this.db.rawQuery("select * from " + tblName, new String[0]);
        this.rowCount = cur.getCount();
        cur.close();
        return this.rowCount;
    }


    /**
     * This method used to get table name
     * @return {@link String}
     */
    public String getTableName() {
        return this.tblName;
    }

    /**
     * This method used to get table column name array.
     * @return {@link String[]}
     */
    public String[] getColNames() {
        Cursor cur = this.db.rawQuery("select * from " + tblName, new String[0]);
        this.colNames = new String[cur.getColumnCount()];
        cur.close();
        return colNames;
    }


    /**
     * This method used to read table row.
     * @param colNames represented table column name
     * @return {@link List<List<String>>}
     */
    public List<List<String>> readRows(String[] colNames) {
        List<List<String>> list = new ArrayList<List<String>>();
        Cursor cursor = this.db.query(this.tblName, colNames, null, null, null, null, null);
        this.colCount = cursor.getColumnCount();
        this.rowCount = cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                List<String> col = new ArrayList<String>();
                int size = colNames.length;
                for (int i = 0; i < size; i++) {
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


    /**
     * This method used to read table row give argument
     * @param rawSQL represented sql query
     * @param rawSQLSelectionArguments represented String[] argument
     * @return {@link ArrayList<HashMap<String, String>>}
     */
    public ArrayList<HashMap<String, String>> readRowSQL(String rawSQL, String[] rawSQLSelectionArguments) {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        Cursor cursor = this.db.rawQuery(rawSQL, rawSQLSelectionArguments);
        this.colCount = cursor.getColumnCount();
        this.rowCount = cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> col = new HashMap<String, String>();
                int size =cursor.getColumnCount();
                for (int i = 0; i < size; i++) {
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

    /**
     * This method used to read table row specific.
     * @param colNames represented table column name
     * @param sel represented table column select
     * @param selArgs represented table column select argument
     * @param groupBy represented table column groupBy
     * @param having represented table column having
     * @param orderBy represented table column orderBy
     * @return
     */
    public List<List<String>> readSpecificRows(String[] colNames, String sel, String[] selArgs, String groupBy, String having, String orderBy) {
        List<List<String>> list = new ArrayList<List<String>>();
        Cursor cursor = this.db.query(this.tblName, colNames, sel, selArgs, groupBy, having, orderBy);
        this.colCount = cursor.getColumnCount();
        this.rowCount = cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                List<String> col = new ArrayList<String>();
                int size = colNames.length;
                for (int i = 0; i < size; i++) {
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


    /**
     * This method used to insert row in table.
     * @param colNames represented column name
     * @param values represented column value
     * @throws Exception {@link Exception}
     */
    public void insertRow(String[] colNames, String[] values) throws Exception {
        if (colNames.length != values.length) {
            Exception t = new Exception("Number of values do not match with number of columns.");
            throw t;
        }
        String sql = "insert into " + this.tblName + "(";
        String sql_colNames = "";
        String sql_values = "";
        int size = colNames.length;
        for (int i = 0; i < size; i++) {
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


    /**
     * This method used to update row in table.
     * @param colNames represented cloumn name string[]
     * @param values represented column value
     * @param condition represented condition
     * @throws Exception {@link Exception}
     */
    public void updateRow(String[] colNames, String[] values, String condition) throws Exception {
        if (colNames.length != values.length) {
            Exception t = new Exception("Number of values do not match with number of columns.");
            throw t;
        }
        String sql = "update " + this.tblName + " set ";
        int size = colNames.length;
        for (int i = 0; i < size; i++) {
            sql += colNames[i];
            sql += " = '" + values[i] + "'";
            if (i != (colNames.length - 1)) {
                sql += ",";
            }
        }
        sql += " where " + condition;
        db.execSQL(sql);
    }


    /**
     * This method used to delete row in table.
     * @param condition represented condition
     */
    public void deleteRow(String condition) {
        String sql = "delete from " + this.tblName + " where " + condition;
        this.db.execSQL(sql);
    }


    /**
     * This method used to delete table.
     */
    public void deleteTable() {
        this.db.execSQL("DROP TABLE IF EXISTS " + this.tblName);
    }
}
