package com.redwasp.cubix.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.redwasp.cubix.DiscoverActivity
import com.redwasp.cubix.R

/**
 * A simple [Fragment] subclass.
 *
 */
class ProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val activity = (activity as DiscoverActivity)
        activity.shutDownToolBar()
        activity.selectedTab = R.id.profile

        return inflater.inflate(R.layout.fragment_feed_list, container, false)
    }

    override fun toString(): String = "ProfileFragment"

}
