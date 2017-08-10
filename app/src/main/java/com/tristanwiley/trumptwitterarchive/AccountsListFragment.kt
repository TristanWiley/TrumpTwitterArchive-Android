package com.tristanwiley.trumptwitterarchive

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import com.google.gson.reflect.TypeToken
import com.koushikdutta.ion.Ion
import com.tristanwiley.trumptwitterarchive.Adapters.AccountAdapter
import com.tristanwiley.trumptwitterarchive.Adapters.TwitterAccount
import kotlinx.android.synthetic.main.fragment_accounts.*
import kotlinx.android.synthetic.main.fragment_accounts.view.*

class AccountsListFragment : Fragment() {
    var accounts: ArrayList<TwitterAccount> = arrayListOf()
    val allAccounts: ArrayList<TwitterAccount> = arrayListOf()
    lateinit var accountsAdapter: AccountAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_accounts, container, false)
        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(false)
        toolbar?.setDisplayShowHomeEnabled(false)
        toolbar?.title = getString(R.string.app_name)

        val context = view.context
        view.recyclerView.layoutManager = LinearLayoutManager(context)
        view.recyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        accountsAdapter = AccountAdapter(activity, accounts)
        view.recyclerView.adapter = accountsAdapter
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Ion.with(activity)
                .load("http://www.trumptwitterarchive.com/data/accounts.json")
                .`as`(object : TypeToken<ArrayList<TwitterAccount>>() {})
                .setCallback { e, result ->
                    if (e != null) {
                        Log.wtf("TweetFragments", e.message)
                    } else {
                        if (result.isNotEmpty()) {
                            accounts.clear()
                            accounts.addAll(result)
                            accounts.sortBy { it.name }
                            accountsAdapter.notifyDataSetChanged()
                            allAccounts.clear()
                            allAccounts.addAll(result)
                            allAccounts.sortBy { it.name }
                        } else {
                            activity.emptyAccounts.text = getString(R.string.accounts_empty)
                            activity.emptyAccounts.visibility = View.VISIBLE
                        }
                    }
                }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.accounts_menu, menu)
        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView

        val queryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                if (allAccounts.isNotEmpty()) {
                    accountsAdapter.searchTweets(allAccounts, newText)
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

    override fun onDetach() {
        super.onDetach()
    }

}
