package com.tpad.hikr

/**
 * Created by oguns on 7/30/2017.
 */
import android.app.Activity
import android.app.DatePickerDialog
import android.support.v4.app.FragmentManager
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tpad.hikr.DataClasses.ActiveHikesData
import com.tpad.hikr.Fragments.DatePickerFragment

/**
 * Created by oguns on 7/30/2017.
 */

class MyHandlers {
    private var databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference(HIKE_DATA)
    private val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    fun onStartHikingClick(view: View, placeId: String) {
        val datePickerDialog = DatePickerFragment()

//        val activity = view.context as Activity
//        val manager: FragmentManager = activity.fragmentManager
//        datePickerDialog.show(, "")
        user?.let { databaseReference.child("$ACTIVE_HIKES_REFERENCE/${user.uid}").push().setValue(
                ActiveHikesData(placeId, null, "${datePickerDialog.day}:${datePickerDialog.month}:${datePickerDialog.year}")) }
        Log.d(TAG, "onStartHikingClick")
    }

    companion object {

        private val TAG = MyHandlers::class.java.simpleName
        private val ACTIVE_HIKES_REFERENCE = "ActiveHikes"
        private val HIKE_DATA = "HikeData"
    }
}
