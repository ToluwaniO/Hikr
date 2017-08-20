package com.tpad.hikr

import android.content.Intent
import android.databinding.DataBindingUtil.setContentView
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.tpad.hikr.Fragments.ReviewFragment

import com.tpad.hikr.R

class ReviewActivity : AppCompatActivity(), ReviewFragment.ReviewListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
    }
    override fun onSubmitClicked(review: String, rating: Float) {
        Log.d(TAG, review)
    }

    companion object {
        private val TAG = ReviewActivity::class.java.simpleName
    }
}
