package com.redwasp.cubix.archComponents_Presenters

import com.redwasp.cubix.arch.IPresenter
import com.redwasp.cubix.utils.Network
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class ImageUploadPresenter : IPresenter() {
    private lateinit var _network : Network
    fun upload(filepath : String): Unit {
        if (::_network.isInitialized){
            launch {
                try {
                    _network.uploadNoteImage(filepath)
                }catch (e : Exception){
                    withContext(UI){

                    }
                }

            }
        }
    }

    fun setNetwork(networkRef : Network){
        _network = networkRef
    }
}