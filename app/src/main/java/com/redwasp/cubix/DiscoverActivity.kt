package com.redwasp.cubix

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.redwasp.cubix.arch.IDiscoverActivity
import com.redwasp.cubix.fragments.*
import com.redwasp.cubix.utils.DialogActivityInterface
import com.redwasp.cubix.utils.Feed
import com.redwasp.cubix.utils.LatestVersionDialog
import com.redwasp.cubix.utils.PBAdapter
import kotlinx.android.synthetic.main.activity_discover.*
import kotlinx.coroutines.android.UI
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class DiscoverActivity : AppCompatActivity(), DialogActivityInterface, IDiscoverActivity {
    private val imageCaptureToken = 4
    private val signInToken = 2
    private var imagePath = ""
    private var clicked = 0
    val db = FirebaseFirestore.getInstance()

    var selectTab : Int = 0
    set(value) { activity_feed_btmNav?.selectedItemId = value }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discover)
        setSupportActionBar(toolbar)
        initUI()
        activity_feed_btmNav?.selectedItemId = R.id.home
        checkVersion()
    }
    private fun initUI() {
        toolbar?.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.search_icon -> {
                    onSearchRequested()
                    return@setOnMenuItemClickListener true
                }
                R.id.profile -> {
                    if (clicked == R.id.profile) return@setOnMenuItemClickListener false
                    clicked = R.id.profile
                    navigateToAnotherView(ProfileFragment())
                    return@setOnMenuItemClickListener true
                }
                else -> {
                    return@setOnMenuItemClickListener false
                }
            }
        }

        activity_feed_btmNav?.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    if(clicked == R.id.home) return@setOnNavigationItemSelectedListener true
                    clicked = R.id.home
                    navigateToAnotherView(HomeFragment())
                                    }
                R.id.library -> {
                    if(clicked == R.id.library) return@setOnNavigationItemSelectedListener true
                    clicked = R.id.library
                    navigateToAnotherView(MaterialRackFragment())
                }
                R.id.take_note -> {
                    // call camera app
                    if (clicked == R.id.take_note) return@setOnNavigationItemSelectedListener true
                    clicked = R.id.take_note
                    cameraDialog()
                }
            }

            return@setOnNavigationItemSelectedListener true
        }

    }

    override fun userSignIn() {
        val providers = listOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build(),
                AuthUI.IdpConfig.FacebookBuilder().build()
        )
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.cubix_logo)
                        .build(),
                signInToken
        )
    }
     override fun navigateToAnotherView(data: Fragment) {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,data,data.toString())
                        .addToBackStack(null)
                        .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }
//
    private fun cameraDialog(){
        val dialogBox = DialogBox()
        dialogBox.setCallingActivity(this)
        dialogBox.show(supportFragmentManager, "dialog_box")
    }
//
    private fun callCameraApp(){
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                // Create the File where the photo should go
                val photoFile : File?
                try {
                    photoFile = createImageFile()
                } catch (e : IOException) {
                    return
                }
                // Continue only if the File was successfully create
                    val photoURI = Uri.fromFile(photoFile)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, imageCaptureToken)
                }
            }
    override fun onPositiveBtnClicked() {
        callCameraApp()
    }

    override fun onNegativeBtnClicked() {
        //
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Get the image path and send to Image Upload Fragment
        when(requestCode){
            imageCaptureToken -> {
                if(resultCode == Activity.RESULT_OK){
                    val fragment = ImageUploadFragment().apply {
                        uploadImagePath = imagePath
                    }
                    navigateToAnotherView(fragment)
                }
            }
            signInToken -> {
                // think of what to do here
                if (resultCode == Activity.RESULT_OK){

                } else {
                    makeToast("Error Signing In")
                }
            }
        }
    }
    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        imagePath = image.absolutePath
        return image
    }

    fun shutDownToolBar(){
        toolbar?.visibility = View.GONE
    }

    fun setUpToolBAr(){
        toolbar?.visibility = View.VISIBLE
    }

    override fun userSignOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    // Holy Snap Why!!!!
                    navigateToAnotherView(ProfileFragment())
                }
    }
    override fun makeToast(mssg: String, duration : Int) {
        val toast = Toast.makeText(applicationContext, mssg, duration)
        toast.show()
    }

    private fun checkVersion(){
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val availabeleLatestVersion = sharedPref.getBoolean(getString(R.string.latestVerrsionCheckKey), false)
        if (availabeleLatestVersion){
            val dialog = LatestVersionDialog()
            dialog.show(supportFragmentManager, "dialog_box_latestVersion")
        }
        val network = (application as App).network
        val defered = async { network.getLatestVersion() }
        launch {
            try {
                val latestVersion = defered.await()
                val packageInfo = packageManager.getPackageInfo(packageName, 0)
                val versionInt = packageInfo.longVersionCode
                latestVersion?:return@launch
                if (latestVersion != versionInt.toInt()){
                    // tell user to download it from
                    withContext(UI){
                        val latestDialog = LatestVersionDialog()
                        latestDialog.show(supportFragmentManager, "dialog_box")
                        return@withContext
                    }
                } else {
                    with(sharedPref.edit()){
                        putBoolean(getString(R.string.latestVerrsionCheckKey), false)
                        apply()
                        return@with
                    }
                }
            } catch (e : Exception){

            }
        }
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount == 1){
            finish()
            return
        }
        super.onBackPressed()
    }

    fun getDataBasedOnDiscipline(adapter : PBAdapter<Feed>, onsuccess : ()-> Unit){
        val disciplineKey = getString(R.string.user_discipline)
        val sharedPref = getSharedPreferences(getString(R.string.settings), Context.MODE_PRIVATE)
        val discipline = sharedPref.getString(disciplineKey, "")!!
        if (discipline.isEmpty()) return
        val latestStuff = db.collection("feeds/latest/$discipline")
        val firestoreQuery = latestStuff.orderBy("date")
                .limit(queryLimit.toLong())
        firestoreQuery.get().addOnSuccessListener { querySnapshot ->
            if(querySnapshot.metadata.isFromCache){
                Log.d("isFromCache", "True am from cache")
            }
            if (querySnapshot.documents.isEmpty()) return@addOnSuccessListener
            val mListOfFeeds = mutableListOf<Feed>()
            for (document in querySnapshot.documents){
                val feed = document.toObject(Feed::class.java)
                feed?:break
                mListOfFeeds.add(feed)
            }
            if (mListOfFeeds.isEmpty()) return@addOnSuccessListener
            @Suppress("UNCHECKED_CAST")
            adapter.addData(mListOfFeeds)
            onsuccess.invoke()
            adapter.notifyDataSetChanged()
        }
    }

    fun getDataBasedOnInterests(adapter: PBAdapter<Feed>, onsuccess: () -> Unit){
        val initerestsKey = getString(R.string.user_preferences)
        val sharedPref = getSharedPreferences(getString(R.string.settings), Context.MODE_PRIVATE)
        val interests = sharedPref.getStringSet(initerestsKey, mutableSetOf())!!
        if (interests.isEmpty()) return
        val latestStuff = db.collection("feeds/latest/interests")
        val queryList = mutableListOf<Query>()
        interests.forEach {
            val query = latestStuff.whereArrayContains("tag", it)
            queryList.add(query)
        }
        queryList.forEach { firestoreQuery ->
            firestoreQuery.get().addOnSuccessListener { querySnapshot ->
                if (querySnapshot.documents.isEmpty()) return@addOnSuccessListener
                val mListOfFeeds = mutableListOf<Feed>()
                for (document in querySnapshot.documents){
                    val feed = document.toObject(Feed::class.java)
                    feed?:break
                    mListOfFeeds.add(feed)
                }
                if (mListOfFeeds.isEmpty()) return@addOnSuccessListener
                @Suppress("UNCHECKED_CAST")
                adapter.addData(mListOfFeeds)
                onsuccess.invoke()
                adapter.notifyDataSetChanged()
            }
        }
    }
}
