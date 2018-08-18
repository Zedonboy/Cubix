package com.redwasp.cubix.arch

import com.redwasp.cubix.DaggersubComponentsProvider
import com.redwasp.cubix.components.ModelModule
import com.redwasp.cubix.components.ViewModule
import com.redwasp.cubix.subComponentsProvider

abstract class IPresenter {
    protected lateinit var components : subComponentsProvider
    open fun init(view : IView){
        if (!::components.isInitialized) // throw an exception
        components = DaggersubComponentsProvider.builder()
                .viewModule(ViewModule(view))
                .modelModule(ModelModule(object : IModel {}))
                .build()
    }
    open fun init(view: IView, model : IModel){
        if (!::components.isInitialized) // throw an exception
            components = DaggersubComponentsProvider.builder()
                    .viewModule(ViewModule(view))
                    .modelModule(ModelModule(model))
                    .build()
    }

    open fun init(model: IModel) {
        if (!this::components.isInitialized)// throw an exception
            components = DaggersubComponentsProvider.builder()
                    .modelModule(ModelModule(model))
                    .viewModule(ViewModule(object : IView{

                    }))
                    .build()
    }

    open fun backBtnPresses(){

    }

    open fun setGlobalPresenter(presenter : IPresenter): Unit {

    }

    open fun<T> navigate(data : T){
        throw NotImplementedError("This call is not implemented")
    }
}