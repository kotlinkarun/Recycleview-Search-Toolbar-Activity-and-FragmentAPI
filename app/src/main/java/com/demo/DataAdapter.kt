@file:Suppress("UNCHECKED_CAST")

package com.demo

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_row.view.*
import java.util.*


class DataAdapter(private val mArrayList: MutableList<Model>) : RecyclerView.Adapter<DataAdapter.ViewHolder>(), Filterable {
    private var mFilteredList: MutableList<Model>? = null

    init {
        mFilteredList = mArrayList
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_row, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.bindItem(mFilteredList!!.get(i))


    }

    override fun getItemCount(): Int {
        return mFilteredList!!.size
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    mFilteredList = mArrayList
                } else {
                    val filteredList = ArrayList<Model>()
                    for (androidVersion in mArrayList) {
                        if (androidVersion.name.toLowerCase().contains(charString)
                                || androidVersion.avatar.toLowerCase().contains(charString)) {
                            filteredList.add(androidVersion)
                        }
                    }
                    mFilteredList = filteredList
                }

                val filterResults = Filter.FilterResults()
                filterResults.values = mFilteredList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
                mFilteredList = filterResults.values as MutableList<Model>
                notifyDataSetChanged()
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(m:Model){
            itemView.tv.text = m.name
            Picasso.get().load(m.avatar).into(itemView.img);
        }
    }
}