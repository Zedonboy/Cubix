package com.redwasp.cubix.fragments


import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.redwasp.cubix.App
import com.redwasp.cubix.DiscoverActivity
import com.redwasp.cubix.R
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import java.io.InputStream
import java.net.URL

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
        if(user != null){
            profile_frag_collapseToolBar?.title = user.displayName?: "Profile"
            fragment_profile_sign_out?.visibility = View.VISIBLE
            fragment_profile_sign_out?.setOnClickListener { _ ->
                (activity as DiscoverActivity).userSignOut()
            }
        } else {
            fragment_profile_sign_in?.visibility = View.VISIBLE
            fragment_profile_sign_in?.setOnClickListener { _ ->
                (activity as DiscoverActivity).userSignIn()
            }
        }

        launch {
            user?:return@launch
            val photoUrl = user.photoUrl
            val stream = URL(photoUrl?.toString()).content as InputStream
            val bitmap = BitmapFactory.decodeStream(stream)
            withContext(UI){
                profile_photo?.setImageBitmap(bitmap)
            }
        }
    }
    override fun toString(): String = "ProfileFragment"
}