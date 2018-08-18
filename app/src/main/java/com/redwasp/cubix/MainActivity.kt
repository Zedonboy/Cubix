package com.redwasp.cubix

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.redwasp.cubix.arch.IView
import com.redwasp.cubix.archComponents_Presenters.MainActivityPresenter
import com.redwasp.cubix.fragments.ProfileNotify
import com.redwasp.cubix.fragments.WelcomeScreenFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IView {
    private val presenter = MainActivityPresenter()
    private val pagerAdapter = SlideFragmentAdapter(supportFragmentManager)
    private val NUM_OF_PAGES = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.init(this)
        main_activity_viewpager.adapter = pagerAdapter
    }

    override fun <T> navigateToAnotherView(data: T) {

    }

    private inner class SlideFragmentAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager){
        override fun getItem(position: Int): Fragment = when(position){
            0 -> WelcomeScreenFragment()
            1 -> ProfileNotify()
            else -> WelcomeScreenFragment()
        }


        override fun getCount(): Int {
            return NUM_OF_PAGES
        }

    }

    override fun onBackBtnPressed() {

    }

}
