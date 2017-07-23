package com.tpad.hikr.Database

import android.content.ContentResolver
import android.net.Uri
import android.net.Uri.withAppendedPath
import android.provider.BaseColumns



/**
 * Created by oguns on 7/23/2017.
 */
object HikeLocationContract {

    /**
     * Inner class that defines constant values for the pets database table.
     * Each entry in the table represents a single pet.
     */
    class HikeLocationEntry : BaseColumns {
        companion object {

            /** Name of database table for pets  */
            val TABLE_NAME = "HikeLocations"

            /**
             * Unique ID number for the locations (only for use in the database table).

             * Type: INTEGER
             */
            val _ID = BaseColumns._ID

            /**
             * Name of the locale.

             * Type: TEXT
             */
            val COLUMN_LOCALE_NAME = "locale"

            /**
             * name of the country.

             * Type: TEXT
             */
            val COLUMN_COUNTRY_NAME = "country"

            /**
             * json data of locations.

             * Type: TEXT
             */
            val COLUMN_LOCATION_JSON = "json"

            val CONTENT_AUTHORITY = "com.tpad.hikr"
            val BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY)
            val PATH_LOCATIONS = "HikeLocations"
            val CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_LOCATIONS)

            /**
             * The MIME type of the [.CONTENT_URI] for a list of locations.
             */
            val CONTENT_LIST_TYPE =
                    ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOCATIONS

            /**
             * The MIME type of the [.CONTENT_URI] for a single pet.
             */
            val CONTENT_ITEM_TYPE =
                    ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOCATIONS
        }

    }

}// To prevent someone from accidentally instantiating the contract class,
// give it an empty constructor.