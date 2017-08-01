package com.tpad.hikr

import android.content.Context
import android.graphics.Bitmap
import org.afinal.simplecache.ACache

/**
 * Created by oguns on 7/30/2017.
 */
class PictureCache{
    companion object {
        fun getPicture(key: String, context: Context): Bitmap? {
            val cache = ACache.get(context)
            return cache.getAsBitmap(key)
        }
    }
}