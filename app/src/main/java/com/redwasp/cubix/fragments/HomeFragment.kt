package com.redwasp.cubix.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentSnapshot
import com.redwasp.cubix.App
import com.redwasp.cubix.DiscoverActivity
import com.redwasp.cubix.R
import com.redwasp.cubix.utils.Feed
import com.redwasp.cubix.utils.Network
import com.redwasp.cubix.utils.PBAdapter
import kotlinx.android.synthetic.main.fragment_feed_list.*
import kotlinx.android.synthetic.main.fragment_profile.*

const val queryLimit = 50

class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var network : Network
    private var lastDocumentSnapshot : DocumentSnapshot? = null
    @Suppress("UNCHECKED_CAST")
    override fun onRefresh() {
        (feed_list_recyclerView?.adapter as PBAdapter<Feed>).clearData()
        feed_list_recyclerView?.visibility = View.GONE
        feed_list_error_constraint_layout?.visibility = View.GONE
        fragment_feed_list_progressbar?.visibility = View.VISIBLE
        feed_list_swipe_refresh_layout?.isRefreshing = true
        getData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as DiscoverActivity).selectTab = R.id.home
        (activity as DiscoverActivity).setUpToolBAr()
        return inflater.inflate(R.layout.fragment_feed_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI()
        getData()
    }

    private fun initUI(){
        feed_list_recyclerView!!.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = PBAdapter<Feed>().apply {
                setActivity = activity as DiscoverActivity
                setDaoSession = (activity?.application as App).daoSession
                Network = (activity?.application as App).network
            }
        }
        feed_list_swipe_refresh_layout?.setOnRefreshListener(this)
        network = (activity?.application as App).network
    }

    @Suppress("UNCHECKED_CAST")
    private fun getData(){
        val db = (activity as DiscoverActivity).db
        val latestFeedsRef = db.collection("feeds/latest/latestFeeds")

        val mListOfFeeds = mutableListOf<Feed>()
        val firestoreQuery = with(latestFeedsRef){
            if (lastDocumentSnapshot != null) startAfter(lastDocumentSnapshot!!)
            orderBy("date")
            limit(queryLimit.toLong())
        }

        firestoreQuery.get().addOnSuccessListener { snapshot ->
            if (snapshot.documents.isEmpty()) return@addOnSuccessListener
            lastDocumentSnapshot = snapshot.documents[snapshot.size() - 1]
            for (snapDocuments in snapshot.documents){
                val feed = snapDocuments.toObject(Feed::class.java)
                feed?:break
                mListOfFeeds.add(feed)
            }
            if (mListOfFeeds.isNotEmpty()){
                if (feed_list_recyclerView?.adapter is PBAdapter<*>){
                    (feed_list_recyclerView?.adapter as PBAdapter<Feed>).addData(mListOfFeeds)
                    showLists()
                    feed_list_recyclerView?.adapter?.notifyDataSetChanged()
                }
            }
        }.addOnFailureListener { _ ->
            showError()
        }
        (activity as DiscoverActivity).getDataBasedOnInterests(feed_list_recyclerView?.adapter as PBAdapter<Feed>, ::showLists)
        (activity as DiscoverActivity).getDataBasedOnDiscipline(feed_list_recyclerView?.adapter as PBAdapter<Feed>, ::showLists)
    }

    private fun showError(){
        feed_list_swipe_refresh_layout?.isRefreshing = false
        fragment_feed_list_progressbar?.visibility = View.GONE
        feed_list_error_constraint_layout?.visibility = View.VISIBLE
    }

    private fun showLists(){
        feed_list_swipe_refresh_layout?.isRefreshing = false
            fragment_feed_list_progressbar?.visibility = View.GONE
            feed_list_recyclerView?.visibility = View.VISIBLE
    }
}