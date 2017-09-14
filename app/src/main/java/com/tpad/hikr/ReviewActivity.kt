package com.tpad.hikr

import android.content.Intent
import android.databinding.DataBindingUtil.setContentView
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.util.Log
import com.google.android.gms.internal.va
import com.tpad.hikr.Fragments.ReviewFragment

import com.tpad.hikr.R

class ReviewActivity : AppCompatActivity(), ReviewFragment.ReviewListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        supportActionBar?.hide()
    }
    override fun onSubmitClicked(review: String, rating: Float) {
        Log.d(TAG, review)
        val view = findViewById(R.id.review_activity)
        val snackbar: Snackbar
        if(TextUtils.isEmpty(review))
        {
            snackbar = Snackbar.make(view, getString(R.string.review_empty), Snackbar.LENGTH_SHORT)
            snackbar.show()
        }
        else if(rating == 0F)
        {
            snackbar = Snackbar.make(view, getString(R.string.rating_zero), Snackbar.LENGTH_SHORT)
            snackbar.show()
        }
        else {
            MyHandlers.Companion.postLocationReview(rating, review)
        }
    }

    companion object {
        private val TAG = ReviewActivity::class.java.simpleName
    }
}
