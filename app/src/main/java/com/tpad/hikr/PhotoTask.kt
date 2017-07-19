package com.tpad.hikr

import android.graphics.Bitmap
import android.provider.MediaStore.Images.Media.getBitmap
import com.google.android.gms.location.places.ui.PlacePicker.getAttributions
import com.google.android.gms.location.places.PlacePhotoMetadata
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer
import com.google.android.gms.location.places.ui.PlaceAutocomplete.getStatus
import com.google.android.gms.location.places.Places
import com.google.android.gms.location.places.PlacePhotoMetadataResult
import android.os.AsyncTask
import com.google.android.gms.common.api.GoogleApiClient


/**
 * Created by oguns on 7/19/2017.
 */
internal abstract class PhotoTask(private val mWidth: Int, private val mHeight: Int, val mGoogleApiClient: GoogleApiClient) : AsyncTask<String, Void, PhotoTask.AttributedPhoto>() {

    /**
     * Loads the first photo for a place id from the Geo Data API.
     * The place id must be the first (and only) parameter.
     */
    override fun doInBackground(vararg params: String): AttributedPhoto? {
        if (params.size != 1) {
            return null
        }
        val placeId = params[0]
        var attributedPhoto: AttributedPhoto? = null

        val result = Places.GeoDataApi
                .getPlacePhotos(mGoogleApiClient, placeId).await()

        if (result.status.isSuccess) {
            val photoMetadataBuffer = result.photoMetadata
            if (photoMetadataBuffer.getCount() > 0 && !isCancelled) {
                // Get the first bitmap and its attributions.
                val photo = photoMetadataBuffer.get(0)
                val attribution = photo.getAttributions()
                // Load a scaled bitmap for this photo.
                val image = photo.getScaledPhoto(mGoogleApiClient, mWidth, mHeight).await()
                        .getBitmap()

                attributedPhoto = AttributedPhoto(attribution, image)
            }
            // Release the PlacePhotoMetadataBuffer.
            photoMetadataBuffer.release()
        }
        return attributedPhoto
    }

    /**
     * Holder for an image and its attribution.
     */
    internal inner class AttributedPhoto(val attribution: CharSequence, val bitmap: Bitmap)
}