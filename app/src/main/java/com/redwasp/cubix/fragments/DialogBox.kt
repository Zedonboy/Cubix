package com.redwasp.cubix.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import com.redwasp.cubix.R
import com.redwasp.cubix.utils.DialogActivityInterface

class DialogBox() : DialogFragment() {
    private lateinit var callingActivity : DialogActivityInterface
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage(R.string.notify_handwriting)
        builder.setNegativeButton(R.string.cancel) { dialog, _ ->
            callingActivity.onNegativeBtnClicked()
            dialog.dismiss()
        }

        builder.setPositiveButton(R.string.ok){ dialog, _ ->
            callingActivity.onPositiveBtnClicked()
            dialog.dismiss()
        }

        return builder.create()
    }
    fun setCallingActivity(activity : DialogActivityInterface){
        this.callingActivity = activity
    }
}