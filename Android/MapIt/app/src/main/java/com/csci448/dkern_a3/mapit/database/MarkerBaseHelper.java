package com.csci448.dkern_a3.mapit.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.csci448.dkern_a3.mapit.database.MarkerDbSchema.MarkerTable;

/**
 * Created by dkern on 3/20/17.
 */

public class MarkerBaseHelper extends SQLiteOpenHelper
{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "MarkerBase.db";

    /**
     * constructor
     * @param context
     */
    public MarkerBaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * Creates Marker table
     * @param db database to add table to
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + MarkerTable.NAME + "(" +
            "_id integer primary key autoincrement, " +
            MarkerTable.Cols.LAT + ", " +
            MarkerTable.Cols.LONG + ", " +
            MarkerTable.Cols.DATE + ", " +
            MarkerTable.Cols.TEMPERATURE + ", " +
            MarkerTable.Cols.WEATHER +
            ")");
    }

    /**
     * Probably for new versions of database to refresh it with new columns and whatnot
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }
}
