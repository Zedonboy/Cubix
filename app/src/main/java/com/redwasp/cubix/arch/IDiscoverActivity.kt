package com.redwasp.cubix.arch

import android.support.v4.app.Fragment

interface IDiscoverActivity {
    fun navigateToAnotherView(data : Fragment){
        throw NotImplementedError("This method must be implemented")
    }
    fun makeToast(mssg : String){
        throw NotImplementedError("This method must be implemented by subclass")
    }
}