package com.redwasp.cubix.archComponents_Presenters

import com.redwasp.cubix.arch.*
import com.redwasp.cubix.archComponentsModels.FeedListFragmentModel
import com.redwasp.cubix.fragments.FeedListFragment
import com.redwasp.cubix.utils.Feed
import com.redwasp.cubix.utils.User
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.runBlocking

class FeedListPresenter : IPresenter() {
    // remember this is still recyclerView Adapter
    private lateinit var adapter : IAdapter<Feed>
    private var globalPresenter : IPresenter? = null
    private var fetchingData = true
    private lateinit var view : FeedListFragment
    private var user: User? = null

    override fun init(view: IView, model: IModel) {
        super.init(view, model)
        this.view = components.getView() as FeedListFragment
    }
    fun setAdapter(adapter: IAdapter<Feed>){
        this.adapter = adapter
    }

    fun getFeeds(){
        if (fetchingData) return
        (components.getModel() as FeedListFragmentModel).getDataList(this::handleSuccess,
                this::handleError)
    }
    private fun handleSuccess(collection: Collection<Feed>) = runBlocking(UI){
        adapter.clearData()
        adapter.addData(collection)
        view.hideProgressBar()
        view.showRecyclerView()
        view.update()
    }

    private fun handleError() = runBlocking(UI){
        view.hideProgressBar()
        view.showError()
    }

    fun likeBtnClicked() {
        // work on it
    }
    fun saveBtnClicked(){
        // work on it
    }

    override fun setGlobalPresenter(presenter: IPresenter) {
        this.globalPresenter = presenter
    }

    fun loadMore(){
        if (fetchingData) return
        (components.getModel() as FeedListFragmentModel).getDataList(this::handleSuccess2,
                this::handleError2)
    }
    private fun handleError2(){
        view.loadMoreError()
    }

    // Was commented Out because the unction is under Construction
    private fun handleSuccess2(collection: Collection<Feed>): Unit {
        if (::adapter.isInitialized){
            adapter.addData(collection)
            view.update()
        }
    }

}