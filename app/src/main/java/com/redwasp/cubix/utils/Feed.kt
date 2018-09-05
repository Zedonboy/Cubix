package com.redwasp.cubix.utils

data class Feed(val title : String, val contentUrl : String , val imageUrl : String? = null, var body: String? = null, val author : String,
                val locked : Boolean)