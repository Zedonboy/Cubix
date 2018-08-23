package com.redwasp.cubix

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat.startActivity
import android.util.Log


class splashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.launch_window)
        Handler().postDelayed({
            /* Create an Intent that will start the Menu-Activity. */
            val mainIntent = Intent(this@splashActivity, DiscoverActivity::class.java)
            this@splashActivity.startActivity(mainIntent)
            this@splashActivity.finish()
        }, 3000)
    }
}
