package com.redwasp.cubix

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.SearchManager
import android.content.Context
import android.view.Menu
import com.redwasp.cubix.archComponents_Presenters.SearchableActivityPresenter
import kotlinx.android.synthetic.main.activity_searchable.*
import android.support.v7.widget.SearchView
import com.redwasp.cubix.archComponentsModels.SearchableActivityModel

class SearchableActivity : AppCompatActivity() {
    private val presenter = SearchableActivityPresenter()
    private val model = SearchableActivityModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //presenter.init()
        setContentView(R.layout.activity_searchable)
        handle(intent)
    }

    private fun handle(intent: Intent){
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            presenter.search(query)
        }
    }

    private fun initUI(){

        setSupportActionBar(searchable_toolbar)

        search_activity_recyclerView?.adapter
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
}