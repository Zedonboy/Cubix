package com.redwasp.cubix.utils

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.redwasp.cubix.R
import com.redwasp.cubix.arch.IAdapter

class SearchableAdapter : RecyclerView.Adapter<ViewHolder>(), IAdapter<Feed>{
    private val dataContainer : MutableList<Feed> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.material_book,parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = dataContainer.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = holder.view
        val title = view.findViewById<TextView>(R.id.material_book_title)
        //val desc = view.findViewById<TextView>(R.id.material_book_desc)
        title.text = dataContainer[position].title
    }

    override fun addData(data: Collection<Feed>) {
       dataContainer.addAll(data)
    }

}