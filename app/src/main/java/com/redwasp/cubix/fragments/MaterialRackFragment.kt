package com.redwasp.cubix.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.redwasp.cubix.App
import com.redwasp.cubix.DiscoverActivity
import com.redwasp.cubix.R
import com.redwasp.cubix.arch.IAdapter
import com.redwasp.cubix.arch.IMaterialRackFragment
import com.redwasp.cubix.arch.IView
import com.redwasp.cubix.archComponentsModels.MaterialFragmentModel
import com.redwasp.cubix.archComponents_Presenters.MaterialFragmentPresenter
import com.redwasp.cubix.utils.Feed
import com.redwasp.cubix.utils.RackAdapter
import kotlinx.android.synthetic.main.fragment_material_rack.*

/**
 * A simple [Fragment] subclass.
 *
 */
class MaterialRackFragment : Fragment(), IMaterialRackFragment {
    private val presenter = MaterialFragmentPresenter()
    private val model = MaterialFragmentModel()
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as DiscoverActivity).selectedTab = R.id.library
        presenter.init(this, model)
        val global = (activity?.application as App).presenter
        presenter.setGlobalPresenter(global)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_material_rack, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.setUpDatabaseSession()
        presenter.populate()
        initUI()
        @Suppress("UNCHECKED_CAST")
        presenter.setAdapter(recyclerView.adapter as IAdapter<Feed>)
    }

    override fun initUI(){
        recyclerView = material_rack_fragment.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = RackAdapter(presenter)
        }
    }
    override fun update() {
        stopProgressBar()
        recyclerView.visibility = View.VISIBLE
        recyclerView.adapter.notifyDataSetChanged()
    }

    override fun stopProgressBar() {
        material_progress_bar?.visibility = View.GONE

    }
    override fun startProgressBar(){
        material_progress_bar?.visibility = View.VISIBLE
    }

    override fun <T> navigateToAnotherView(data: T) {
        if (data is Fragment){
            fragmentManager?.beginTransaction()?.add(R.id.fragment_container, data, data.toString())
                    ?.commit()
        }
    }

    override fun toString(): String = "MaterialRackFragment"
}
