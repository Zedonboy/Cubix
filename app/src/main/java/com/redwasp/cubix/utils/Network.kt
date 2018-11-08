package com.redwasp.cubix.utils

import android.content.Context
import com.algolia.search.saas.Client
import com.algolia.search.saas.Query
import com.redwasp.cubix.R
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.lang.Exception
import java.util.concurrent.TimeUnit

class Network(context : Context?) {
    private val apiService : ApiContract
    init {
        // Check how to get string value from resource
        val okclient = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .build()
        val retrofit = Retrofit.Builder()
                .baseUrl(context?.getString(R.string.cubixFirebase_Url)?:"http://api.beeReader.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okclient)
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
        val list = mutableListOf<Feed>()
        val algoClient = Client("BN3VG93NEM", "595653d9fbd6824c83e920ac5a0140c5")
        val index = algoClient.getIndex("FEED")
        val queryString = Query(query)
        try {
            val jsonObject = index.searchSync(queryString)
            val searchResults = jsonObject.getInt("nbHits")
            if (searchResults > 0){
                val feedsJsonArray = jsonObject.getJSONArray("hits")
                for (i in 0 until feedsJsonArray.length()){
                    val feedItem = feedsJsonArray.getJSONObject(i)
                    val feed = Feed(feedItem.getString("title"),
                            feedItem.getString("searchUrl"),
                            feedItem.getString("imageUrl"),
                            feedItem.getString("description"),
                            feedItem.getString("author"),
                            feedItem.getBoolean("locked"))
                    list.add(feed)
                }
            } else {
                return emptyList()
            }
        } catch (e : Exception){
            throw e
        }
        return list
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

    fun getFeeds(string: String?, interest: List<String>?): List<Feed> {
       val feeds = apiService.getLatestFeeds(string, interest)
        val response = feeds.execute()
        return if (response.isSuccessful and (response.code() == 200)) {
            response.body()?: emptyList()
        } else throw NetworkNotSuccesfulException("Network Call wasn't successful")
    }

    fun getFullText(searchUrl : String) : String{
        val handler = apiService.getFullText(searchUrl)
        val resp = handler.execute()
        return if (resp.isSuccessful and (resp.code() == 200)){
            resp.body()?:""
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

    fun getLatestVersion() : Int?{
        val call = apiService.latestVersion()
        val resp = call.execute()
        return if (resp.isSuccessful){
            resp.body()
        } else {
            null
        }
    }

    fun getUserInterests(disciple : String, interest : List<String>) : List<Feed>{
        val call = apiService.getInterests(disciple, interest)
        val resp = call.execute()
        return if (resp.isSuccessful){
            resp.body() ?: emptyList()
        } else {
            throw NetworkNotSuccesfulException("Connection wasn't successful")
        }
    }
}