package com.redwasp.cubix.utils

import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.redwasp.cubix.*
import com.redwasp.cubix.arch.IAdapter
import com.redwasp.cubix.arch.IDiscoverActivity
import com.redwasp.cubix.fragments.ReadingFragment
import kotlinx.coroutines.android.UI
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.URL

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

    override fun getItemCount(): Int = dataContainer.size

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
                    val feed : Feed
                    try {
                       feed = dataContainer[position] as Feed
                    } catch (e : IndexOutOfBoundsException) {
                        return
                    }
                    val title = view.findViewById<TextView>(R.id.feed_item_title);
                    title.text = feed.title
                    val desc = view.findViewById<TextView>(R.id.feed_item_desc)
                    desc.text = feed.description
                    val author = view.findViewById<TextView>(R.id.feed_item_author)
                    author.text = feed.author
                    val imgBtn = view.findViewById<ImageButton>(R.id.feed_item_addToLibrary)
                    val feedPhoto = view.findViewById<ImageView>(R.id.articlePhoto)

                    if (feed.imagebase64 != null && feed.imagebase64!!.isNotBlank()) {
                        val deferred = async { Utils.Base64Coverter.convert(feed.imagebase64!!) }
                        launch {
                            val bitmapImg = deferred.await()
                            withContext(UI){
                                feedPhoto.setImageBitmap(bitmapImg)
                            }
                        }
                    } else if (feed.imageUrl != null && feed.imageUrl!!.isNotEmpty()){
                        launch {
                            try{
                                val stream = URL(feed.imageUrl).content as InputStream
                                val bitmapImg = BitmapFactory.decodeStream(stream)
                                withContext(UI){
                                    feedPhoto.setImageBitmap(bitmapImg)
                                }
                            } catch (e : Exception){
                                return@launch
                            }
                        }
                    }

                    if(feed.locked){
                        imgBtn.setImageResource(R.drawable.ic_lock_grey_600_24dp)
                        imgBtn.setOnClickListener { _ ->
                            // make a toast
                        }
                    } else {
                        imgBtn.setOnClickListener {
                            // checking if the user is registered
                            val auth = FirebaseAuth.getInstance()
                            if(auth.currentUser == null){
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
                                    val deferred = async { _network?.getFullText(feed.searchUrl) }
                                    val feedRec = FeedRecord()
                                    feedRec.searchUrl = feed.searchUrl
                                    feedRec.title = feed.title
                                    feedRec.imagebase64 = feed.imagebase64
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
                                            // image button tryinh to reset itself
                                            imageBtnView.setImageResource(R.drawable.ic_playlist_add_red_400_24dp)
                                            discoverActivity?.makeToast("${feed.title} could not be saved")
                                            imageBtnView.isEnabled = true
                                        }
                                    }
                                } catch (e : Exception){
                                    withContext(UI){
                                        // make a toast
                                        // image button trying to reset itself.
                                        imageBtnView.setImageResource(R.drawable.ic_playlist_add_red_400_24dp)
                                        discoverActivity?.makeToast("Could not download ${feed.title}, retry")
                                        imageBtnView.isEnabled = true
                                    }
                                }
                            }
                        }
                    }

                    val imgBtn1 = view.findViewById<ImageButton>(R.id.feed_item_like)
                    imgBtn1.tag = 0
                    imgBtn1.setOnClickListener {
                        // register the hash as liked shits
                        val imageBtn = it as ImageButton
                        if (imageBtn.tag == 0){
                            imageBtn.tag = 1
                            imageBtn.setImageResource(R.drawable.ic_favorite_red_400_24dp)
                        } else {
                            imageBtn.tag = 0
                            imageBtn.setImageResource(R.drawable.ic_favorite_border_red_400_24dp)
                        }
                    }

                    val container = view.findViewById<View>(R.id.feed_list_body)
                    container.setOnClickListener { _ ->
                        // call the readingListView
                        val fragment = ReadingFragment().apply {
                            searchURL = feed.searchUrl
                            this.title = feed.title
                        }
                        discoverActivity?.navigateToAnotherView(fragment)
                    }
                }
            }
        }
    }

}