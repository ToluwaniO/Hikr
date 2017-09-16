package com.tpad.hikr.Fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.tpad.hikr.R

/**
 * Created by oguns on 8/21/2017.
 */
class ReportLocationFragment: Fragment(){

    internal lateinit var rootView: View
    internal lateinit var submitButton: Button
    internal lateinit var reportText: EditText
    internal lateinit var callback: ReportListener

    interface ReportListener{
        fun reportClicked(report: String)
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater?.inflate(R.layout.report_location_fragment_layout, container, false) as View

        submitButton = rootView.findViewById(R.id.report_button) as Button
        reportText = rootView.findViewById(R.id.report_location_edittext) as EditText

        submitButton.setOnClickListener(View.OnClickListener { callback.reportClicked(reportText.text.toString()) })
        return rootView
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            callback = context as ReportListener
        }catch (e: Exception){
            throw ClassCastException("${context.toString()} must implement ReviewListener}")
        }
    }
}