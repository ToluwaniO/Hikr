package com.tpad.hikr.WebUtils

import android.text.TextUtils
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.tpad.hikr.BuildConfig
import com.tpad.hikr.DataClasses.Weather
import com.tpad.hikr.QueryUtils
import org.json.JSONException
import org.json.JSONObject
import java.net.URL
import java.util.*

/**
 * Created by oguns on 8/26/2017.
 */
class QueryWeather{
    companion object {
        val API_KEY = BuildConfig.OPEN_WEATHER_MAP_KEY
        val LOG_TAG = QueryWeather::class.java.simpleName
        fun createUrl(url: String) = URL(url)

        fun parseJson(jsonString: String): ArrayList<Weather> {
            val weatherList = ArrayList<Weather>()

            try {
                val json = JSONObject(jsonString)
                val list = json.getJSONArray("list")

                for (i in 0..list.length() - 1) {
                    val main = list.getJSONObject(i).getJSONObject("main")
                    val min = main.getDouble(WeatherContract.MIN_TEMP)
                    val max = main.getDouble(WeatherContract.MAX_TEMP)
                    val temp = main.getDouble(WeatherContract.TEMP)
                    val icon = list.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString(WeatherContract.ICON)
                    val date = list.getJSONObject(i).getString(WeatherContract.DATE).split(" ")[0]
                    Log.d(LOG_TAG, date)

                    if (weatherList.isNotEmpty()) {
                        val oldDate = weatherList.get(weatherList.lastIndex)

                        if (!date.equals(oldDate)) {
                            weatherList.add(Weather(temp, min, max, date, icon))
                            Log.d(LOG_TAG, "Weather added")
                        }
                    } else {
                        weatherList.add(Weather(temp, min, max, date, icon))
                    }
                }
            }
            catch (e: JSONException) {
                Log.d(LOG_TAG, e.toString())
            }
            return weatherList
        }

        fun getWeatherData(latLng: LatLng): ArrayList<Weather> {
            val lat = latLng.latitude
            val lon = latLng.longitude
            var urlString = WeatherContract.URL.replace("{lat}", lat.toString())
            urlString = urlString.replace("{lon}", lon.toString())
            urlString = urlString.replace("{APIKEY}", API_KEY)
            val url = createUrl(urlString)
            val json = QueryUtils.makeHttpRequest(url)

            return parseJson(json)
        }
    }
}