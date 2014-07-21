package com.ijoomer.caching;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedInputStream;
import java.io.IOException;

/**
 *  This Class Contains Method IjoomerAdvance Database OpenHelper.
 * @author tasol
 *
 */
public class IjoomerOpenHelper extends SQLiteOpenHelper {

    private Context context;
    private SQLiteDatabase myDataBase;
    private String DB_SQL;


    /**
     * Constructor
     * @param context represented {@link Context}}
     * @param dbname represented database name
     * @param dbversion represented database version
     * @param dbSqlName represented database sql name
     * @throws IOException {@link Exception}
     */
    IjoomerOpenHelper(Context context, String dbname, int dbversion, String dbSqlName) throws IOException {
        super(context, dbname, null, dbversion);
        this.context = context;
        this.DB_SQL = dbSqlName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            BufferedInputStream inStream = new BufferedInputStream(context.getAssets().open(DB_SQL));
            String sql = "";
            int character = -2;
            do {
                character = inStream.read();
                if ((character != -1) && (character != -2))
                    sql += (char) character;
                else
                    break;
            } while (true);
            System.out.println("onCreate DB SQL = " + sql.split("\n"));
            String[] arrSQL = sql.split("\n");

            int size = arrSQL.length;
            for (int i = 0; i < size; i++) {
                db.execSQL(arrSQL[i]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try {
            BufferedInputStream inStream = new BufferedInputStream(context.getAssets().open(DB_SQL));
            String sql = "";
            int character = -2;
            do {
                character = inStream.read();
                if ((character != -1) && (character != -2))
                    sql += (char) character;
                else
                    break;
            } while (true);

            System.out.println("onUpgrade DB SQL = " + sql.split("\n"));
            String[] arrSQL = sql.split("\n");
            int size = arrSQL.length;
            for (int i = 0; i < size; i++) {
                db.execSQL(arrSQL[i]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String Query = "select 'drop table  ' || name || ';' from sqlite_master where type = 'table'";
            Cursor c = db.rawQuery(Query, null);
            c.moveToFirst();
            while (c.getPosition() < c.getCount()) {
                if (!c.getString(0).equals("android_metadata")) {
                    db.execSQL(c.getString(0));
                }
                c.moveToNext();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * This method used to get database,
     * @return {@link SQLiteDatabase}
     */
    public SQLiteDatabase getOpenDatabase() {
        return myDataBase;
    }

    /**
     * This method used to close database.
     */
    public synchronized void close() {
        if (myDataBase != null) {
            myDataBase.close();
        }
        super.close();
    }
}
