package com.redwasp.cubix

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.view.Menu
import android.view.View
import com.redwasp.cubix.arch.IView
import com.redwasp.cubix.archComponents_Presenters.DiscoverActivityPresenter
import com.redwasp.cubix.fragments.DialogBox
import com.redwasp.cubix.fragments.FeedListFragment
import com.redwasp.cubix.fragments.MaterialRackFragment
import com.redwasp.cubix.fragments.ProfileFragment
import com.redwasp.cubix.utils.DialogActivityInterface
import kotlinx.android.synthetic.main.activity_discover.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class DiscoverActivity : AppCompatActivity(), IView, DialogActivityInterface {
    private val presenter = DiscoverActivityPresenter()
    private val fragmentManager = supportFragmentManager
    private val imageCaptureToken = 4
    private var imagePath = ""
    var selectedTab : Int = 0
    set(value) {
        activity_feed_btmNav?.selectedItemId = value
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.init(this)
        setContentView(R.layout.activity_discover)
        setSupportActionBar(toolbar)
        initUI()
        presenter.navigate(FeedListFragment())
    }

    private fun initUI() {
        toolbar?.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.search_icon -> {
                    onSearchRequested()
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
                    presenter.navigate(ProfileFragment())
                }
                R.id.home -> {
                    presenter.navigate(FeedListFragment())
                }
                R.id.library -> {
                    presenter.navigate(MaterialRackFragment())
                }
                R.id.take_note -> {
                    presenter.showDialog()
                }
            }

            return@setOnNavigationItemSelectedListener true
        }

    }

    override fun update(){

    }

    override fun <T> navigateToAnotherView(data: T) {
        if(data is Fragment)
                fragmentManager.beginTransaction().replace(R.id.fragment_container,data,data.toString())
                        .addToBackStack(null)
                        .commit()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    fun cameraDialog(){
        val dialogBox = DialogBox()
        dialogBox.setCallingActivity(this)
        dialogBox.show(getFragmentManager(), "dialog_box")
    }

    private fun callCameraApp(){
        // this method is called by the presenter
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) !=  null){
            val photoFile : File?
            try {
                photoFile = createImageFile()
            } catch (e : IOException){
                // Tell the user of the Issue
                return
            }
            val photoFileUri = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoFileUri)
            startActivityForResult(intent, imageCaptureToken)
        }
    }
    override fun onPositiveBtnClicked() {
        callCameraApp()
    }

    override fun onNegativeBtnClicked() {
        //
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == imageCaptureToken && resultCode == Activity.RESULT_OK) {
            data?.putExtra(ImageUploadFragment.IMAGE_FILE_PATH, imagePath)
            val fragment = ImageUploadFragment.newInstance(data?.extras)
            presenter.navigate(fragment)
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
        imagePath = "file:" + image.absolutePath
        return image
    }

    override fun onBackBtnPressed() {
        supportFragmentManager.popBackStack()
        if(supportFragmentManager.backStackEntryCount <= 1){
            finish()
        }
    }

    fun shutDownToolBar(){
        toolbar?.visibility = View.GONE
    }

    fun callCamFunc(){
        callCameraApp()
    }
}
