package com.tpad.hikr

import android.util.Log
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
                jsonResponse = readFromStream(inputStream)

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

    fun extractHikeLocations(json : String) : ArrayList<>




}