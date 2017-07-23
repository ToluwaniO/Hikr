package com.tpad.hikr.Database

import android.content.*
import android.database.Cursor
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import com.tpad.hikr.Database.HikeLocationContract.HikeLocationEntry

/**
 * Created by oguns on 7/23/2017.
 */
class HikeLocationProvider(): ContentProvider(){

    private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    private val HIKE = 100
    private val HIKE_ID = 101
    private lateinit var dbHelper: HikeLocationDbHelper

    override fun onCreate(): Boolean {
        dbHelper = HikeLocationDbHelper(context)

        sUriMatcher.addURI(HikeLocationEntry.CONTENT_AUTHORITY, HikeLocationEntry.PATH_LOCATIONS, HIKE)
        sUriMatcher.addURI(HikeLocationEntry.CONTENT_AUTHORITY, HikeLocationEntry.PATH_LOCATIONS + "/#", HIKE_ID)

        return true
    }

    override fun insert(uri: Uri?, values: ContentValues?): Uri? {
        val match = sUriMatcher.match(uri)
        when (match) {
            HIKE -> return insertHikeLocation(uri, values)
            else -> throw IllegalArgumentException("Insertion is not supported for " + uri)
        }
    }

    override fun query(uri: Uri?, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val database = dbHelper.readableDatabase
        var cursor: Cursor? = null
        val match = sUriMatcher.match(uri)

        when(match){
            HIKE -> {
                cursor = database?.query(HikeLocationEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder)
            }
            HIKE_ID -> {
                var s = HikeLocationEntry._ID + "=?"
                val sArgs = arrayOf(ContentUris.parseId(uri).toString())
                cursor = database?.query(HikeLocationEntry.TABLE_NAME, projection, s, sArgs, null, null, sortOrder);
            }
            else -> throw IllegalArgumentException("ILLEGAL ARGUMENT")
        }
        cursor?.setNotificationUri(context.contentResolver, uri)
        return cursor
    }

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        var selection = selection
        var selectionArgs = selectionArgs
        val match = sUriMatcher.match(uri)
        when (match) {
            HIKE -> return updatePet(uri, values, selection, selectionArgs)
            HIKE_ID -> {
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = HikeLocationEntry._ID + "=?"
                selectionArgs = arrayOf(ContentUris.parseId(uri).toString())
                return updatePet(uri, values, selection, selectionArgs)
            }
            else -> throw IllegalArgumentException("Update is not supported for " + uri)
        }
    }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        var selection = selection
        var selectionArgs = selectionArgs
        // Get writeable database
        val db = dbHelper?.writableDatabase
        var rowsDeleted : Int

        val match = sUriMatcher.match(uri)
        when (match) {
            HIKE ->
                // Delete all rows that match the selection and selection args
                rowsDeleted = db.delete(HikeLocationEntry.TABLE_NAME, selection, selectionArgs)
            HIKE_ID -> {
                // Delete a single row given by the ID in the URI
                selection = HikeLocationEntry._ID + "=?"
                selectionArgs = arrayOf(ContentUris.parseId(uri).toString())
                context.contentResolver.notifyChange(uri, null)
                rowsDeleted = db.delete(HikeLocationEntry.TABLE_NAME, selection, selectionArgs)
            }
            else -> throw IllegalArgumentException("Deletion is not supported for " + uri)
        }
        if(rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted
    }

    override fun getType(uri: Uri?): String {
        val match = sUriMatcher.match(uri)
        when (match) {
            HIKE -> return HikeLocationEntry.CONTENT_LIST_TYPE
            HIKE_ID -> return HikeLocationEntry.CONTENT_ITEM_TYPE
            else -> throw IllegalStateException("Unknown URI $uri with match $match")
        }
    }

    private fun insertHikeLocation(uri: Uri?, values: ContentValues?): Uri? {
        val db = dbHelper.writableDatabase
        val locale = values?.getAsString(HikeLocationEntry.COLUMN_LOCALE_NAME)
        val country = values?.getAsString(HikeLocationEntry.COLUMN_COUNTRY_NAME)
        val locationJson = values?.getAsString(HikeLocationEntry.COLUMN_LOCATION_JSON)

        if (locale == null || TextUtils.isEmpty(locale))throw IllegalArgumentException("argument is null")
        if (country == null || TextUtils.isEmpty(country))throw IllegalArgumentException("argument is null")
        if (locationJson == null || TextUtils.isEmpty(locationJson))throw IllegalArgumentException("argument is null")

        val id = db.insert(HikeLocationEntry.TABLE_NAME, null, values)

        if(id == -1L){
            Log.d(TAG, "Failed to insert row for $uri")
            return null
        }
        context.contentResolver.notifyChange(uri, null)
        return ContentUris.withAppendedId(uri, id)
    }

    private fun updatePet(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        val db = dbHelper.writableDatabase;
        val s = HikeLocationEntry._ID + "=?"
        val sArgs = arrayOf(ContentUris.parseId(uri).toString());
        val rowsUpdated = db.update(HikeLocationEntry.TABLE_NAME, values, s, sArgs)

        if (rowsUpdated != 0) {
            context.getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated
    }

    companion object {
        private val TAG = HikeLocationProvider::class.java.simpleName
    }

}