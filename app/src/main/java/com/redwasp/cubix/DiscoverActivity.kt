package com.redwasp.cubix

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.View
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

class DiscoverActivity : AppCompatActivity(), DialogActivityInterface {
    private val imageCaptureToken = 4
    private var imagePath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discover)
        setSupportActionBar(toolbar)
        initUI()
        navigateToAnotherView(HomeFragment())
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
                R.id.profile -> {
                    navigateToAnotherView(ProfileFragment())
                }
                R.id.home -> {
                    navigateToAnotherView(HomeFragment())
                                    }
                R.id.library -> {
                    navigateToAnotherView(MaterialRackFragment())
                }
                R.id.take_note -> {
                    // call camera app
                    cameraDialog()
                }
            }

            return@setOnNavigationItemSelectedListener true
        }

    }

    private fun navigateToAnotherView(data: Fragment) {
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
        //
        val fragment = ImageUploadFragment.newInstance(null, imagePath)
        navigateToAnotherView(fragment)
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
}
