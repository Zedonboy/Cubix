package com.redwasp.cubix.utils

import android.support.design.widget.BottomNavigationView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.redwasp.cubix.R
import com.redwasp.cubix.arch.IAdapter
import com.redwasp.cubix.arch.IPresenter
import com.redwasp.cubix.archComponents_Presenters.DiscoverActivityPresenter
import com.redwasp.cubix.fragments.ReadingFragment

class PBAdapter<T>() :
        RecyclerView.Adapter<RecyclerView.ViewHolder>(), IAdapter<T> {
    private val dataContainer : MutableList<T> = mutableListOf()
    val count
    get() = this.dataContainer.size
    private val VIEW_ITEM = 0
    private val PROGRESS_ITEM = 1

    override fun addData(data: Collection<T>) {
        dataContainer.addAll(data)
    }

    override fun clearData() {
        dataContainer.clear()
    }

    override fun getItemViewType(position: Int): Int {
        // If these is pointing to the last Item, because of the +1 there
        if (position == dataContainer.size && dataContainer.size > 0) return PROGRESS_ITEM
        return VIEW_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            VIEW_ITEM -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.feed_item, parent, false)
                ViewHolder(view, VIEW_ITEM)
            }
            PROGRESS_ITEM -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.progress_item,parent, false)
                ViewHolder(view, PROGRESS_ITEM)
            }

            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.progress_item,parent, false)
                ViewHolder(view, PROGRESS_ITEM)
            }
        }
    }

    override fun getItemCount(): Int = dataContainer.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder){
            when(holder.viewType){
                PROGRESS_ITEM -> {
                    val view = holder.view
                    val progress = view.findViewById<ProgressBar>(R.id.feed_list_progressBar)
                    progress.animate()
                }
                VIEW_ITEM -> {
                    val view = holder.view
                    val container = view.findViewById<View>(R.id.feed_list_body)
                    val bottomNav = view.findViewById<BottomNavigationView>(R.id.feed_item_feed_btmNav)
                    bottomNav.setOnNavigationItemSelectedListener {

                        return@setOnNavigationItemSelectedListener false
                    }
                    container.setOnClickListener { _ ->

                    }
                }
            }
        }
    }

}