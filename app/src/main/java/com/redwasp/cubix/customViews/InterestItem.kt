package com.redwasp.cubix.customViews

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.redwasp.cubix.R

class InterestItem(context: Context?) : FrameLayout(context){
    init {
        LayoutInflater.from(context).inflate(R.layout.interest_item, this)
    }

    fun selected(){
        rootView.findViewById<View>(R.id.interest_item_check).visibility = View.VISIBLE
    }
    fun deselect(){
        rootView.findViewById<View>(R.id.interest_item_check).visibility = View.INVISIBLE
    }
}