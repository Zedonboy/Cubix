package com.redwasp.cubix.archComponents_Presenters

import com.redwasp.cubix.arch.IPresenter
import com.redwasp.cubix.archComponentsModels.ReadingFragmentModel
import com.redwasp.cubix.utils.Feed
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class ReadingFragmentPresenter : IPresenter() {
    fun getData(searchUrl : String, callback : (feed : Feed) -> Unit){
        val deferred = async(CommonPool) {
            (components.getModel() as ReadingFragmentModel).getData<Feed,String>(searchUrl)
        }
        launch(CommonPool) {
            val result = deferred.await()
            if (result != null){
                withContext(UI){
                    callback(result)
                }
            }
        }
    }
}