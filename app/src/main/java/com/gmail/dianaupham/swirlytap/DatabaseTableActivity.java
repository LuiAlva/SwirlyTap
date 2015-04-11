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
    public static final String GAME_TABLE_NAME = "HIGH SCORE LEADER BOARD";
    public static final String GAME_COLUMN_ID = "ID";
    public static final String GAME_COLUMN_USERNAME = "USERNAME";
    public static final String GAME_COLUMN_PASSWORD = "PASSWORD";
    public static final String GAME_COLUMN_SCORE = "SCORE";


    public DatabaseTableActivity(Context ctx) {
        super(ctx, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create Table " +
                        "(ID integer key, Username text, Password text, Score integer value)"

        );

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP DATABASE TABLE IF EXISTS HIGH SCORE LEADER BOARD ");
        onCreate(db);

    }

    public boolean InsertInfo(String USER_NAME, String PASS_WORD,int SCORE)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues ctv = new ContentValues();

        ctv.put("USERNAME", USER_NAME);
        ctv.put("PASSWORD", PASS_WORD);
        ctv.put("SCORE", SCORE);

        db.insert("HIGH SCORE LEADER BOARD", null, ctv);
        return true;

    }
    public Cursor getData(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "select * from HIGH SCORE LEADER BOARD where ID="+id+"", null);
        return res;


    }

    public int NumRows()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db,GAME_TABLE_NAME );
        return numRows;

    }
    public boolean updateInfo( Integer id, String USER_NAME, String PASS_WORD, int SCORE)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues ctv = new ContentValues();
        ctv.put("USERNAME",USER_NAME);
        ctv.put("PASSWORD",PASS_WORD );
        ctv.put("SCORE", SCORE);
        db.update("HIGH SCORE LEADER BOARD", ctv, "id = ? ", new String[] { Integer.toString(id)});
        return true;

    }
    public Integer deleteInfo (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("HIGH SCORE LEADER BOARD",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }
    public ArrayList getAllInfo()
    {
        ArrayList array_list = new ArrayList();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from HIGH SCORE LEADER BOARD", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(GAME_COLUMN_USERNAME)));
            res.moveToNext();
        }
        return array_list;
    }


}