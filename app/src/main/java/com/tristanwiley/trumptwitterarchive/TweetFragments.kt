package com.tristanwiley.trumptwitterarchive

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import com.google.gson.reflect.TypeToken
import com.koushikdutta.ion.Ion
import com.tristanwiley.trumptwitterarchive.Adapters.TweetAdapter
import com.tristanwiley.trumptwitterarchive.Adapters.TweetObject
import com.tristanwiley.trumptwitterarchive.Adapters.TwitterAccount
import kotlinx.android.synthetic.main.fragment_tweets.view.*


class TweetFragments : Fragment() {
    lateinit var adapter: TweetAdapter
    var allTweets: ArrayList<TweetObject> = arrayListOf()

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.tweets_menu, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView: SearchView
        searchView = searchItem?.actionView as SearchView

        val queryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                if (allTweets.isNotEmpty()) {
                    adapter.searchTweets(allTweets, newText)
                }
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }
        }
        searchView.setOnQueryTextListener(queryTextListener)

        super.onCreateOptionsMenu(menu, inflater)

    }

    lateinit var accountName: TwitterAccount
    var tweets: ArrayList<TweetObject> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = (this.arguments as Bundle)
        accountName = bundle.getParcelable("account")
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater?.inflate(R.layout.fragment_tweets, container, false) as View

        view.recyclerTweets.layoutManager = LinearLayoutManager(activity)
        view.recyclerTweets.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        adapter = TweetAdapter(activity, tweets)
        view.recyclerTweets.adapter = adapter
        Ion.with(activity)
                .load("http://www.trumptwitterarchive.com/data/${accountName.account}/2017.json")
                .`as`(object : TypeToken<ArrayList<TweetObject>>() {})
                .setCallback { e, result ->
                    if (e != null) {
                        Log.wtf("TweetFragments", e.message)
                    }
                    allTweets = result
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
