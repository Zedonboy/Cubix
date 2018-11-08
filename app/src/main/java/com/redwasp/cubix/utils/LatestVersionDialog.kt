package com.redwasp.cubix.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.redwasp.cubix.R

class LatestVersionDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val DialogBuilder = AlertDialog.Builder(activity)
        return   DialogBuilder.setMessage("Hey, I think we have a new version, download it and enjoy cool new features")
                        .setPositiveButton("ALright") { dialog, _ ->
                            // create an intent
                            val downloadUrl = resources.getString(R.string.downloadUrl)
                            val downloadUri = Uri.parse(downloadUrl)
                            val intent = Intent(Intent.ACTION_WEB_SEARCH, downloadUri)
                            dialog.dismiss()
                            startActivity(intent)
                        }
                        .setNegativeButton("Later"){ dialog, _ ->
                            //  save it at
                            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)?: return@setNegativeButton
                            with(sharedPref.edit()){
                                putBoolean(getString(R.string.latestVerrsionCheckKey),true)
                                        .apply()
                            }
                            dialog.dismiss()
                        }
                        .create()
    }
}