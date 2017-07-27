package com.tristanwiley.trumptwitterarchive

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.reflect.TypeToken
import com.koushikdutta.ion.Ion
import com.tristanwiley.trumptwitterarchive.Adapters.Tweet
import com.tristanwiley.trumptwitterarchive.Adapters.TweetAdapter
import com.tristanwiley.trumptwitterarchive.Adapters.TwitterAccount
import kotlinx.android.synthetic.main.fragment_tweets.view.*


class TweetFragments : Fragment() {
    lateinit var accountName: TwitterAccount
    var tweets: ArrayList<Tweet> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = (this.arguments as Bundle)
        accountName = bundle.getParcelable("account")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater?.inflate(R.layout.fragment_tweets, container, false) as View
        view.recyclerTweets.layoutManager = LinearLayoutManager(activity)
        view.recyclerTweets.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        val adapter = TweetAdapter(activity, tweets)
        view.recyclerTweets.adapter = adapter
        Ion.with(activity)
                .load("http://www.trumptwitterarchive.com/data/${accountName.account}/2017.json")
                .`as`(object : TypeToken<ArrayList<Tweet>>() {})
                .setCallback { e, result ->
                    tweets.addAll(result)
                    adapter.notifyDataSetChanged()
                    view.progressBar.visibility = View.GONE
                    view.loadingText.visibility = View.GONE
                }
        return view
    }

    override fun onDetach() {
        super.onDetach()
    }

}
