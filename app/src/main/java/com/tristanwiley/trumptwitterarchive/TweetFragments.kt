package com.tristanwiley.trumptwitterarchive

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.reflect.TypeToken
import com.koushikdutta.ion.Ion
import com.tristanwiley.trumptwitterarchive.Adapters.Tweet
import com.tristanwiley.trumptwitterarchive.Adapters.TweetAdapter
import com.tristanwiley.trumptwitterarchive.Adapters.TwitterAccount
import kotlinx.android.synthetic.main.fragment_tweets.view.*


class TweetFragments : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_tweets, container, false)

        val context = view.context
        val accountName: TwitterAccount
        val bundle = (this.arguments as Bundle)
        accountName = bundle.getParcelable("account")

//        Toast.makeText(activity, accountName.account, Toast.LENGTH_SHORT).show()

        view.recyclerTweets.layoutManager = LinearLayoutManager(context)
        view.recyclerTweets.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))

        val accounts = Ion.with(activity)
                .load("http://www.trumptwitterarchive.com/data/${accountName.account}/2017.json")
                .`as`(object : TypeToken<ArrayList<Tweet>>() {})
                .get()

        view.recyclerTweets.adapter = TweetAdapter(activity, accounts)

        return view
    }

    override fun onDetach() {
        super.onDetach()
    }

}
