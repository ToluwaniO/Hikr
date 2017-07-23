package com.tpad.hikr.Fragments

import android.content.Context
import android.location.Geocoder
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.model.LatLng
import com.tpad.hikr.Adapters.HikeLocationAdapater
import com.tpad.hikr.DataClasses.HikeLocationData
import com.tpad.hikr.QueryUtils
import com.tpad.hikr.R
import org.afinal.simplecache.ACache
import java.io.IOException
import java.util.*
import java.util.ArrayList

/**
 * Created by oguns on 7/20/2017.
 */
class HomeFragment(latLng: LatLng, googleApiClient: GoogleApiClient?) : Fragment() {

    internal lateinit var recyclerView: RecyclerView
    internal lateinit var progressBar: ProgressBar
    internal var hikeLocationDataArrayList: ArrayList<HikeLocationData> = ArrayList( )
    internal lateinit var hikeLocationAdapater: HikeLocationAdapater
    internal var location: String? = null
    internal var latLng: LatLng
    internal var googleApiClient : GoogleApiClient?
    internal lateinit var rootView: View
    internal val LOCATION_LIST = "location_list"
    var dataSet = false

    constructor():this(LatLng(0.0,0.0), null)

    init {
        this.latLng = latLng
        this.googleApiClient = googleApiClient
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater?.inflate(R.layout.home_fragment_layout, container, false) as View
        hikeLocationDataArrayList = ArrayList<HikeLocationData>()
        progressBar = rootView.findViewById(R.id.home_progressbar) as ProgressBar
        recyclerView = rootView.findViewById(R.id.discover_recycler) as RecyclerView

        setRecyclerView(googleApiClient)

        location = getCityName(latLng) +  ", " + getCountryName(latLng)
        Log.d(TAG, "onCreateView")
        val query = getCityName(latLng) + "%20" + getCountryName(latLng) + "hiking%20area"
        Log.d(TAG, query)
        if (savedInstanceState != null){
            Log.d(TAG, "savedInstance size ${savedInstanceState.getParcelableArrayList<HikeLocationData>(LOCATION_LIST).size}")
            hikeLocationDataArrayList.copyFrom(savedInstanceState.getParcelableArrayList<HikeLocationData>(LOCATION_LIST))
            Log.d(TAG, "savedInstanced used for list")
        }
        if(hikeLocationAdapater.itemCount <= 1) {
            GetLocationData(rootView.context, googleApiClient).execute("https://us-central1-hikr-41391.cloudfunctions.net/app/locations/"+query)
            Log.d(TAG, "dataSet not available")
        }
        return rootView
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSavedINSTANCE")
        outState?.putParcelableArrayList(LOCATION_LIST, hikeLocationDataArrayList);
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        savedInstanceState?.let { hikeLocationDataArrayList = savedInstanceState.getParcelableArrayList(LOCATION_LIST)
            progressBar.visibility = View.GONE
        Log.d(TAG, "onSavedInstanceUsed")}
    }

    private fun setRecyclerView(googleApiClient: GoogleApiClient?) {
        googleApiClient?.let { hikeLocationAdapater = HikeLocationAdapater(hikeLocationDataArrayList, rootView.context, googleApiClient)
            val layoutManager : RecyclerView.LayoutManager = GridLayoutManager(rootView.context, 1)
            recyclerView.layoutManager = layoutManager
            recyclerView.itemAnimator = DefaultItemAnimator()
            recyclerView.adapter = hikeLocationAdapater
            hikeLocationDataArrayList.add(HikeLocationData(getCityName(latLng) + ", " + getCountryName(latLng)))
        }


    }

    private fun getCityName(latLng: LatLng): String? {
        val geocoder = Geocoder(rootView.context, Locale.getDefault())

        try {
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            Log.d(TAG, addresses.toString())

            if (addresses.size > 0) {
                return addresses[0].locality //+ ", " + addresses.get(0).getAddressLine(1);
            }
        } catch (e: IOException) {
            Log.d(TAG, "error: ${e.toString()}")
        }

        return null
    }

    private fun getCountryName(latLng: LatLng): String? {
        val geocoder = Geocoder(rootView.context, Locale.getDefault())

        try {
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

            if (addresses.size > 0) {
                return addresses[0].countryName
            }
        } catch (e: IOException) {
            Log.d(TAG, "error: ${e.toString()}")
        }

        return null
    }

    private inner class GetLocationData(internal var context: Context, internal val googleApiClient: GoogleApiClient?) : AsyncTask<String, Void, ArrayList<HikeLocationData>>() {

        internal var locDataList: ArrayList<HikeLocationData> = ArrayList()

        override fun doInBackground(vararg urls: String): ArrayList<HikeLocationData> {
            Log.d(TAG, "doInBackground() called")
            val url = urls[0]
            googleApiClient?.let { QueryUtils.setApiClient(googleApiClient) }
            locDataList = QueryUtils.getHikeLocations(url, getCityName(latLng), getCountryName(latLng), context)
            return locDataList
        }

        override fun onProgressUpdate(vararg values: Void) {
            super.onProgressUpdate(*values)
        }

        override fun onPostExecute(result: ArrayList<HikeLocationData>) {
            Log.d(TAG, "onProgressUpdate")
            Log.d(TAG, "size: " + result.size)
            hikeLocationDataArrayList.copyFrom(result)
            dataSet = true
            setRecyclerView(googleApiClient)
            progressBar.visibility = View.GONE
        }
    }
    fun <T> ArrayList<T>.copyFrom(fromList: ArrayList<T>){
        for (i in fromList){
            this.add(i)
        }
    }

    companion object {
        private val TAG = HomeFragment::class.java.simpleName
    }
}
