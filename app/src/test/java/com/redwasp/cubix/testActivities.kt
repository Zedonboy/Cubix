package com.redwasp.cubix

import android.support.design.internal.BottomNavigationItemView
import android.support.design.widget.BottomNavigationView
import android.widget.ProgressBar
import com.redwasp.cubix.fragments.*
import junit.framework.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment

@RunWith(RobolectricTestRunner::class)
class testActivities {

    @Test
    fun startingActivity(){
        val discoverActivity = Robolectric.setupActivity(DiscoverActivity::class.java)
        assertNotNull("discover Activity have issues", discoverActivity)
        }
    @Test
    fun testingFragments(){
        val frag = HomeFragment()
        val materialFrag = MaterialRackFragment()
        val profFrag = ProfileFragment()
        val readingFragment = ReadingFragment()
        val profileNotify = ProfileFragment()
        startFragment(frag)
        startFragment(materialFrag)
        startFragment(profFrag)
        startFragment(readingFragment)
        startFragment(profileNotify)
        assertNotNull("FeedList Fragment is null",frag)
        assertNotNull("Material Fragment is null", materialFrag)
        assertNotNull("Profile Fragment is null", profFrag)
        assertNotNull("Reading Fragment is null", readingFragment)
        assertNotNull("Profile Notify is null", profileNotify)

    }

    @Test
    fun testingView(){
//        startFragment(frag)
//        val fragView = frag.view
//        val progressBar = fragView?.findViewById<ProgressBar>(R.id.fragment_feed_list_progressbar)
//        assertTrue("View is not progressBar",progressBar is ProgressBar)
//        assertTrue("ProgressBar is not visible", progressBar?.isVisible!!)
    }

    @Test
    fun testingDiscoverActivity(){
        val discoverAct = Robolectric.setupActivity(DiscoverActivity::class.java)
        val App = discoverAct.application
        val bottomNav = discoverAct.findViewById<BottomNavigationView>(R.id.activity_feed_btmNav)
        val profileButton = bottomNav.findViewById<BottomNavigationItemView>(R.id.profile)
        val materialbtn = bottomNav.findViewById<BottomNavigationItemView>(R.id.library)
        val snapshot = bottomNav.findViewById<BottomNavigationItemView>(R.id.take_note)
        // simulating clicking action
        profileButton.performClick()
        var currentFrag = discoverAct.supportFragmentManager.findFragmentByTag("ProfileFragment")
        assertTrue("Profile Does not showup",(currentFrag != null && currentFrag.isVisible && currentFrag is ProfileFragment))

        materialbtn.performClick()
        currentFrag = discoverAct.supportFragmentManager.findFragmentByTag("MaterialRackFragment")
        assertTrue("Material Fragment does not show up", (currentFrag != null && currentFrag.isVisible && currentFrag is MaterialRackFragment))

    }

}