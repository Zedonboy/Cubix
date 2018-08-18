package com.redwasp.cubix.archComponents_Presenters

import com.redwasp.cubix.arch.IAdapter
import com.redwasp.cubix.arch.IPresenter
import com.redwasp.cubix.archComponentsModels.MaterialFragmentModel
import com.redwasp.cubix.fragments.MaterialRackFragment
import com.redwasp.cubix.utils.Feed
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class MaterialFragmentPresenter : IPresenter() {
    private lateinit var globalPresenter: IPresenter
    private lateinit var adapter: IAdapter<Feed>
    fun populate() {
        val result = async(CommonPool) { (components.getModel() as MaterialFragmentModel).getDataList<Feed>() }
        launch (CommonPool) {
            val list = result.await()
            adapter.addData(list)
            withContext(UI){
                (components.getView() as MaterialRackFragment).update()
            }
        }
    }

    override fun setGlobalPresenter(presenter: IPresenter) {
        this.globalPresenter = presenter
    }

    fun setUpDatabaseSession(){
        (components.getModel() as MaterialFragmentModel).setUpDaoSession((globalPresenter as ApplicationPresenter).DaoSession)
    }

    fun setAdapter(adapter : IAdapter<Feed>) {
        this.adapter = adapter
    }

    override fun <T> navigate(data: T) {
        components.getView()
    }
}