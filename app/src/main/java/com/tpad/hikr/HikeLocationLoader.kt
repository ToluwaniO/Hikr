package com.tpad.hikr

import android.content.AsyncTaskLoader
import android.content.Context
import android.util.Log
import com.tpad.hikr.DataClasses.HikeLocationData

/**
 * Created by oguns on 7/15/2017.
 */
 class HikeLocationLoader
/**
 * Constructs a new [EarthquakeLoader].

 * @param context of the activity
 * *
 * @param url to load data from
 */
(context: Context,
 /** Query URL  */
 private val mUrl: String?) : AsyncTaskLoader<List<HikeLocationData>>(context) {

    protected override fun onStartLoading() {
        forceLoad()
    }

    /**
     * This is on a background thread.
     */
    override fun loadInBackground(): List<HikeLocationData>? {
        if (mUrl == null) {
            Log.d(LOG_TAG, "url is null")
            return null
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        val earthquakes = QueryUtils.getHikeLocations(mUrl)
        Log.d(LOG_TAG, "url is not null")

        return earthquakes
    }

    companion object {

        /** Tag for log messages  */
        private val LOG_TAG = HikeLocationLoader::class.java.name
    }
}