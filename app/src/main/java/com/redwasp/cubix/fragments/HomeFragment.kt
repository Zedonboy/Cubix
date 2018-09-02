package com.redwasp.cubix.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.redwasp.cubix.App
import com.redwasp.cubix.DiscoverActivity
import com.redwasp.cubix.R
import com.redwasp.cubix.utils.Feed
import com.redwasp.cubix.utils.Network
import com.redwasp.cubix.utils.PBAdapter
import kotlinx.android.synthetic.main.fragment_feed_list.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
private lateinit var recyclerView : RecyclerView
    private lateinit var network : Network

    @Suppress("UNCHECKED_CAST")
    override fun onRefresh() {
        (recyclerView.adapter as PBAdapter<Feed>).clearData()
        recyclerView.visibility = View.GONE
        feed_list_error_constraint_layout?.visibility = View.GONE
        fragment_feed_list_progressbar?.visibility = View.VISIBLE
        feed_list_swipe_refresh_layout?.isRefreshing = true
        getData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as DiscoverActivity).SelectTab = R.id.home
        (activity as DiscoverActivity).setUpToolBAr()
        return inflater.inflate(R.layout.fragment_feed_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI()
        getData()
    }

    private fun initUI(){
        recyclerView = feed_list_recyclerView!!.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = PBAdapter<Feed>()
        }
        feed_list_swipe_refresh_layout?.setOnRefreshListener(this)
        network = (activity?.application as App).network
    }

    @Suppress("UNCHECKED_CAST")
    private fun getData(){

        val deferred = async { network.getFeeds() }
        launch {
            try {
                val list = deferred.await()
                if (list.isEmpty()){
                    withContext(UI){
                        this@HomeFragment.showError()
                    }
                    return@launch
                }
                withContext(UI){
                    (this@HomeFragment.recyclerView.adapter as PBAdapter<Feed>).addData(list)
                    this@HomeFragment.showLists()
                    this@HomeFragment.recyclerView.adapter.notifyDataSetChanged()
                }
            } catch (e : Exception) {
                withContext(UI){
                    this@HomeFragment.showError()
                }
            }
        }
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