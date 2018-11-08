package com.redwasp.cubix.customViews

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.redwasp.cubix.R

class InterestItem(context: Context?) : FrameLayout(context){
    private var _selected = false
    val izSelected
    get() = _selected
    var imageResource : Int = 0
    set(value) {
       val img = rootView.findViewById<ImageView>(R.id.interest_item_category_image)
        img.setImageResource(value)
    }

    var title : String
    set(value){
        val textview = rootView.findViewById<TextView>(R.id.interest_item_category_title)
        textview.text = value
    }
    get() {
        val textview = rootView.findViewById<TextView>(R.id.interest_item_category_title)
        return textview.text.toString()
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.interest_item, this)
    }

    fun selected(){
        _selected = true
        rootView.findViewById<View>(R.id.interest_item_check).visibility = View.VISIBLE
    }
    fun deselect(){
        _selected = false
        rootView.findViewById<View>(R.id.interest_item_check).visibility = View.INVISIBLE
    }
}