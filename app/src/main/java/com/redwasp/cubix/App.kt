package com.redwasp.cubix

import android.app.Application
import android.support.multidex.MultiDexApplication
import com.redwasp.cubix.arch.IGlobalApp
import com.redwasp.cubix.archComponentsModels.AppModel
import com.redwasp.cubix.archComponents_Presenters.ApplicationPresenter
import com.redwasp.cubix.utils.Network

class App : MultiDexApplication(), IGlobalApp {
    private val _presenter = ApplicationPresenter()
    private val model = AppModel()
    val presenter : ApplicationPresenter
    get() = this._presenter

    override fun onCreate() {
        super.onCreate()
        model.setContext(applicationContext)
        _presenter.init(model)
        _presenter.initGLobalApp(this)
        _presenter.setUpDatabasehelper()
        _presenter.setNetworkObject(Network(baseContext))
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

    override fun addToListeners() {

    }
}