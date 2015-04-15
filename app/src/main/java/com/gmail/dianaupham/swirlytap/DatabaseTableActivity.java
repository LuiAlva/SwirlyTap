package com.gmail.dianaupham.swirlytap;
import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseTableActivity extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "SwirlyTapDatabase.db";
    public static final String GAME_TABLE_NAME = "HIGHSCORELEADERBOARD";
    public static final String GAME_COLUMN_ID = "ID";
    public static final String GAME_COLUMN_USERNAME = "USERNAME";
    public static final String GAME_COLUMN_PASSWORD = "PASSWORD";
    public static final String GAME_COLUMN_SCORE = "SCORE";
    public static final String GAME_COLUMN_LOGIN = "LOGGEDIN";


    public DatabaseTableActivity(Context ctx) {
        super(ctx, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create Table HIGHSCORELEADERBOARD " +
                        "(ID integer primary key, Username text, Password text, Score integer,LOGGEDIN integer)"

        );

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if(newVersion > oldVersion)
        {
            db.execSQL("DROP DATABASE TABLE IF EXISTS HIGHSCORELEADERBOARD ");
            onCreate(db);
        }
    }

    public void setlogin(String USER_NAME, String PASS_WORD, int FLAG)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues ctv = new ContentValues();
        if(FLAG == 1)
        {
            ctv.put(GAME_COLUMN_LOGIN,1);
            String[] args = new String[]{USER_NAME,PASS_WORD};
            db.update(GAME_TABLE_NAME,ctv, "USERNAME=? AND PASSWORD=?", args);

        }
        else
        {
            ctv.put(GAME_COLUMN_LOGIN, 0);
            String[] args = new String []{USER_NAME, PASS_WORD};
            db.update(GAME_TABLE_NAME,ctv,"USERNAME=? AND PASSWORD=?", args);
        }
    }

    public boolean Login(String USER_NAME, String PASS_WORD)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from HIGHSCORELEADERBOARD where USERNAME='" + USER_NAME + "'" +
                                    " AND PASSWORD='" + PASS_WORD + "'", null);
        if(res.getCount()< 1 || res.getCount() > 1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean InsertInfo(String USER_NAME, String PASS_WORD)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues ctv = new ContentValues();

        ctv.put("USERNAME", USER_NAME);
        ctv.put("PASSWORD", PASS_WORD);
       // ctv.put("SCORE", SCORE);

        db.insert("HIGHSCORELEADERBOARD", null, ctv);
        return true;

    }
    public Cursor getData(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "select * from HIGHSCORELEADERBOARD where ID="+id+"", null);
        return res;


    }

/*    public int NumRows()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db,GAME_TABLE_NAME );
        return numRows;

    }
   */
    public boolean updateInfo( Integer id, String USER_NAME, String PASS_WORD, int SCORE)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues ctv = new ContentValues();

        ctv.put("SCORE", SCORE);
        String[] args = new String[]{USER_NAME,PASS_WORD};
        db.update("HIGHSCORELEADERBOARD", ctv, "USERNAME=? AND PASSWORD=?",args); //new String[] { Integer.toString(id)});
        return true;

    }
    public Integer deleteInfo (String USER_NAME, String PASS_WORD)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = new String[]{USER_NAME,PASS_WORD};
        return db.delete("HIGHSCORELEADERBOARD",
                "USERNAME=? AND PASSWORD=?",args);
                //new String[] { Integer.toString(id) });
    }
    public ArrayList getAllInfo()
    {
        ArrayList array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from HIGHSCORELEADERBOARD", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(GAME_COLUMN_USERNAME)));
            res.moveToNext();
        }
        return array_list;
    }


}