package com.tpad.hikr

import android.text.TextUtils
import android.util.Log
import com.tpad.hikr.DataClasses.HikeLocationData
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset


/**
 * Created by oguns on 7/14/2017.
 */

class QueryUtils(url: String) {

    private var link: String = ""
    private val TAG = scratch::class.java.simpleName

    init {
        this.link = url
    }

    fun createUrl(a : String) : URL? {
        var url : URL? = null

        try {
            url = URL(a)
        }
        catch (e : Exception){
            e.printStackTrace()
        }

        return url
    }

    fun makeHttpRequest(url : URL) : String? {
        var jsonResponse = ""

        if (url.toString() == ""){
            Log.d("URL", "null")
            return null
        }

        var urlConnection : HttpURLConnection? = null
        var inputStream : InputStream? = null

        try{
            urlConnection = url.openConnection() as HttpURLConnection

            urlConnection.readTimeout = 10000
            urlConnection.connectTimeout = 15000
            urlConnection.requestMethod = "GET"

            urlConnection.connect()

            if(urlConnection.responseCode == 200){
                inputStream = urlConnection.inputStream
                jsonResponse = readFromStream(inputStream)!!

                Log.e(TAG, "Error ${urlConnection.responseCode} ${urlConnection.responseMessage}")
            }
        }
        catch (e : IOException){
            Log.e(TAG, "problem retrieving eartchquake results", e)
        }
        finally {
            if(urlConnection != null) urlConnection.disconnect()
            if (inputStream != null) inputStream.close()
        }

        return jsonResponse
    }

    private fun readFromStream(stream : InputStream?) : String? {
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

        return stringBuilder.toString()
    }

    fun extractHikeLocations(json : String?) : ArrayList<HikeLocationData>? {
        if(TextUtils.isEmpty(json)){
            Log.d(TAG, "isEmpty")
            return null
        }

        val hikeLocationList = ArrayList<HikeLocationData>()

        try{
            val root : JSONObject

            if(json != null){
                root = JSONObject(json)
                val results : JSONArray = root.getJSONArray("results")

                val length = results.length()

                for(i in 0..length-1){
                    val locationItem = results.getJSONObject(i)
                    val arrayItem = HikeLocationData()
                    arrayItem.name = locationItem.getString("name")
                    arrayItem.address = locationItem.getString("formatted_address")
                    arrayItem.imageUrl = locationItem.getString(locationItem.getJSONArray("photos").getJSONObject(0).getString("photo_reference"))
                    arrayItem.rating = locationItem.getDouble("rating")
                    arrayItem.latitude = locationItem.getJSONObject("viewport").getJSONObject("northeast").getString("lat").toDouble()
                    arrayItem.longitude = locationItem.getJSONObject("viewport").getJSONObject("northeast").getString("lng").toDouble()
                }
            }
        }
        catch (e : JSONException){
            Log.d(TAG, e.toString())
        }

        return hikeLocationList
    }

    fun getHikeLOcations(link: String): ArrayList<HikeLocationData>? {

        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        val url = createUrl(link)

        Log.d("URL", url.toString())

        var json: String? = null
        try {
            json = makeHttpRequest(url!!)
            if (json != null) {
                Log.d("json", "not null")
            }
        } catch (e: IOException) {
            Log.d("URL CONNECTION", "IO", e)
        }

        return extractHikeLocations(json)
    }




}