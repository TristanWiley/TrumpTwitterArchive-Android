package com.tristanwiley.trumptwitterarchive

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import com.twitter.sdk.android.core.Twitter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var mOptionsMenu: Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        mOptionsMenu = menu as Menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                val searchMenuItem = mOptionsMenu.findItem(R.id.action_search)
                val searchView = searchMenuItem.actionView as SearchView
                if (!searchView.isIconified) {
                    searchView.isIconified = true
                } else {
                    supportFragmentManager.popBackStack()
                }
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item);
            }
        }

    }

    override fun onBackPressed() {
        val searchMenuItem = mOptionsMenu.findItem(R.id.action_search)
        val searchView = searchMenuItem.actionView as SearchView
        if (!searchView.isIconified) {
            searchView.isIconified = true
        } else {
            super.onBackPressed()
            supportFragmentManager.popBackStack()
        }
    }

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