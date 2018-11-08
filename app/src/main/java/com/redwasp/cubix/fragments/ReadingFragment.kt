package com.redwasp.cubix.fragments

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.redwasp.cubix.App
import com.redwasp.cubix.R
import com.redwasp.cubix.utils.Network
import kotlinx.android.synthetic.main.fragment_reading.*
import kotlinx.coroutines.android.UI
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
/**
 * A simple [Fragment] subclass.
 * Use the [ReadingFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ReadingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    var title: String? = null
    var searchURL: String? = null
    var content : String? = null
    var parcelableString : String? = null
    private lateinit var network: Network
    private lateinit var state : Parcelable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_reading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI()
    }

    private fun initUI(){
        reading_fragment_toolbar?.title = title
        reading_fragment_title?.text = title
        network = (activity!!.application as App).network
        reading_fragment_tryAgain?.setOnClickListener { _ ->
            reading_fragment_body?.visibility = View.GONE
            reading_fragment_errorVIew?.visibility = View.GONE
            reading_fragment_progressbar?.visibility = View.VISIBLE
            getFullContent()
        }
        showFullContent()
        if(parcelableString != null){
            val gson = Gson()
            state = gson.fromJson(parcelableString, Parcelable::class.java)
            fragment_reading_nestedScrollview?.state = state
        }
    }

    private fun getFullContent(){
        if (searchURL == null) return
        val deferred = async { network.getFullText(searchURL!!) }
        launch {
            try {
                content = deferred.await()
                withContext(UI){
                    showFullContent()
                }
            } catch (e : Exception){
                withContext(UI){
                    showError()
                }
            }
        }
    }

    private fun showFullContent(){
        reading_fragment_errorVIew?.visibility = View.GONE
        reading_fragment_progressbar?.visibility = View.GONE
        reading_fragment_body?.visibility = View.VISIBLE
        reading_fragment_body?.text = content
    }

    private fun showError(){
        reading_fragment_body?.visibility = View.GONE
        reading_fragment_progressbar?.visibility = View.GONE
        reading_fragment_errorVIew?.visibility = View.VISIBLE
    }
}
