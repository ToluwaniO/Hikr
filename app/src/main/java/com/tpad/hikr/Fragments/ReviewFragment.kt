package com.tpad.hikr.Fragments

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import com.tpad.hikr.R

/**
 * Created by oguns on 8/19/2017.
 */
class ReviewFragment: Fragment(){
    internal lateinit var rootView: View
    internal lateinit var reviewText: EditText
    internal lateinit var ratingView: RatingBar
    internal lateinit var submitButton: Button
    internal lateinit var reviewCallback: ReviewListener

    interface ReviewListener{
        fun onSubmitClicked(review: String, rating: Float)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater?.inflate(R.layout.review_fragment_layout, container, false) as View

        reviewText = rootView.findViewById(R.id.review_text) as EditText
        ratingView = rootView.findViewById(R.id.review_rating) as RatingBar
        submitButton = rootView.findViewById(R.id.submit_review) as Button

        submitButton.setOnClickListener(View.OnClickListener {
            reviewCallback.onSubmitClicked(reviewText.text.toString(), ratingView.rating) })

        return rootView
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            reviewCallback = context as ReviewListener
        }
        catch (e: Exception)
        {
            throw ClassCastException("${context.toString()} must implement ReviewListener}")
        }
    }
}