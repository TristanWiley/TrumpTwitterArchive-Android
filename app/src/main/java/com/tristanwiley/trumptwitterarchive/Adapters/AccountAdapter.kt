package com.tristanwiley.trumptwitterarchive.Adapters

import android.content.Context
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tristanwiley.trumptwitterarchive.R
import com.tristanwiley.trumptwitterarchive.TweetFragments
import kotlinx.android.synthetic.main.account_item_content.view.*


/**
 * Adapter created by Tristan to display all accounts
 */

data class TwitterAccount(val account: String, val display: Boolean, val id: Long, val inactive: Boolean, val linked: String, val name: String, val startingYear: String, val title: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readLong(),
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(account)
        parcel.writeByte(if (display) 1 else 0)
        parcel.writeLong(id)
        parcel.writeByte(if (inactive) 1 else 0)
        parcel.writeString(linked)
        parcel.writeString(name)
        parcel.writeString(startingYear)
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TwitterAccount> {
        override fun createFromParcel(parcel: Parcel): TwitterAccount {
            return TwitterAccount(parcel)
        }

        override fun newArray(size: Int): Array<TwitterAccount?> {
            return arrayOfNulls(size)
        }
    }
}

class AccountAdapter(var context: Context, var accounts: ArrayList<TwitterAccount>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        val v = LayoutInflater.from(context).inflate(R.layout.account_item_content, parent, false)
        return Item(v, context)
    }

    override fun getItemCount(): Int {
        return accounts.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as Item).bindData(accounts[position])
    }

    class Item(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView) {
        fun bindData(account: TwitterAccount) {
            itemView.account_name.text = account.name
            itemView.account_title.text = account.title
            itemView.account_year.text = account.startingYear

            itemView.setOnClickListener {
                val fragment = TweetFragments()
                val bundle = Bundle()
                bundle.putParcelable("account", account)
                fragment.arguments = bundle
                (context as FragmentActivity).supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit()

            }
        }
    }
}