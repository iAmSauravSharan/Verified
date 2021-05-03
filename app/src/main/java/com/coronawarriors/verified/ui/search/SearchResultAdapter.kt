package com.coronawarriors.verified.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.coronawarriors.verified.databinding.ItemSearchSuggestionBinding

class SearchResultAdapter(private  val clickListener: OnResultSelectedClickListener) : RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder>() {

    private lateinit var context: Context
    private val resultList = ArrayList<String>()

    inner class SearchResultViewHolder(private val binding: ItemSearchSuggestionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: String, clickListener: OnResultSelectedClickListener) {
            binding.result = result
            binding.tvResult.setOnClickListener{
                clickListener.onItemSelected(result)
            }
        }

    }

    fun updateList(updatedList: ArrayList<String>) {
        if (!updatedList.isNullOrEmpty()) {
            resultList.clear()
            resultList.addAll(updatedList)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        context = parent.context
        val binding: ItemSearchSuggestionBinding = ItemSearchSuggestionBinding
            .inflate(LayoutInflater.from(context), parent, false)
        return SearchResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(resultList[position], clickListener)
    }

    override fun getItemCount(): Int = resultList.size
}