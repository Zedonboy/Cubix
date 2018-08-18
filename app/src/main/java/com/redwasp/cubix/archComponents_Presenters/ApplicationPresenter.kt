package com.redwasp.cubix.archComponents_Presenters

import com.redwasp.cubix.App
import com.redwasp.cubix.DaoMaster
import com.redwasp.cubix.DaoSession
import com.redwasp.cubix.arch.IGlobalApp
import com.redwasp.cubix.arch.IPresenter
import com.redwasp.cubix.archComponentsModels.AppModel
import com.redwasp.cubix.utils.Network
import com.redwasp.cubix.utils.User
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class ApplicationPresenter : IPresenter(){
    private var user: User? = null
    private lateinit var app: IGlobalApp
    private lateinit var network: Network
    val Network : Network
    get(){
        if (::network.isInitialized){
            return this.network
        } else {
            throw NotImplementedError("Network Object is not Initialized")
        }
    }
    private lateinit var daoSession: DaoSession
    val DaoSession
    get() = this.daoSession

    fun initGLobalApp(app: IGlobalApp): Unit {
        this.app = app
    }

    fun setUpDatabasehelper(){
        val helper = DaoMaster.DevOpenHelper(app as App, "feeds-db")
        val db = helper.writableDb
        this.daoSession = DaoMaster(db).newSession()
    }

    fun getUser() : User?{
        return this.user
    }

    fun retrieveUser(){
        val result = async(CommonPool){(components.getModel() as AppModel).getData<User>()}
        launch(CommonPool) {
            this@ApplicationPresenter.user = result.await()
        }

    }

    fun setNetworkObject(network: Network): Unit {
        this.network = network
    }
}