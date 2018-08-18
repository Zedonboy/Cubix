package com.redwasp.cubix.archComponentsModels

import android.content.Context
import com.google.gson.Gson
import com.redwasp.cubix.R
import com.redwasp.cubix.arch.IModel
import com.redwasp.cubix.utils.User

class AppModel : IModel{
    private lateinit var context: Context

    override fun setContext(context: Context?) {
        this.context = context ?: return
    }

    override fun <T> getData(): T? {
        if (!::context.isInitialized) return null
        val gson = Gson()
        val key = context.getString(R.string.userKey)
        val sharedPrefKey = context.getString(R.string.shared_pref_key)
        val sharedPref = context.getSharedPreferences(sharedPrefKey, Context.MODE_PRIVATE)
        val userStringified = sharedPref.getString(key, "null")
        @Suppress("")
        return gson.fromJson(userStringified, User::class.java) as T
    }
}