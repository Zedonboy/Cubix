package com.redwasp.cubix.utils

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.redwasp.cubix.DaoSession
import com.redwasp.cubix.FeedRecord
import com.redwasp.cubix.R
import com.redwasp.cubix.arch.IAdapter
import com.redwasp.cubix.arch.IDiscoverActivity
import com.redwasp.cubix.fragments.ReadingFragment
import kotlinx.coroutines.android.UI
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RackAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(), IAdapter<FeedRecord> {
    private val dataContainer = mutableListOf<FeedRecord>()
    private var controller : IDiscoverActivity? = null
    var Controller : IDiscoverActivity? = null
    set(value) {controller = value}

    private var _daoSession : DaoSession? = null
    var daoSession : DaoSession? = null
    set(value) {_daoSession = value}
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
            val feedrecord = dataContainer[position]
            val title = view.findViewById<TextView>(R.id.material_book_title)
            val desc = view.findViewById<TextView>(R.id.material_book_desc)
            val deletebtn = view.findViewById<ImageButton>(R.id.material_book_delete)
            deletebtn.setOnClickListener {
                val record = _daoSession?.feedRecordDao
                launch {
                    try {
                        record?.delete(dataContainer[position])
                        dataContainer.removeAt(position)
                        withContext(UI){
                            notifyDataSetChanged()
                            controller?.makeToast("${feedrecord.title} is deleted")
                            return@withContext
                        }
                    } catch (e : Exception){
                        withContext(UI){
                            controller?.makeToast("${feedrecord.title} could not be deleted")
                        }
                    }
                }
            }
            val img = view.findViewById<ImageView>(R.id.material_book_photo)

            val deferred = async { Utils.Base64Coverter.convert(feedrecord.imagebase64) }
            launch {
                val bitmapImg = deferred.await()
                withContext(UI){
                    img.setImageBitmap(bitmapImg)
                }
            }

            with(title){
                text = feedrecord.title
                setOnClickListener { callReaderFrag(position) }
            }
            with(desc){
                text = feedrecord.body
                setOnClickListener { callReaderFrag(position) }
            }
        }
    }

    private fun callReaderFrag(position: Int){
        val record = dataContainer[position]
        val frag = ReadingFragment().apply {
            title = record.title
            content = record.body
            parcelableString = record.parcelableState
        }
        controller?.navigateToAnotherView(frag)
    }
    override fun addData(data: Collection<FeedRecord>) {
        dataContainer.addAll(data)
    }
}