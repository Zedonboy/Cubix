package com.redwasp.cubix

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.redwasp.cubix.fragments.OneMoreThing
import com.redwasp.cubix.fragments.splashFragment
import kotlinx.android.synthetic.main.launch_window.*


class splashActivity : AppCompatActivity() {
    private val requestToken = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        // add the fragment
        supportFragmentManager.beginTransaction().replace(R.id.splash_activity_container,splashFragment())
                .commitNow()
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
            if (resultCode == Activity.RESULT_OK){

                supportFragmentManager.beginTransaction().replace(R.id.splash_activity_container, OneMoreThing())
                        .commitNow()
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

    fun gotoNxtActivity(){
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
                        .setLogo(R.drawable.cubix_logo)
                        .build(),
                requestToken
        )
    }
}
