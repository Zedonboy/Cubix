package com.redwasp.cubix.arch

interface IView{
    fun <T> navigateToAnotherView (data: T): Unit {
        throw NotImplementedError("This call is not implemented")
    }
    fun update() {
        throw NotImplementedError("This call is not implemented")
    }
    fun startWaitingAnimation(){
        throw NotImplementedError("This call is not implemented")
    }
    fun stopWaitingAnimation(){
        throw NotImplementedError("This call is not implemented")
    }
    fun goBackToPreviousView(){
        throw NotImplementedError("This call is not implemented")
    }
    fun onBackBtnPressed(){
        throw NotImplementedError("This call is not implemented")
    }
}