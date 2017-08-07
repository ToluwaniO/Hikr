package com.tpad.hikr.Fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import com.tpad.hikr.R

/**
 * Created by oguns on 8/7/2017.
 */
class RatingFragment : Fragment(){
    lateinit var rating: RatingBar
    lateinit var ratingCallback: onRatingChangedListener

    interface onRatingChangedListener{
        fun onRatingChanged(value: Float)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.rating_fragment_layout, container, false)

        rating = view?.findViewById(R.id.ratingBar) as RatingBar

        rating.setOnRatingBarChangeListener(object : RatingBar.OnRatingBarChangeListener {
            override fun onRatingChanged(ratingBar: RatingBar?, rating: Float, fromUser: Boolean) {
                ratingCallback.onRatingChanged(rating)
            }

        })

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            ratingCallback = context as onRatingChangedListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + " must implement onRatingChangedListener")
        }

    }
}