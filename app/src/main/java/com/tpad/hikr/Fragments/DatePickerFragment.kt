package com.tpad.hikr.Fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.format.DateFormat
import android.widget.DatePicker
import java.util.*

/**
 * Created by oguns on 7/31/2017.
 */
class DatePickerFragment: DialogFragment(), DatePickerDialog.OnDateSetListener{
    internal var day: Int
    internal var month: Int
    internal var year: Int
    internal val calendar = Calendar.getInstance()

    init{
        day =calendar.get(Calendar.DAY_OF_MONTH)
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DatePickerDialog(activity, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        this.day = dayOfMonth
        this.month = month
        this.year = year
    }

}