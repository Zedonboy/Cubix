package com.redwasp.cubix.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.redwasp.cubix.App
import kotlinx.android.synthetic.main.fragment_reading.*
import com.redwasp.cubix.R
import com.redwasp.cubix.arch.IView
import com.redwasp.cubix.archComponentsModels.ReadingFragmentModel
import com.redwasp.cubix.archComponents_Presenters.ReadingFragmentPresenter

class ReadingFragment : Fragment(), IView {

    private val presenter = ReadingFragmentPresenter()
    private val model = ReadingFragmentModel()
    private var searchString = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        model.setDatabase((activity?.application as App).presenter.DaoSession)
        model.setContext(context)
        model.setNetwork((activity?.application as App).presenter.Network)
        presenter.init(this, model)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // search string at database first before the network
        presenter.getData(this.searchString){
            // Work on Image Loaders please on the next Version
            reading_fragment_title?.text = it.title
            reading_fragment_body?.text = it.body
        }
    }
    fun setSearchUrl(url: String?): Fragment {
        this.searchString = url ?: ""
        return this
    }
}
