package com.redwasp.cubix.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.redwasp.cubix.R

class WelcomeDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(R.string.welcomeMessage)
        builder.setPositiveButton(R.string.ok){
            dialog, _-> dialog.dismiss()
        }
        return builder.create()
    }
}