package com.redwasp.cubix.utils

import android.content.Context
import com.redwasp.cubix.utils.ApiContract
import com.redwasp.cubix.R
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class Network(context : Context?) {
    private val apiService : ApiContract
    init {
        // Check how to get string value from resource
        val retrofit = Retrofit.Builder()
                .baseUrl(context?.getString(R.string.apiUrl)?:"http://api.beeReader.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        apiService = retrofit.create(ApiContract::class.java)
    }

    fun uploadNoteImage(filepath : String){
        val imageFile = File(filepath)
        val reqFile = RequestBody.create(MediaType.parse("image/*"), imageFile)
        val body = MultipartBody.Part.createFormData("upload", imageFile.name, reqFile)
        val name = RequestBody.create(MediaType.parse("text/plain"), "image_upload")
        val handle =  apiService.uploadNoteImage(body, name)
        val resp = handle.execute()
        if(resp.isSuccessful){

        } else throw NetworkNotSuccesfulException("Image Could not be uploaded")
    }
    fun likeArticle(username: String, feedTitle:String){
        val handle = apiService.likeFeedByUser(username, feedTitle)
        val resp = handle.execute()
        if(!resp.isSuccessful) throw NetworkNotSuccesfulException("Network Call wasn't successful")
    }

    fun searchForFeed(query : String) : List<Feed>{
        val handle = apiService.searchForArticle(query)
        val resp = handle.execute()
        if (resp.code() == 404) {
            throw FeedNotFoundException("No search result found")
        }
        if (resp.isSuccessful){
            return resp.body() ?: emptyList()
        }
        else {
            throw NetworkNotSuccesfulException("Network Call wasn't successful")
        }
    }

    fun getArticlesSavedByUser(username : String) : List<Feed>{
        val handle = apiService.getFeedsSavedByUser(username)
        val response = handle.execute()
        return if(response.isSuccessful) {
            response.body()?: emptyList()
        } else {
            throw NetworkNotSuccesfulException("Network Call wasn't successful")
        }
    }

    fun getFeeds(): List<Feed> {
       val feeds = apiService.getArticle()
        val response = feeds.execute()
        return if (response.isSuccessful and (response.code() == 200)) {
            response.body()?: emptyList()
        } else throw NetworkNotSuccesfulException("Network Call wasn't successful")
    }

    fun getFullText(searchUrl : String) : Feed?{
        val handler = apiService.getFullText(searchUrl)
        val resp = handler.execute()
        return if (resp.isSuccessful and (resp.code() == 200)){
            resp.body()
        } else throw NetworkNotSuccesfulException("Network call wasn't successful")
    }

    // Takes care of registration of user
    fun registerUser(registerContract : RegistrationContract) : User? {
        val userReg = apiService.registerUser(registerContract)
        val resp = userReg.execute()
        return if(resp.isSuccessful){
            resp.body()
        } else throw NetworkNotSuccesfulException("Network Call wasn't successful")
    }

    //Takes care of saving a material
    fun saveArticleToUser(username: String, articleName : String, callback: suspend () -> Unit){
        val call = apiService.saveArticle(username, articleName)
        val resp = call.execute()
        if (!resp.isSuccessful)throw NetworkNotSuccesfulException("Could not Connect To Servers")
    }

    @FeatureNotImplemented("This will not be implemented because of development time","2.0")
    fun getTests() : List<Test>? {
        val call = apiService.getTests()
        val resp = call.execute()
        return if (resp.isSuccessful) {
            resp.body()
        } else {
            emptyList()
        }
    }
}