package com.tpad.hikr.Fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.tpad.hikr.R
import android.text.Editable



/**
 * Created by oguns on 8/7/2017.
 */
class ReviewFragment : Fragment() {
    lateinit var reviewText: EditText
    lateinit var textCallBack: onTextChangeListener

    interface onTextChangeListener{
        fun onTextChanged(review: String)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.review_fragment_layout, container, false)

        reviewText = view?.findViewById(R.id.review_edit_text) as EditText

        reviewText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                textCallBack.onTextChanged(s as String)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {

                // TODO Auto-generated method stub
            }
        })

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            textCallBack = activity as onTextChangeListener
        }catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + " must implement onReviewChangedListener")
        }
    }
}

