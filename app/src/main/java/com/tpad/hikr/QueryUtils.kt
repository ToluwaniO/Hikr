package com.tpad.hikr

import android.util.Log
import com.tpad.hikr.DataClasses.HikeLocationData
import com.tpad.hikr.Database.HikeLocationContract.HikeLocationEntry
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import com.google.android.gms.common.api.GoogleApiClient
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.location.Geocoder
import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import com.tpad.hikr.Database.HikeLocationDbHelper
import com.tpad.hikr.Database.HikeLocationProvider
import java.io.*
import java.util.*


/**
 * Created by oguns on 7/14/2017.
 */

class QueryUtils(val url: String, val dbHelper: HikeLocationDbHelper) {

    private var link: String = ""

    init {
        this.link = url
    }

    companion object {
        var mGoogleApiClient : GoogleApiClient? = null

        private val TAG = QueryUtils::class.java.simpleName

        fun setApiClient(client : GoogleApiClient) {
            mGoogleApiClient = client
        }

        fun createUrl(a : String) : URL? {
            var  url : URL? = null

            try {
                url = URL(a)
                Log.d(TAG, "url is not null")
            }
            catch (e : Exception){
                Log.d(TAG, "url is null")
                Log.d(TAG, "uel error: ${e.toString()}")
            }
            return url
        }

        fun makeHttpRequest(url : URL) : String {
            var jsonResponse = ""
            var urlConnection : HttpURLConnection? = null
            var inputStream : InputStream? = null

            Log.d(TAG, url.toString())

            try{
                urlConnection = url.openConnection() as HttpURLConnection

                urlConnection.readTimeout = 10000
                urlConnection.connectTimeout = 15000
                urlConnection.requestMethod = "GET"

                urlConnection.connect()

                if(urlConnection.responseCode == 200){
                    inputStream = urlConnection.inputStream
                    jsonResponse = readFromStream(inputStream)

                    Log.d(TAG, "Error ${urlConnection.responseCode} ${urlConnection.responseMessage}")
                }
                else{
                    Log.d(TAG, "Error ${urlConnection.responseCode} ${urlConnection.responseMessage}")
                }
            }
            catch (e : IOException){
                Log.e(TAG, "problem retrieving earthquake results", e)
            }
            finally {
                if(urlConnection != null) urlConnection.disconnect()
                if (inputStream != null) inputStream.close()
            }
            return jsonResponse
        }

        @Throws(IOException::class)
        private fun readFromStream(stream : InputStream?) : String {
            val stringBuilder : StringBuilder = StringBuilder()

            if(stream != null){
                val inputStreamReader : InputStreamReader = InputStreamReader(stream, Charset.forName("UTF-8"))
                val reader = BufferedReader(inputStreamReader)

                var line = reader.readLine()

                while(line != null){
                    Log.d("line", line)
                    stringBuilder.append(line)
                    line = reader.readLine()
                }
            }
            else {
                Log.d(TAG, "input stream is null")
            }
            Log.d(TAG, stringBuilder.toString())
            return stringBuilder.toString()
        }

        fun extractHikeLocations(json : String?, context: Context) : ArrayList<HikeLocationData> {
            val hikeLocationList = ArrayList<HikeLocationData>()

            try{
                val root : JSONObject

                if(json != null){
                    Log.d("json", json)
                    root = JSONObject(json)
                    val results : JSONArray = root.getJSONArray("results")
                    val length = results.length()
                    var i : Int = 0

                    while (i < length){
                        try{
                            Log.d(TAG, i.toString())
                            val locationItem = results.getJSONObject(i)
                            val arrayItem = HikeLocationData()
                            arrayItem.name = locationItem.getString("name")
                            Log.d(TAG, arrayItem.name)
                            arrayItem.address = locationItem.getString("formatted_address")
                            Log.d(TAG, arrayItem.address)
                            arrayItem.placeId = locationItem.getString("place_id")
                            Log.d(TAG, "place-id ${locationItem.getString("place_id")}")
                            arrayItem.rating = locationItem.getDouble("rating")
                            Log.d(TAG, "rating- ${arrayItem.rating}")
                            arrayItem.latitude = locationItem.getJSONObject("geometry").getJSONObject("location").getDouble("lat")
                            arrayItem.longitude = locationItem.getJSONObject("geometry").getJSONObject("location").getDouble("lng")
                            //arrayItem.city = )
                            val city = getCityName(LatLng(arrayItem.latitude, arrayItem.longitude), context)
                            if(city != null)arrayItem.city = city
                            else arrayItem.city = ""
                            i++
                            hikeLocationList.add(arrayItem)
                        }
                        catch(e : JSONException){
                            Log.d(TAG, e.toString())
                            i++
                        }
                    }
                    Log.d(TAG, "LENGTH - " + i)
                }
            }
            catch (e : JSONException){
                Log.d(TAG, e.toString())
            }
            catch (e : Exception){
                Log.d(TAG, e.toString())
            }
            return hikeLocationList
        }

        fun getHikeLocations(link: String, locale: String?, country: String?, context: Context): ArrayList<HikeLocationData> {
            Log.d(TAG, "get hike location called")
            var json: String? = null

            var dataLoc: Cursor? = null
            if(locale != null && country != null)
                dataLoc= getLocation(locale, country, context)

            if(dataLoc == null || dataLoc.count <= 0){
                Log.d(TAG, "location not present in db")
                val url : URL? = createUrl(link)
                Log.d(TAG, url?.toString())
                try {
                    json = url?.let { makeHttpRequest(it) }
                    json?.let { if(locale != null && country != null){
                        insertLocation(locale, country, json as String, context)
                    }
                    }
                } catch (e: IOException) {
                    Log.d(TAG, "IO", e)
                }
            }
            else{
                Log.d(TAG, "location present in db")
                Log.d(TAG, "cursor ${dataLoc.toString()} and size: ${dataLoc.count}")
                val index = dataLoc.getColumnIndex(HikeLocationEntry.COLUMN_LOCATION_JSON)
                dataLoc.moveToFirst()
                return extractHikeLocations(dataLoc.getString(index), context)
            }

            return extractHikeLocations(json, context)
        }

        private fun getCityName(latLng: LatLng, context: Context): String? {
            val geocoder = Geocoder(context, Locale.getDefault())

            try {
                val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                if (addresses.size > 0) {
                    return addresses[0].getAddressLine(1)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }

        private fun insertLocation(locale: String, country: String, json: String, context: Context){
            val values = ContentValues()
            values.put(HikeLocationEntry.COLUMN_LOCALE_NAME, locale)
            values.put(HikeLocationEntry.COLUMN_COUNTRY_NAME, country)
            values.put(HikeLocationEntry.COLUMN_LOCATION_JSON, json)

            val uri = context.contentResolver?.insert(HikeLocationEntry.CONTENT_URI, values)

            if(uri == null){
                Log.d(TAG, "Could not insert uri")
            }
            else{
                Log.d(TAG, "insert location succesfull")
            }
        }

        private fun getLocation(locale: String, country: String, context: Context): Cursor? {
            //val uri = Uri()
            val projection = arrayOf<String>(HikeLocationEntry._ID, HikeLocationEntry.COLUMN_LOCALE_NAME,
            HikeLocationEntry.COLUMN_COUNTRY_NAME, HikeLocationEntry.COLUMN_LOCATION_JSON)
            val  selection = "${HikeLocationEntry.COLUMN_LOCALE_NAME} =? AND ${HikeLocationEntry.COLUMN_COUNTRY_NAME} = ?"
            val selectionArgs = arrayOf<String>(locale, country)
            val cursor = context.contentResolver.query(HikeLocationEntry.CONTENT_URI, projection, selection, selectionArgs, null)

            return cursor
        }
    }

}