package com.example.spencer.swirlytap;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ebe550 on 3/10/2015.
 */
public class DatabaseOperations extends SQLiteOpenHelper {
    public static final int database_version = 1;
    public String CREATE_QUERY = "CREATE TABLE" + TableData.TableInfo.TABLE_NAME+"("+TableData.TableInfo.USER_NAME+" TEXT,"+TableData.TableInfo.USER_PASS+"TEXT );";
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
        long v = SQ.insert(TableData.TableInfo.TABLE_NAME, null,cv);
        Log.d("Database operations", "Something inserted");
    }
}

