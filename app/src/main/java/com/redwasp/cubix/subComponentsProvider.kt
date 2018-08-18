package com.redwasp.cubix

import com.redwasp.cubix.arch.IModel
import com.redwasp.cubix.arch.IView
import com.redwasp.cubix.components.ModelModule
import com.redwasp.cubix.components.ViewModule
import dagger.Component

@Component(modules = [ModelModule::class, ViewModule::class])
interface subComponentsProvider {
    fun getView() : IView
    fun getModel() : IModel
}