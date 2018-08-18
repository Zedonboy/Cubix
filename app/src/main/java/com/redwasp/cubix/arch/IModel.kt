package com.redwasp.cubix.arch

import android.content.Context

interface IModel{
    fun<T> getData() : T?{
        throw NotImplementedError("This call is not implemented")
    }
    fun<T, R> getData(data : R) : T?{
        throw NotImplementedError("This call is not implemented")
    }
    fun<T> getDataList(callbackOnSuccess : (Collection<T>)-> Unit, callbackOnFailure: () -> Unit){
        throw NotImplementedError("This call is not implemented")
    }
    fun<T> getDataList() : Collection<T>{
        throw NotImplementedError("This call is not implemented")
    }
    fun<T> saveData(data: T){
        throw NotImplementedError("This call is not implemented")
    }
    fun<T> saveData(collection: Collection<T>){
        throw NotImplementedError("This call is not implemented")
    }
    fun updateData(){
        throw NotImplementedError("This call is not implemented")
    }

    fun setContext(context : Context?){
        throw NotImplementedError("THis call is not implemented")
    }
}