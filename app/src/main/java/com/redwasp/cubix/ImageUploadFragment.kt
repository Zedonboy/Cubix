package com.redwasp.cubix

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.redwasp.cubix.archComponents_Presenters.ImageUploadPresenter
import kotlinx.android.synthetic.main.fragment_image_upload.*

private const val ARG_PARAM1 = "data"
private const val ARG_PARAM2 = MediaStore.EXTRA_OUTPUT

class ImageUploadFragment : Fragment() {

    private var thumbnail: Bitmap? = null
    lateinit var fullPhotoPath: String
    var noteName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_upload, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI()
    }

    private fun initUI(){
        image_upload_thumbnail?.setImageBitmap(thumbnail)
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


    companion object {
        @JvmStatic
        fun newInstance(param1: Bundle?, path : String) =
                ImageUploadFragment().apply {
                    arguments = param1
                    fullPhotoPath = path
                }
        const val IMAGE_FILE_PATH = "image_file_path"
    }
}
