package com.redwasp.cubix

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.redwasp.cubix.customViews.Markers
import kotlinx.android.synthetic.main.fragment_image_upload.*
import kotlinx.coroutines.android.UI
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImageUploadFragment : Fragment() {
    private var _imagePath = ""
    var uploadImagePath : String = ""
    set(value) {
        _imagePath = value
    }
    var noteName = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        (activity as DiscoverActivity).setUpToolBAr()
        return inflater.inflate(R.layout.fragment_image_upload, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI()
    }

    private fun initUI(){
        if (_imagePath.isEmpty()) return
        launch {
            val option = BitmapFactory.Options().apply {
                inSampleSize = 4
            }
            val noteImg = BitmapFactory.decodeFile(_imagePath, option)
            withContext(UI){
                image_upload_preview.setImageBitmap(noteImg)
            }
        }
        image_upload_btn?.setOnClickListener {

        }

        image_upload_preview_layout?.addView(Markers(context!!).apply {
            x = 100f
            y = 50f
        })
    }
}
