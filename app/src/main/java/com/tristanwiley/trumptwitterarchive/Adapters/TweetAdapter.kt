package com.tristanwiley.trumptwitterarchive.Adapters

import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tristanwiley.trumptwitterarchive.R
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.tweetui.TweetUtils
import com.twitter.sdk.android.tweetui.TweetView
import kotlinx.android.synthetic.main.tweet_item_content.view.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * Adapter created by Tristan to display all accounts
 */

data class TweetObject(val source: String, val created_at: String, val text: String, val is_retweet: Boolean, val id_str: String)

class TweetAdapter(var context: Context, var accounts: ArrayList<TweetObject>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun searchTweets(originalArray: ArrayList<TweetObject>, criteria: String) {
        val filteredArray = originalArray.filter { it.text.contains(criteria) }

        accounts.clear()
        accounts.addAll(filteredArray)
        this.notifyDataSetChanged()
    }

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

    class Item(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView) {
        val simpleDateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.getDefault())
        fun bindData(tweet: TweetObject) {
            val tweetDate = simpleDateFormat.parse(tweet.created_at)

            itemView.tweet_source.text = tweet.source
            itemView.tweet_date.text = tweetDate.toString()
            itemView.tweet_content.text = tweet.text

            itemView.setOnClickListener {
                TweetUtils.loadTweet(tweet.id_str.toLong(), object : Callback<Tweet>() {
                    override fun success(result: Result<Tweet>?) {
                        AlertDialog.Builder(itemView.rootView.context).setView(TweetView(context, result?.data)).show()
                    }

                    override fun failure(exception: TwitterException) {
                        Log.d("AutomaticTwitActivity", " - StealingTwitFromTarget - ERROR: " + exception.toString()
                                + " Cause:" + exception.cause
                                + " LocalMessage:" + exception.localizedMessage
                                + " Message:" + exception.message
                                + " Stack:" + exception.stackTrace.toString()
                        )
                    }
                })
            }
        }
    }
}