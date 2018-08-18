package com.redwasp.cubix.utils

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.redwasp.cubix.R
import com.redwasp.cubix.arch.IAdapter
import com.redwasp.cubix.arch.IPresenter
import com.redwasp.cubix.fragments.ReadingFragment

class RackAdapter(private val presenter : IPresenter) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), IAdapter<Feed> {
    private val dataContainer = mutableListOf<Feed>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.material_book, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataContainer.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ViewHolder){
            val view = holder.view
            view.setOnClickListener { _ ->
                presenter.navigate(ReadingFragment())
            }
        }
    }

    override fun addData(data: Collection<Feed>) {
        dataContainer.addAll(data)
    }
}