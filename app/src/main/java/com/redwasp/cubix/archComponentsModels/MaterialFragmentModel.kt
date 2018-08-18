package com.redwasp.cubix.archComponentsModels

import com.redwasp.cubix.DaoSession
import com.redwasp.cubix.arch.IModel

class MaterialFragmentModel : IModel {
    private lateinit var daoSession: DaoSession

    fun setUpDaoSession(daosession : DaoSession){
        this.daoSession = daosession
    }

    override fun <T> getDataList(): Collection<T> {
        val record = daoSession.feedRecordDao
        @Suppress("UNCHECKED_CAST")
        return record.loadAll() as Collection<T>? ?: emptyList()
    }
}