package com.redwasp.cubix

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.SearchManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_searchable.*
import android.view.View
import com.redwasp.cubix.utils.Feed
import com.redwasp.cubix.utils.Network
import com.redwasp.cubix.utils.PBAdapter
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class SearchableActivity : AppCompatActivity() {
    private lateinit var network: Network
    private lateinit var recyclerView: RecyclerView
    private var query : String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchable)
        initUI()
        getData()
    }

    private fun initUI(){
        if (Intent.ACTION_SEARCH == intent.action) {
            query = intent.getStringExtra(SearchManager.QUERY)
        }
        recyclerView = search_activity_recyclerView!!.apply {
            setHasFixedSize(true)
            adapter = PBAdapter<Feed>()
            layoutManager = LinearLayoutManager(context)
        }
        network = (application as App).network
        search_activity_searchview?.apply {
            val query = intent?.getStringExtra(SearchManager.QUERY)
            setQuery(query,false)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getData(){
        val defered = async { network.searchForFeed(query!!) }
        launch {
            try {
                val list = defered.await()
                if(list.isEmpty()){
                    withContext(UI){
                        // notify user visually
                        NoSearchResult()
                    }
                    return@launch
                }
                withContext(UI){
                    (this@SearchableActivity.recyclerView.adapter as PBAdapter<Feed>).addData(list)
                    showlists()
                    this@SearchableActivity.recyclerView.adapter.notifyDataSetChanged()
                }
            } catch (e : Exception) {
                withContext(UI){
                    // notify User visually
                    showError()
                }
            }
        }
    }

    private fun showlists(){
        search_activity_progressbar?.visibility = View.GONE
        search_activity_recyclerView?.visibility = View.VISIBLE
    }
    private fun NoSearchResult(){
        search_activity_progressbar?.visibility = View.GONE
        search_activity_noresultfound?.visibility = View.VISIBLE
    }

    private fun showError(){
        search_activity_progressbar?.visibility = View.GONE
        search_activity_errorView?.visibility = View.GONE
    }
}