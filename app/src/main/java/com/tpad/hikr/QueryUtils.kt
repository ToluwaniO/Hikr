package com.tpad.hikr

import android.graphics.Bitmap
import android.text.TextUtils
import android.util.Log
import com.tpad.hikr.DataClasses.HikeLocationData
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import android.provider.MediaStore.Images.Media.getBitmap
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.ui.PlacePicker.getAttributions
import com.google.android.gms.location.places.PlacePhotoMetadata
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer
import com.google.android.gms.location.places.ui.PlaceAutocomplete.getStatus
import com.google.android.gms.location.places.Places
import com.google.android.gms.location.places.PlacePhotoMetadataResult
import android.text.Html
import android.R.attr.bitmap
import android.content.Context
import android.location.Geocoder
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import com.google.android.gms.maps.model.LatLng
import java.io.*
import java.util.*


/**
 * Created by oguns on 7/14/2017.
 */

class QueryUtils(url: String) {

    private var link: String = ""

    init {
        this.link = url
    }

    companion object {
        var mGoogleApiClient : GoogleApiClient? = null
        var context : Context? = null

        private val TAG = QueryUtils::class.java.simpleName

        fun setApiClient(client : GoogleApiClient) {
            mGoogleApiClient = client
        }

        fun putContext(c : Context) {
            context = c
        }
        fun createUrl(a : String) : URL {
            var url : URL? = null

            try {
                url = URL(a)
                Log.d(TAG, "url is not null")
            }
            catch (e : Exception){
                Log.d(TAG, "url is null")
                e.printStackTrace()
            }

            return url!!
        }

        fun makeHttpRequest(url : URL) : String {
            var jsonResponse = ""

            if (url.toString() == ""){
                Log.d("URL", "null")
                return null!!
            }

            Log.d(TAG, url.toString())

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

            return jsonResponse!!
        }

        @Throws(IOException::class)
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

            Log.d(TAG, stringBuilder.toString())
            return stringBuilder.toString()
        }

        fun extractHikeLocations(json : String?) : ArrayList<HikeLocationData> {
            if(TextUtils.isEmpty(json)){
                Log.d(TAG, "isEmpty")
                return null!!
            }

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
                            arrayItem.latitude = locationItem.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                            arrayItem.longitude = locationItem.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                            //arrayItem.city = )
                            val city = getCityName(LatLng(arrayItem.latitude, arrayItem.longitude))
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
                }
            }
            catch (e : JSONException){
                Log.d(TAG, e.toString())
            }

            return hikeLocationList
        }

        fun getHikeLocations(link: String): ArrayList<HikeLocationData> {
            Log.d(TAG, "get hike location called")
            try {
                Thread.sleep(2000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            val url = createUrl(link)

            Log.d(TAG, url.toString())

            var json: String? = null
            try {
                json = makeHttpRequest(url!!)
                if (json != null) {
                    Log.d(TAG, "json not null")
                }
            } catch (e: IOException) {
                Log.d(TAG, "IO", e)
            }

            return extractHikeLocations(json)
        }

        public fun addPhoto(placeId : String) : Bitmap? {
            val result = Places.GeoDataApi
                    .getPlacePhotos(mGoogleApiClient, placeId).await()
            var photoMetadataBuffer : PlacePhotoMetadataBuffer?= null
            // Get a PhotoMetadataBuffer instance containing a list of photos (PhotoMetadata).
            if (result != null && result.status.isSuccess) {
                photoMetadataBuffer = result.photoMetadata

                // Get the first photo in the list.
                val photo = photoMetadataBuffer.get(0)
                // Get a full-size bitmap for the photo.
                val image = photo.getPhoto(mGoogleApiClient).await()
                        .getBitmap()
                // Get the attribution text.
                val attribution = photo.getAttributions()
                Log.d(TAG, attribution.toString())

                photoMetadataBuffer.release()

                return image
            }
            return null
        }

        private fun getCityName(latLng: LatLng): String? {
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
    }

}