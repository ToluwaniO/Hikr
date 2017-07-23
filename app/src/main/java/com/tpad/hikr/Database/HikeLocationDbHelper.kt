package com.tpad.hikr.Database

/**
 * Created by oguns on 7/23/2017.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.tpad.hikr.DataClasses.HikeLocationData
import com.tpad.hikr.Database.HikeLocationContract.HikeLocationEntry


class HikeLocationDbHelper(val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    override fun onCreate(db: SQLiteDatabase?) {
        val SQL_CREATE_HIKE_LOCATION_TABLE = "CREATE TABLE ${HikeLocationEntry.TABLE_NAME} (${HikeLocationEntry._ID}" +
                " INTEGER PRIMARY KEY AUTOINCREMENT, ${HikeLocationEntry.COLUMN_LOCALE_NAME} TEXT NOT NULL,"+
                " ${HikeLocationEntry.COLUMN_COUNTRY_NAME} TEXT NOT NULL, ${HikeLocationEntry.COLUMN_LOCATION_JSON} TEXT NOT NULL);"
        db?.execSQL(SQL_CREATE_HIKE_LOCATION_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        private val TAG = HikeLocationData::class.java.simpleName
        private val DATABASE_NAME = "Hikr.db"
        private val DATABASE_VERSION = 1
    }

}