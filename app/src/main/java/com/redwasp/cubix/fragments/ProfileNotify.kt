package com.redwasp.cubix.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.redwasp.cubix.R
import com.redwasp.cubix.arch.IView
/**
 * A simple [Fragment] subclass.
 *
 */
class ProfileNotify : Fragment(), IView {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_notify, container, false)
    }

    override fun <T> navigateToAnotherView(data: T) {
        //val intent = Intent(context!!, )
    }
}
