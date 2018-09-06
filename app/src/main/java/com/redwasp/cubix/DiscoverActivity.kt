package com.redwasp.cubix

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.View
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.redwasp.cubix.arch.IDiscoverActivity
import com.redwasp.cubix.fragments.DialogBox
import com.redwasp.cubix.fragments.HomeFragment
import com.redwasp.cubix.fragments.MaterialRackFragment
import com.redwasp.cubix.fragments.ProfileFragment
import com.redwasp.cubix.utils.DialogActivityInterface
import kotlinx.android.synthetic.main.activity_discover.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class DiscoverActivity : AppCompatActivity(), DialogActivityInterface, IDiscoverActivity {
    private val imageCaptureToken = 4
    private val signInToken = 2
    private var imagePath = ""
    private var clicked = 0

    var SelectTab : Int = 0
    set(value) { activity_feed_btmNav?.selectedItemId = value }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discover)
        setSupportActionBar(toolbar)
        initUI()
        activity_feed_btmNav?.selectedItemId = R.id.home
    }
    private fun initUI() {
        toolbar?.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.search_icon -> {
                    onSearchRequested()
                    return@setOnMenuItemClickListener true
                }
                R.id.profile -> {
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
                        .build(),
                signInToken
        )
    }
     override fun navigateToAnotherView(data: Fragment) {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,data,data.toString())
                        .addToBackStack(null)
                        .commit()
    }
//
//
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }
//
    private fun cameraDialog(){
        val dialogBox = DialogBox()
        dialogBox.setCallingActivity(this)
        dialogBox.show(fragmentManager, "dialog_box")
    }
//
    private fun callCameraApp(){
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
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
                val fragment = ImageUploadFragment().apply {
                    fullImagePath = imagePath
                }
                navigateToAnotherView(fragment)
            }
            signInToken -> {
                // think of what to do here
                if (resultCode == Activity.RESULT_OK){
                    val auth = FirebaseAuth.getInstance()
                    val app = application as App
                    app.CurrentUser = auth.currentUser
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
                    // Holy Snap WHy!!!!
                    (application as App).CurrentUser = null
                    navigateToAnotherView(ProfileFragment())
                }
    }
    override fun makeToast(mssg: String) {
        val toast = Toast.makeText(applicationContext, mssg, Toast.LENGTH_LONG)
        toast.show()
    }
}
