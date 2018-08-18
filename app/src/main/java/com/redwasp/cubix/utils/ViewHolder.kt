package com.redwasp.cubix.utils

import android.support.v7.widget.RecyclerView
import android.view.View

// Created its own class here to obey Dependency Injection
class ViewHolder(val view : View) : RecyclerView.ViewHolder(view){
    var viewType : Int = -1
    constructor(view: View, viewType : Int) : this(view){
        this.viewType = viewType
    }
}
