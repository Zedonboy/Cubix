package com.redwasp.cubix.fragments

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.view.isVisible
import com.redwasp.cubix.App
import kotlinx.android.synthetic.main.fragment_feed_list.*

import com.redwasp.cubix.R
import com.redwasp.cubix.arch.IFeedListFragment
import com.redwasp.cubix.archComponentsModels.FeedListFragmentModel
import com.redwasp.cubix.archComponents_Presenters.FeedListPresenter
import com.redwasp.cubix.utils.Feed
import com.redwasp.cubix.utils.Network
import com.redwasp.cubix.utils.PBAdapter

class FeedListFragment : Fragment(), IFeedListFragment, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var recyclerView: RecyclerView
    private val presenter = FeedListPresenter()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        presenter.init(this, FeedListFragmentModel(Network(context)))
        presenter.setGlobalPresenter((context?.applicationContext as App).presenter)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        showDialog()
    }

    override fun toString(): String = "FeedListFragment"

    private fun showDialog() {
        val dialog = WelcomeDialog()
        dialog.show(childFragmentManager, "welcome_dialog")
    }

    private fun initUI(){
        // Initializing my recycler View
        recyclerView = feed_list_recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = PBAdapter<Feed>(presenter)
        }


        @Suppress("UNCHECKED_CAST")
        presenter.setAdapter(recyclerView.adapter as PBAdapter<Feed>)
        presenter.getFeeds()
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                val layoutManager = recyclerView?.layoutManager
                if (layoutManager is LinearLayoutManager){
                    val adapter = recyclerView.adapter as PBAdapter<*>
                    val itemPos = layoutManager.findLastVisibleItemPosition()
                    // if the last item visible item is the last data item in the adapter
                    if (itemPos == adapter.count - 1){
                        // Load more
                        // Possible Errors, calling getFeeds() without setting the adapter.
                        presenter.loadMore()
                    }
                }
            }
        })

        feed_list_swipe_refresh_layout?.setOnRefreshListener(this)

    }

    override fun update() {
        recyclerView.adapter.notifyDataSetChanged()
    }

    override fun showRecyclerView(){
        recyclerView.visibility = View.VISIBLE
    }

    override fun hideProgressBar(){
        if (fragment_feed_list_progressbar?.isVisible!!){
            fragment_feed_list_progressbar?.visibility = View.GONE
        }
    }

    override fun showError(){
        fragment_feed_list_progressbar?.visibility = View.GONE
        feed_list_error_constraint_layout?.visibility = View.VISIBLE
    }

    override fun onRefresh() {
        // when the user refresh
        // hide the list
        // show progressbar
        // if get data from net, clear the current data.
        // add data to the adapte... (Make sure you Notify)
        // else hide progressBar, show recyclerView
        // render old data
        recyclerView.visibility = View.GONE
        fragment_feed_list_progressbar?.visibility = View.VISIBLE
        presenter.getFeeds()
    }

    override fun loadMoreError(){
        val layoutManager = recyclerView.layoutManager
        val view = layoutManager.getChildAt(recyclerView.adapter.itemCount - 1)
        val progressBar = view?.findViewById<ProgressBar?>(R.id.feed_list_progressBar)
        progressBar?.visibility = View.GONE
        val errorView = view?.findViewById<ConstraintLayout?>(R.id.progress_item_error_connecting)
        errorView?.visibility = View.VISIBLE
        val tryAgainBtn = errorView?.findViewById<Button>(R.id.progress_item_try_again_btn)
        tryAgainBtn?.setOnClickListener { _ ->
            errorView.visibility = View.GONE
            progressBar?.visibility = View.VISIBLE
            presenter.loadMore()
        }
    }

}
