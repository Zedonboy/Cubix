package com.redwasp.cubix

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.SearchManager
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import kotlinx.android.synthetic.main.activity_searchable.*
import android.support.v7.widget.SearchView
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
        setSupportActionBar(searchable_toolbar)
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
        network = (application as App).presenter.Network
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the options menu from XML
        val inflater = menuInflater
        inflater.inflate(R.menu.searchable_menu, menu)
        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search_View)?.actionView as SearchView
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
        val query = intent?.getStringExtra(SearchManager.QUERY)
        searchView.setQuery(query ?: "", false)
        return true
    }

    private fun showlists(){
        search_activity_progress_bar?.visibility = View.GONE
        search_activity_recyclerView?.visibility = View.VISIBLE
    }
    private fun NoSearchResult(){
        search_activity_progress_bar?.visibility = View.GONE
        search_activity_notfound?.visibility = View.VISIBLE
    }

    private fun showError(){
        search_activity_progress_bar?.visibility = View.GONE
        search_activity_error?.visibility = View.GONE
    }
}