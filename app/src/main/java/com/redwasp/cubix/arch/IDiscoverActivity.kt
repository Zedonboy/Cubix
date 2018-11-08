package com.redwasp.cubix.arch

import android.support.v4.app.Fragment
import android.widget.Toast

interface IDiscoverActivity {
    fun navigateToAnotherView(data : Fragment){
        throw NotImplementedError("This method must be implemented")
    }

    fun userSignIn(){
        throw NotImplementedError("this method must be implemented")
    }

    fun userSignOut(){
        throw NotImplementedError("This method must be implemented")
    }

    fun makeToast(mssg: String, duration: Int = Toast.LENGTH_SHORT){
        throw NotImplementedError("this method must be implemented")
    }
}