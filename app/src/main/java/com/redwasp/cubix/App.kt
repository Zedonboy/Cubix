package com.redwasp.cubix

import android.support.multidex.MultiDexApplication
import com.redwasp.cubix.utils.Network

class App : MultiDexApplication(){

    private lateinit var _daosession : DaoSession
    val daoSession : DaoSession
    get() = this._daosession

    private lateinit var _network : Network
    val network : Network
    get() = this._network
    override fun onCreate() {
        super.onCreate()
        setUpDatabasehelper()
        setUpNetwork()
    }


    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onTerminate() {
        super.onTerminate()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
    }

    private fun setUpDatabasehelper(){
        val helper = DaoMaster.DevOpenHelper(this, "feeds-db")
        val db = helper.writableDb
        this._daosession= DaoMaster(db).newSession()
    }

    private fun setUpNetwork(){
        _network = Network(applicationContext)
    }

}