package com.redwasp.cubix.utils

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiContract {
    @GET("users/like/{username}/{feedTitle}")
    fun likeFeedByUser(@Path("username")name: String, @Path("feedTitle")feedTitle:String) : Call<Unit>

    @GET(value = "{searchUrl}/?getBody=true")
    fun getFullText(@Path("searchUrl") url: String): Call<String>

    @GET("users/{username}")
    fun getFeedsSavedByUser(@Path("username") name: String): Call<List<Feed>>
    // Save an article to user account
    @GET("users/save/{username}/{feedTitle}")
    fun saveArticle(@Path("username") name : String, @Path("feedTitle")feedTitle : String) : Call<Unit>

    @GET("users/search/article")
    fun searchForArticle(@Query("searchWord")query : String) : Call<List<Feed>>

    @GET("users/remove/{username}/{feedTitle}")
    fun removeArticle(@Path("username") name : String, @Path("feedTitle")feedTitle : String)

    @Multipart
    @POST("users/upload/image")
    fun uploadNoteImage(@Part image : MultipartBody.Part,@Path("name") body : RequestBody) : Call<Unit>

    // Register User
    @POST("users/new")
    fun registerUser(@Body data : RegistrationContract) : Call<User>

    //LoginUser
    @POST("users/login")
    fun loginUser(@Body data: RegistrationContract) : Call<User>

    //Remember User Session
    @POST("users/remember")
    fun rememberUser(@Body sessionId : String) : Call<User>

    // This section of the contract gets Test Question is for the fucking User
    @GET("feeds/test")
    fun getTests() : Call<List<Test>>

    // It Gets Materials from the database
    @GET("feeds/material")
    fun getMaterial() : Call<List<Feed>>

    // gets Article from Third party websites
    @GET("feeds/articles")
    fun getArticle () : Call<List<Feed>>
}