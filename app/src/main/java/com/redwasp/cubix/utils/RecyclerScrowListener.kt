package com.redwasp.cubix.utils

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class RecyclerScrowListener : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        val layoutManager =  recyclerView?.layoutManager
        if (layoutManager is LinearLayoutManager){

        }
    }
}