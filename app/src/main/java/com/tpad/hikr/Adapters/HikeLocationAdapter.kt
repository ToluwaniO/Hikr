package com.tpad.hikr.Adapters

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.disklrucache.DiskLruCache
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.tpad.hikr.DataClasses.HikeLocationData
import com.tpad.hikr.HikeLocationActivity
import com.tpad.hikr.R
import org.afinal.simplecache.ACache
import java.io.File
import java.util.ArrayList

/**
 * Created by oguns on 7/20/2017.
 */
class HikeLocationAdapater(internal var hikeLocationDataList: ArrayList<HikeLocationData>, internal var context: Context, internal var googleApiClient: GoogleApiClient) : RecyclerView.Adapter<HikeLocationAdapater.MyViewHolder>() {
    private val LOCATION_TYPE = 0
    private val LOCATION_CARD_TYPE = 1

    init {
        diskcache = ACache.get(context)
    }

    inner class MyViewHolder : RecyclerView.ViewHolder {
        internal var name: TextView
        internal lateinit var city: TextView
        internal lateinit var distance: TextView
        internal lateinit var image: ImageView
        internal lateinit var cardView: CardView
        internal lateinit var ratingBar: RatingBar
        internal lateinit var loadingBar: ProgressBar
        internal var type: Int = 0

        constructor(itemView: View) : super(itemView) {
            cardView = itemView.findViewById(R.id.discover_card) as CardView
            name = itemView.findViewById(R.id.discover_name) as TextView
            city = itemView.findViewById(R.id.discover_city_name) as TextView
            distance = itemView.findViewById(R.id.discover_distance) as TextView
            image = itemView.findViewById(R.id.dicover_location_image) as ImageView
            ratingBar = itemView.findViewById(R.id.discover_rating_bar) as RatingBar
            loadingBar = itemView.findViewById(R.id.discover_image_loader) as ProgressBar
        }

        constructor(itemView: View, type: Int) : super(itemView) {
            name = itemView.findViewById(R.id.location_txt_view) as TextView
            this.type = type
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View

        when (viewType) {
            LOCATION_TYPE -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.current_location_layout, parent, false)
                return MyViewHolder(view, 1)
            }
            else -> view = LayoutInflater.from(parent.context).inflate(R.layout.discover_card, parent, false)
        }

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.d(TAG, "TYPE - " + holder.itemViewType)
        Log.d(TAG, "POSITION - " + position)

        when (holder.itemViewType) {

            LOCATION_TYPE -> holder.name.text = hikeLocationDataList[position].city
            else -> {
                holder.name.text = hikeLocationDataList[position].name
                holder.city.text = hikeLocationDataList[position].address
                holder.distance.text = hikeLocationDataList[position].distance.toString()
                holder.loadingBar.visibility = View.VISIBLE
                holder.image.visibility = View.GONE
                val b = getBitmapFromMemCache(hikeLocationDataList[position].placeId)
                if (b != null) {
                    Log.d(TAG, "Cache used")
                    holder.loadingBar.visibility = View.GONE
                    holder.image.visibility = View.VISIBLE
                    holder.image.setImageBitmap(b)
                } else {
                    PhotoTask(holder.image.maxWidth, holder.image.maxHeight, position, holder).execute(hikeLocationDataList[position].placeId)
                }

                //holder.image.setImageBitmap(hikeLocationDataList.get(position).getImage());
                holder.ratingBar.max = 5
                holder.ratingBar.rating = hikeLocationDataList[position].rating.toFloat()

                holder.cardView.setOnClickListener {
                    val intent = Intent(context, HikeLocationActivity::class.java)
                    val bundle = Bundle()
                    bundle.putParcelable(HikeLocationData::class.java.simpleName, hikeLocationDataList[position])
                    //intent.putExtra("image", hikeLocationDataList.get(position).getImage());
                    intent.putExtras(bundle)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return hikeLocationDataList.size
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) return LOCATION_TYPE
        return LOCATION_CARD_TYPE
    }

    fun getBitmapFromMemCache(key: String): Bitmap? {
        val b = diskcache.getAsBitmap(key)
        return b
    }

    internal inner class PhotoTask(private val mWidth: Int, private val mHeight: Int, private val position: Int, private val holder: MyViewHolder) : AsyncTask<String, Void, PhotoTask.AttributedPhoto>() {

        /**
         * Loads the first photo for a place id from the Geo Data API.
         * The place id must be the first (and only) parameter.
         */
        override fun doInBackground(vararg params: String): AttributedPhoto? {
            if (params.size != 1) {
                Log.d(TAG, "params length is null")
                return null
            }
            val placeId = params[0]
            var attributedPhoto: AttributedPhoto? = null

            val result = Places.GeoDataApi
                    .getPlacePhotos(googleApiClient, placeId).await()

            if (result.status.isSuccess) {
                val photoMetadataBuffer = result.photoMetadata
                if (photoMetadataBuffer.count > 0 && !isCancelled) {
                    // Get the first bitmap and its attributions.
                    val photo = photoMetadataBuffer.get(0)
                    val attribution = photo.attributions
                    // Load a scaled bitmap for this photo.
                    val image = photo.getScaledPhoto(googleApiClient, mWidth, mHeight).await()
                            .bitmap

                    attributedPhoto = AttributedPhoto(attribution, image)
                }
                // Release the PlacePhotoMetadataBuffer.
                photoMetadataBuffer.release()
            }
            return attributedPhoto
        }

        override fun onPostExecute(attributedPhoto: AttributedPhoto?) {
            if (attributedPhoto != null && attributedPhoto.bitmap != null) {
                holder.loadingBar.visibility = View.GONE
                holder.image.visibility = View.VISIBLE
                holder.image.setImageBitmap(attributedPhoto.bitmap)
                diskcache.put(hikeLocationDataList[position].placeId, attributedPhoto.bitmap)
                Log.d(TAG, "cache created")
            }

        }

        /**
         * Holder for an image and its attribution.
         */
        internal inner class AttributedPhoto(val attribution: CharSequence, val bitmap: Bitmap?)
    }

    companion object {
        private val TAG = HikeLocationAdapater::class.java.simpleName
        lateinit var diskcache: ACache
    }
}
