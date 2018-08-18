package com.redwasp.cubix.arch

interface IFeedListFragment : IView{
    private fun showDialog() {

    }

    private fun init(){

    }

    fun showRecyclerView(){

    }

    fun hideProgressBar(){

    }

    fun showError(){

    }

    fun onRefresh() {
        // when the user refresh
        // hide the list
        // show progressbar
        // if get data from net, clear the current data.
        // add data to the adapte... (Make sure you Notify)
        // else hide progressBar, show recyclerView
        // render old data

    }

    fun loadMoreError(){

    }

}