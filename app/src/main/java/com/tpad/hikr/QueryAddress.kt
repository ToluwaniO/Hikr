package com.tpad.hikr

import android.content.Context
import android.location.Geocoder
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import java.util.*

/**
 * Created by oguns on 8/19/2017.
 */
class QueryAddress{
    companion object {
        private val LOG_TAG = QueryAddress::class.java.simpleName

        fun getCityFromLatLng(latLng: LatLng, context: Context): String? {
            var geocoder = Geocoder(context, Locale.getDefault())

            try {
                var addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                return addresses[0].locality
            } catch (e: IOException) {
                Log.d(LOG_TAG, "error: ${e.toString()}")
            }
            return null
        }

        fun getCityFromAddress(address: String): String? {
            Log.d(LOG_TAG, "get city name from address - $address")
            var list: List<String>? = null
            if(address.contains(','))
            {
                list = address.split(",")
                Log.d(LOG_TAG, ", is present $list")
            }

            if(list != null && list.size >= 2){
                Log.d(LOG_TAG, ", is not present $list")
                return list[1]
            }
            return null
        }

        fun getCountryName(latLng: LatLng, context: Context): String? {
            val geocoder = Geocoder(context, Locale.getDefault())
            try {
                val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

                if (addresses.size > 0) {
                    return addresses[0].countryName
                }
            } catch (e: IOException) {
                Log.d(LOG_TAG, "error: ${e.toString()}")
            }
            return null
        }
    }
}