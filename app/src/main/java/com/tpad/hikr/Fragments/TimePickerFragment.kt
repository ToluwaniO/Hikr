package com.tpad.hikr.Fragments

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.format.DateFormat
import android.widget.TimePicker
import java.util.*
import android.text.format.DateFormat.is24HourFormat



/**
 * Created by oguns on 7/31/2017.
 */
class TimePickerFragment: DialogFragment(), TimePickerDialog.OnTimeSetListener {
    internal var hour: Int
    internal var minute: Int
    val calendar = Calendar.getInstance()

    init {
        hour = calendar.get(Calendar.HOUR_OF_DAY)
        minute = calendar.get(Calendar.MINUTE)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return TimePickerDialog(activity, this, hour, minute,
                DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        this.hour = hourOfDay
        this.minute = minute
    }



}