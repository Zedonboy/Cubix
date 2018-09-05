package com.redwasp.cubix.utils

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import com.redwasp.cubix.*
import com.redwasp.cubix.arch.IAdapter
import com.redwasp.cubix.arch.IDiscoverActivity
import com.redwasp.cubix.fragments.ReadingFragment
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class PBAdapter<T> :
        RecyclerView.Adapter<RecyclerView.ViewHolder>(), IAdapter<T> {

    private var _network : Network? = null
    var Network : Network? = null
    set(value) {_network = value}

    private var discoverActivity : IDiscoverActivity? = null
    private var daoSession : DaoSession? = null

    var setActivity : IDiscoverActivity? = null
    set(value) {discoverActivity = value}

    var setDaoSession : DaoSession? = null
    set(value) {daoSession = value}
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
                    val feed = dataContainer[position] as Feed
                    val title = view.findViewById<TextView>(R.id.feed_item_title);
                    title.text = feed.title
                    val desc = view.findViewById<TextView>(R.id.feed_item_desc)
                    desc.text = feed.body
                    val author = view.findViewById<TextView>(R.id.feed_item_author)
                    author.text = feed.author
                    val imgBtn = view.findViewById<ImageButton>(R.id.feed_item_addToLibrary)
                    if(feed.locked){
                        imgBtn.setImageResource(R.drawable.ic_lock_grey_600_24dp)
                        imgBtn.setOnClickListener { _ ->
                            // make a toast
                        }
                    } else {
                        imgBtn.setOnClickListener {
                            val app = (discoverActivity as DiscoverActivity).application as App
                            if(app.CurrentUser == null){
                                // not signed in
                                discoverActivity?.userSignIn()
                                return@setOnClickListener
                            }
                            val imageBtnView = it as ImageButton
                            if (!imageBtnView.isEnabled) return@setOnClickListener
                            imageBtnView.isEnabled = false
                            discoverActivity?.makeToast("Saving ${feed.title}")
                            imageBtnView.setImageResource(R.drawable.ic_more_horiz_grey_500_24dp)
                            launch {
                                try {
                                    val deferred = async { _network?.getFullText(feed.contentUrl) }
                                    val feedRec = FeedRecord()
                                    feedRec.searchUrl = feed.contentUrl
                                    feedRec.title = feed.title
                                    feedRec.body = deferred.await()
                                    val record = daoSession?.feedRecordDao
                                    try {
                                        record?.insert(feedRec)
                                        withContext(UI){
                                            discoverActivity?.makeToast("${feed.title} is saved")
                                            imageBtnView.setImageResource(R.drawable.ic_playlist_add_check_green_400_24dp)
                                        }
                                    } catch (e : Exception){
                                        withContext(UI){
                                            // make a toast
                                            discoverActivity?.makeToast("${feed.title} could not be saved")
                                        }
                                    }
                                } catch (e : Exception){
                                    withContext(UI){
                                        // make a toast
                                        discoverActivity?.makeToast("Could not download ${feed.title}, retry")
                                    }
                                }
                            }
                        }
                    }

                    val imgBtn1 = view.findViewById<ImageButton>(R.id.feed_item_like)
                    imgBtn1.setOnClickListener {
                        // register the hash as liked shits
                        val imageBtn = it as ImageButton
                        imageBtn.tag = R.drawable.ic_favorite_red_400_24dp
                        if (imageBtn.tag == R.drawable.ic_favorite_red_400_24dp){
                            imageBtn.setImageResource(R.drawable.ic_favorite_border_red_400_24dp)
                        } else {
                            imageBtn.setImageResource(R.drawable.ic_favorite_red_400_24dp)
                        }
                    }

                    val container = view.findViewById<View>(R.id.feed_list_body)
                    container.setOnClickListener { _ ->
                        // call the readingListView
                        val fragment = ReadingFragment.newInstance(feed.title, feed.contentUrl, null)
                        discoverActivity?.navigateToAnotherView(fragment)
                    }
                }
            }
        }
    }

}