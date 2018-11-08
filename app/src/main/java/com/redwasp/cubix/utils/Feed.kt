package com.redwasp.cubix.utils

data class Feed(
        var title : String,
        var searchUrl : String, var imageUrl : String? = null, var description : String, var author : String,
                var locked : Boolean = false, var imagebase64 : String? = null){
    constructor() : this("","",null,"","")
}