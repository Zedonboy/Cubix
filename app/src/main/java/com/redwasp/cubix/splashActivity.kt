package com.redwasp.cubix

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.launch_window.*


class splashActivity : AppCompatActivity() {
    private val requestToken = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.launch_window)
        Handler().postDelayed({
            /* Create an Intent that will start the Menu-Activity. */
            val auth = FirebaseAuth.getInstance()
            if (auth.currentUser == null){
                // not signed
                showUserSignIn()
                return@postDelayed
            }
            gotoNxtActivity()
        }, 3000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestToken){
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK){
                val auth = FirebaseAuth.getInstance()
                val app = application as App
                app.CurrentUser = auth.currentUser
                gotoNxtActivity()
            } else {
                splash_activity_retry?.visibility = View.VISIBLE
                splash_activity_retry?.setOnClickListener{_ ->
                    showUserSignIn()
                }
                splash_activty_skip?.visibility = View.VISIBLE
                splash_activty_skip?.setOnClickListener{_ ->
                    gotoNxtActivity()
                }
            }
        }
    }

    private fun gotoNxtActivity(){
        val mainIntent = Intent(this@splashActivity, DiscoverActivity::class.java)
        this@splashActivity.startActivity(mainIntent)
        this@splashActivity.finish()
    }
    private fun showUserSignIn(){
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
                requestToken
        )
    }
}
