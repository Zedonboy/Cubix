package com.redwasp.cubix.arch

import android.os.Bundle
import android.support.v4.app.Fragment

interface IMaterialRackFragment : IView {
    fun initUI(){

    }

    fun stopProgressBar() {

    }
    fun startProgressBar(){

    }

    fun navToView(frag : Fragment){

    }
}