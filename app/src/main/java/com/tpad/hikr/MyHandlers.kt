package com.tpad.hikr

/**
 * Created by oguns on 7/30/2017.
 */
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tpad.hikr.DataClasses.ActiveHikesData
import com.tpad.hikr.DataClasses.Review
import com.tpad.hikr.Fragments.DatePickerFragment
import com.google.android.gms.location.LocationSettingsStatusCodes
import android.content.IntentSender
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.ApiException
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.tasks.OnSuccessListener



class MyHandlers {

    fun onStartHikingClick(view: View, placeId: String) {
//        val datePickerDialog = DatePickerFragment()
//
//        user?.let { databaseReference.child("$ACTIVE_HIKES_REFERENCE/${user.uid}").push().setValue(
//                ActiveHikesData(placeId, null, "${datePickerDialog.day}:${datePickerDialog.month}:${datePickerDialog.year}")) }
        Log.d(TAG, "onStartHikingClick")
    }

    companion object {

        fun onSubmitReviewClicked(view: View, placeId: String){
            val intent = Intent(view.context, ReviewActivity::class.java)
            val bundle = Bundle()
            bundle.putString("placeId", placeId)
            intent.putExtras(bundle)
            view.context.startActivity(intent)
        }

        fun onSubmitReportClicked(view: View, placeId: String){
            val intent = Intent(view.context, ReportLocationActivity::class.java)
            val bundle = Bundle()
            bundle.putString("placeId", placeId)
            intent.putExtras(bundle)
            view.context.startActivity(intent)
        }

        fun postLocationReview(view: View, placeId: String, rating: Float, review: String) {
            databaseReference = firebaseDatabase.getReference("ratings_reviews").child(placeId)
            val reviewRating = Review(rating, review, System.currentTimeMillis())
            user?.let { databaseReference.child(user.uid).setValue(reviewRating)
                    .addOnCompleteListener { displayToast(view.context, view.context.getString(R.string.rating_added))
                        (view.context as Activity).finish()}
                    .addOnFailureListener { displayToast(view.context, view.context.getString(R.string.rating_failed)) } }
        }

        fun displaySnackbar(view: View, message: String) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
        }

        fun displayToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        private val TAG = MyHandlers::class.java.simpleName
        private val ACTIVE_HIKES_REFERENCE = "ActiveHikes"
        private val HIKE_DATA = "HikeData"
        private val firebaseDatabase = FirebaseDatabase.getInstance()
        private var databaseReference: DatabaseReference = firebaseDatabase.getReference(HIKE_DATA)
        private val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    }

}
