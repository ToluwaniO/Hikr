package com.tpad.hikr.WebUtils

/**
 * Created by oguns on 8/26/2017.
 */
class WeatherContract{

    companion object {
        val DATE = "dt_txt"
        val MIN_TEMP = "temp_min"
        val MAX_TEMP = "temp_max"
        val TEMP = "temp"
        val ICON = "icon"
        val URL = "https://api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&APPID={APIKEY}"
    }

}