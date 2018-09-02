package com.redwasp.cubix.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.redwasp.cubix.App
import com.redwasp.cubix.R
import com.redwasp.cubix.utils.Network
import kotlinx.android.synthetic.main.fragment_reading.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReadingFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ReadingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var title: String? = null
    private var searchURL: String? = null
    private var content : String? = null
    private lateinit var network: Network

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_PARAM1)
            searchURL = it.getString(ARG_PARAM2)
            content = it.getString(ARG_PARAM3)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_reading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    private fun initUI(){
        reading_fragment_toolbar?.title = title
        reading_fragment_title?.text = title
        network = (activity!!.application as App).network
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param title Parameter 1.
         * @param searchURL Parameter 2.
         * @return A new instance of fragment ReadingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(title: String?, searchURL: String?, content: String?) =
                ReadingFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, title)
                        putString(ARG_PARAM2, searchURL)
                        putString(ARG_PARAM3, content)
                    }
                }
    }
}
