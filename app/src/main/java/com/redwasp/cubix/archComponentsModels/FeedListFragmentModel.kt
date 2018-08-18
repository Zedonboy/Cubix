package com.redwasp.cubix.archComponentsModels

import com.redwasp.cubix.arch.IModel
import com.redwasp.cubix.utils.Feed
import com.redwasp.cubix.utils.Network
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class FeedListFragmentModel(network: Network) : IModel {
    private var _network : Network = network

    override fun <T> getDataList(callbackOnSuccess: (Collection<T>) -> Unit, callbackOnFailure: () -> Unit) {
        val result = async(CommonPool){_network.getFeeds()}
        launch(CommonPool){
            try {
                val list = result.await()
                @Suppress("UNCHECKED_CAST")
                callbackOnSuccess(list as Collection<T>)
            } catch (e : Exception){
                callbackOnFailure()
            }
        }
    }

    fun likeFeed(username : String, nameOfFeed : String) {
        launch(CommonPool) {
            _network.likeArticle(username, nameOfFeed)
        }
    }

}