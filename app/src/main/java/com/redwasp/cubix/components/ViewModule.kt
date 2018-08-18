package com.redwasp.cubix.components

import com.redwasp.cubix.arch.IView
import dagger.Module
import dagger.Provides

@Module
class ViewModule(private val view : IView) {
    @Provides
    fun providesView() : IView{
        return view
    }
}