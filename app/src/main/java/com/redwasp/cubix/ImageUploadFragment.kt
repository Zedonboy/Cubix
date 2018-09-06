package com.redwasp.cubix

import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_image_upload.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class ImageUploadFragment : Fragment() {
    private var imagePath = ""
    var fullImagePath = ""
    set(value) {imagePath = value}
    var noteName = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_upload, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI()
    }

    private fun initUI(){
        if (fullImagePath.isEmpty()) return
        launch {
            val thumbImage = BitmapFactory.decodeFile(fullImagePath)
            val thumbnail = ThumbnailUtils.extractThumbnail(thumbImage, 100, 100)
            withContext(UI){
                image_upload_thumbnail?.setImageBitmap(thumbnail)
            }
        }
        image_upload_btn?.setOnClickListener {
            _ ->
            noteName = image_upload_edit_Text?.text?.toString() ?: "note_1"
            // get the file absolute path
        }
    }

    fun showProgressBar(){
        // first disable the button
        // show progressBar
    }
}
