package com.redwasp.cubix.archComponentsModels

import android.content.Context
import com.redwasp.cubix.DaoSession
import com.redwasp.cubix.FeedRecordDao
import com.redwasp.cubix.arch.IModel
import com.redwasp.cubix.utils.Feed
import com.redwasp.cubix.utils.Network

class ReadingFragmentModel : IModel {
    private lateinit var daoSession: DaoSession
    private var context: Context? = null
    private lateinit var network: Network
    override fun <T,R> getData(data : R): T? {
        if(data !is String) throw Exception("Empty String was passed as database query")
        val record = daoSession.feedRecordDao
        val result = record.queryBuilder()
                .where(FeedRecordDao.Properties.SearchUrl.eq(data))
                .limit(1)
                .list()
        if (result.isEmpty()){
            // if the result is empty, means no shit is there
            val netResult = network.getFullText(data)
            @Suppress("UNCHECKED_CAST")
            return netResult as T?
        }
        val tmp = result[0]
        @Suppress("UNCHECKED_CAST")
        return Feed(tmp.title, tmp.searchUrl, null, tmp.body) as T
    }

    fun setDatabase(daosession: DaoSession): Unit {
        this.daoSession = daosession
    }

    override fun setContext(context: Context?) {
        this.context = context
        this.network = Network(context)
    }

    fun setNetwork(network : Network){
        this.network = network
    }
}