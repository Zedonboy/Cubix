package com.redwasp.cubix.utils

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.redwasp.cubix.FeedRecord
import com.redwasp.cubix.R
import com.redwasp.cubix.arch.IAdapter
import com.redwasp.cubix.arch.IMaterialRackFragment
import com.redwasp.cubix.fragments.ReadingFragment

class RackAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(), IAdapter<FeedRecord> {
    private val dataContainer = mutableListOf<FeedRecord>()
    private lateinit var controller : IMaterialRackFragment
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
            val feedrecord = dataContainer[holder.adapterPosition]
            val title = view.findViewById<TextView>(R.id.material_book_title)
            val desc = view.findViewById<TextView>(R.id.material_book_desc)

            title.text = feedrecord.title
            desc.text = feedrecord.body
            view.setOnClickListener { _ ->
                // call on read activiy
                if(this@RackAdapter::controller.isInitialized){
                    val  feedrec = dataContainer[holder.adapterPosition]
                    val fragment = ReadingFragment.newInstance(feedrec.title, null, feedrec.body)
                    controller.navToView(fragment)
                }
            }
        }
    }

    override fun addData(data: Collection<FeedRecord>) {
        dataContainer.addAll(data)
    }

    fun addCOntrollingFragment(activity : IMaterialRackFragment){
        controller = activity
    }
}