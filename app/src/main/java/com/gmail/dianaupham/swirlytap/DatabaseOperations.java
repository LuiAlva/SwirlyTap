package com.gmail.dianaupham.swirlytap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseOperations extends SQLiteOpenHelper {
    public static final int database_version = 1;
    public String CREATE_QUERY = "CREATE TABLE " + TableData.TableInfo.TABLE_NAME + "("+TableData.TableInfo.USER_NAME + " TEXT,"+TableData.TableInfo.USER_PASS +" TEXT );";
    private DatabaseOperations db;

    public DatabaseOperations(Context context)
    {
        super(context, TableData.TableInfo.DATABASE_NAME, null, database_version);
        Log.d("Database operations", "Database created");

    }

    @Override
    public void onCreate(SQLiteDatabase sdb)
    {
        sdb.execSQL(CREATE_QUERY);
        Log.d("Database operations", "Table created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2)
    {


    }
    public void InsertInfo(DatabaseOperations dop,String username,String password)
    {
        SQLiteDatabase SQ = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TableData.TableInfo.USER_NAME,username);
        cv.put(TableData.TableInfo.USER_PASS,password);
        SQ.insert(TableData.TableInfo.TABLE_NAME, null, cv);
        Log.d("Database operations", "Something inserted");
    }
    public Cursor getInformation(DatabaseOperations db1)
    {
        SQLiteDatabase sq = db1.getReadableDatabase();
        String[] columns = {TableData.TableInfo.USER_NAME, TableData.TableInfo.USER_PASS};
        Cursor x = sq.query(TableData.TableInfo.TABLE_NAME, columns, null, null, null, null, null);
        return x ;

    }

}

