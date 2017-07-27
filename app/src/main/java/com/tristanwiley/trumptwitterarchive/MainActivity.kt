package com.tristanwiley.trumptwitterarchive

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.twitter.sdk.android.core.Twitter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        Twitter.initialize(applicationContext)

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, AccountsListFragment())
                .commit()
    }
}