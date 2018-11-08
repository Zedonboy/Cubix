package com.redwasp.cubix.fragments


import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.redwasp.cubix.App
import com.redwasp.cubix.DiscoverActivity
import com.redwasp.cubix.R
import com.redwasp.cubix.utils.Feed
import com.redwasp.cubix.utils.PBAdapter
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.android.UI
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        init()
        val user = FirebaseAuth.getInstance().currentUser
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
        @Suppress("UNCHECKED_CAST")
        (activity as DiscoverActivity).getDataBasedOnDiscipline( fragment_profile_recyclerView?.adapter as PBAdapter<Feed>, ::showList)

        @Suppress("UNCHECKED_CAST")
        (activity as DiscoverActivity).getDataBasedOnInterests( fragment_profile_recyclerView?.adapter as PBAdapter<Feed>, ::showList)
        launch {
            activity?:return@launch
            user?:return@launch
            try {
                val photoUrl = user.photoUrl?:return@launch
                val stream = URL(photoUrl.toString()).content as InputStream
                val bitmap = BitmapFactory.decodeStream(stream)
                FirebaseAuth.getInstance().currentUser?:return@launch
                withContext(UI){
                    profile_photo?.setImageBitmap(bitmap)
                }
            } catch (e : Exception){
                // dont do shit
            }

        }
    }

    private fun init(){
        fragment_profile_recyclerView?.apply {
            adapter = PBAdapter<Feed>().apply {
                setActivity = activity as DiscoverActivity
                setDaoSession = (activity?.application as App).daoSession
                Network = (activity?.application as App).network
            }
            layoutManager = LinearLayoutManager(context)
        }
    }
    override fun toString(): String = "ProfileFragment"

    private fun showList(){
        fragment_profile_recyclerView?.visibility = View.VISIBLE
    }
}