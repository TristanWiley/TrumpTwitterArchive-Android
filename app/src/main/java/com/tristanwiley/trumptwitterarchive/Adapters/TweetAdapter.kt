package com.tristanwiley.trumptwitterarchive.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tristanwiley.trumptwitterarchive.R
import kotlinx.android.synthetic.main.tweet_item_content.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Adapter created by Tristan to display all accounts
 */

data class Tweet(val source: String, val created_at: String, val text: String, val is_retweet: Boolean, val id_str: String)

class TweetAdapter(var context: Context, var accounts: ArrayList<Tweet>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        val v = LayoutInflater.from(context).inflate(R.layout.tweet_item_content, parent, false)
        return Item(v, context)
    }

    override fun getItemCount(): Int {
        return accounts.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as Item).bindData(accounts[position])
    }

    class Item(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {
        val simpleDateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.getDefault())
        fun bindData(tweet: Tweet) {
            val tweetDate = simpleDateFormat.parse(tweet.created_at)

            itemView.tweet_source.text = tweet.source
            itemView.tweet_date.text = tweetDate.toString()
            itemView.tweet_content.text = tweet.text

            itemView.setOnClickListener {
                Log.wtf("TWEET", tweet.text)
            }
        }
    }
}