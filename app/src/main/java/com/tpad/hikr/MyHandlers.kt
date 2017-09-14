package com.tpad.hikr

/**
 * Created by oguns on 7/30/2017.
 */
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.support.v4.app.FragmentManager
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tpad.hikr.DataClasses.ActiveHikesData
import com.tpad.hikr.DataClasses.Review
import com.tpad.hikr.Fragments.DatePickerFragment

class MyHandlers {


    fun onStartHikingClick(view: View, placeId: String) {
//        val datePickerDialog = DatePickerFragment()
//
//        user?.let { databaseReference.child("$ACTIVE_HIKES_REFERENCE/${user.uid}").push().setValue(
//                ActiveHikesData(placeId, null, "${datePickerDialog.day}:${datePickerDialog.month}:${datePickerDialog.year}")) }
        Log.d(TAG, "onStartHikingClick")
    }

    companion object {

        fun onSubmitReviewClicked(view: View){
            val intent = Intent(view.context, ReviewActivity::class.java)
            view.context.startActivity(intent)
        }

        fun onSubmitReportClicked(view: View){
            val intent = Intent(view.context, ReportLocationActivity::class.java)
            view.context.startActivity(intent)
        }

        fun postLocationReview(rating: Float, review: String)
        {
            databaseReference = firebaseDatabase.getReference("ratings_reviews")
            val reviewRating = Review(rating, review, System.currentTimeMillis())
            user?.let { databaseReference.child(user.uid).setValue(reviewRating) }
        }

        private val TAG = MyHandlers::class.java.simpleName
        private val ACTIVE_HIKES_REFERENCE = "ActiveHikes"
        private val HIKE_DATA = "HikeData"
        private val firebaseDatabase = FirebaseDatabase.getInstance()
        private var databaseReference: DatabaseReference = firebaseDatabase.getReference(HIKE_DATA)
        private val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    }
}
