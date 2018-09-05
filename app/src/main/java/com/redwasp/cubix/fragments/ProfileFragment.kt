package com.redwasp.cubix.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.redwasp.cubix.App
import com.redwasp.cubix.DiscoverActivity
import com.redwasp.cubix.R
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * A simple [Fragment] subclass.
 *
 */
class ProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as DiscoverActivity).shutDownToolBar()
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = ((activity as DiscoverActivity).application as App).CurrentUser
        profile_frag_collapseToolBar?.title = user?.displayName
    }
    override fun toString(): String = "ProfileFragment"

}
