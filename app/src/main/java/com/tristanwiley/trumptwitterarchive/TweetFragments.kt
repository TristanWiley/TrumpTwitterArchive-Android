package com.tristanwiley.trumptwitterarchive

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.gson.reflect.TypeToken
import com.koushikdutta.ion.Ion
import com.tristanwiley.trumptwitterarchive.Adapters.TweetAdapter
import com.tristanwiley.trumptwitterarchive.Adapters.TweetObject
import com.tristanwiley.trumptwitterarchive.Adapters.TwitterAccount
import kotlinx.android.synthetic.main.fragment_tweets.*
import kotlinx.android.synthetic.main.fragment_tweets.view.*
import java.util.*


class TweetFragments : Fragment() {
    lateinit var adapter: TweetAdapter
    lateinit var accountObject: TwitterAccount
    var allTweets: ArrayList<TweetObject> = arrayListOf()
    var tweets: ArrayList<TweetObject> = arrayListOf()
    lateinit var allYears: List<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = (this.arguments as Bundle)
        accountObject = bundle.getParcelable("account")
        allYears = getYearsFromStarting(accountObject.startingYear)
        setHasOptionsMenu(true)
        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.setDisplayShowHomeEnabled(true)
        toolbar?.title = accountObject.name
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater?.inflate(R.layout.fragment_tweets, container, false) as View

        view.recyclerTweets.layoutManager = LinearLayoutManager(activity)
        view.recyclerTweets.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        adapter = TweetAdapter(activity, tweets)
        view.recyclerTweets.adapter = adapter
        return view
    }

    fun loadYear(year: Int) {
        val dataURL: String
        if (year == Calendar.getInstance().get(Calendar.YEAR)) {
            dataURL = "http://www.trumptwitterarchive.com/data/${accountObject.account}/$year.json"
        } else {
            dataURL = "http://trumptwitterarchivedata.s3-website-us-east-1.amazonaws.com/data/${accountObject.account}/$year.json"
        }
        Ion.with(activity)
                .load(dataURL)
                .`as`(object : TypeToken<ArrayList<TweetObject>>() {})
                .setCallback { e, result ->
                    if (e != null) {
                        Log.wtf("TweetFragments", e.message)
                    } else {
                        if (result.isNotEmpty()) {
                            allTweets.clear()
                            allTweets.addAll(result)
                            tweets.clear()
                            tweets.addAll(result)
                            adapter.notifyDataSetChanged()
                            view?.progressBar?.visibility = View.GONE
                            view?.loadingText?.visibility = View.GONE
                        } else {
                            activity.emptyTweets.text = getString(R.string.tweets_empty, year)
                        }
                    }
                }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.tweets_menu, menu)
        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        val spinner = menu.findItem(R.id.spinner)?.actionView as Spinner
        val spinnerAdapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, allYears)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = spinnerAdapter
        spinner.setSelection(allYears.size - 1)

        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                loadYear(allYears[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

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

    fun getYearsFromStarting(startingYear: String): List<Int> {
        val start = startingYear.toInt()
        val end = Calendar.getInstance().get(Calendar.YEAR)
        val list = (start..end).toList()
        return list
    }

    override fun onDetach() {
        super.onDetach()
    }
}
