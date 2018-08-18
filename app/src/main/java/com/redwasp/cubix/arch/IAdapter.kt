package com.redwasp.cubix.arch

interface IAdapter<T> {
    fun addData(data : Collection<T>)
    fun clearData(){}
}