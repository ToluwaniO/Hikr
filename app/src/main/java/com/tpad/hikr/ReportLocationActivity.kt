package com.tpad.hikr

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.tpad.hikr.Fragments.ReportLocationFragment
import com.tpad.hikr.Fragments.ReviewFragment

import com.tpad.hikr.R

class ReportLocationActivity : AppCompatActivity(), ReportLocationFragment.ReportListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_location)

        supportActionBar?.hide()
    }

    override fun reportClicked(report: String) {
        Log.d(LOG_TAG, report)
    }

    companion object {
        private val LOG_TAG = ReportLocationActivity::class.java.simpleName
    }
}
