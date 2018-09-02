package com.redwasp.cubix.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.redwasp.cubix.App
import com.redwasp.cubix.DaoSession
import com.redwasp.cubix.R
import com.redwasp.cubix.arch.IMaterialRackFragment
import com.redwasp.cubix.utils.RackAdapter
import kotlinx.android.synthetic.main.fragment_material_rack.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

/**
 * A simple [Fragment] subclass.
 *
 */
class MaterialRackFragment : Fragment(), IMaterialRackFragment {
    private lateinit var recyclerView: RecyclerView
    private lateinit var daoSession: DaoSession

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_material_rack, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI()
        getData()
    }

    override fun initUI(){
        recyclerView = material_rack_fragment.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = RackAdapter().apply {
                addCOntrollingFragment(this@MaterialRackFragment)
            }
        }

        daoSession = (activity?.application as App).daoSession
    }
    override fun update() {
        stopProgressBar()
        recyclerView.visibility = View.VISIBLE
        recyclerView.adapter.notifyDataSetChanged()
    }

    private fun getData(){
        if (::daoSession.isInitialized){
            val feeds = daoSession.feedRecordDao
            val deferred = async { feeds.loadAll() }
            launch {
                try {
                    val list = deferred.await()
                    if (list.isEmpty()){
                        withContext(UI){
                            // Notify user visually
                            notifyUserOfEmpty()
                        }
                        return@launch
                    }
                    (recyclerView.adapter as RackAdapter).addData(list)
                    withContext(UI){
                        update()
                    }
                } catch (e : Exception){
                    withContext(UI){
                        //Notify User visually
                        notifyError()
                    }
                }

            }
        }
    }

    override fun stopProgressBar() {
        material_progress_bar?.visibility = View.GONE
    }
    override fun startProgressBar(){
        material_progress_bar?.visibility = View.VISIBLE
    }

    private fun notifyError(){
        material_progress_bar?.visibility = View.GONE
        material_notify_emptty_rack?.visibility = View.GONE
        material_notify_error?.visibility = View.VISIBLE
    }

    private fun notifyUserOfEmpty(){
        material_progress_bar?.visibility = View.GONE
        material_notify_emptty_rack?.visibility = View.VISIBLE
    }
    override fun navToView(frag: Fragment) {
        activity!!.supportFragmentManager.beginTransaction().replace(R.id.container, frag, frag.toString())
                .commit()
    }
    override fun toString(): String = "MaterialRackFragment"
}
