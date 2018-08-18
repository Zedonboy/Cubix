package com.redwasp.cubix.components

import com.redwasp.cubix.arch.IModel
import dagger.Module
import dagger.Provides

@Module
class ModelModule(private val model : IModel) {

    @Provides
    fun providesModel() : IModel{
        return model
    }
}