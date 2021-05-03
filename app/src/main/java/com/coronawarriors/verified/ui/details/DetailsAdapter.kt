package com.coronawarriors.verified.ui.details

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.coronawarriors.verified.data.models.RequirementDetail
import com.coronawarriors.verified.databinding.ItemDetailsBinding

class DetailsAdapter(
    private val isSearchUI: Boolean
) : RecyclerView.Adapter<DetailsAdapter.DetailsViewHolder>() {

    private lateinit var requirementList: ArrayList<RequirementDetail>

    inner class DetailsViewHolder(
        private val binding: ItemDetailsBinding,
        private val isSearchUI: Boolean
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.vgVerifyView.visibility = if (isSearchUI) GONE else VISIBLE
        }

        fun bind(requirement: RequirementDetail) {
            binding.requirement = requirement
            binding.tvNotesDetail.visibility =
                if (requirement.requirementNotes.isNullOrBlank()) GONE else VISIBLE
            binding.tvAddressDetail.text = String.format(
                "%s, %s, %s, %s", requirement.landmark,
                requirement.country, requirement.state, requirement.city
            )
            val adapter = ContactsAdapter(requirement.contactList, isSearchUI)
            binding.rvContactList.adapter = adapter
        }
    }

    fun updateDetails(requirement: ArrayList<RequirementDetail>) {
        this.requirementList = requirement
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsViewHolder {
        val binding: ItemDetailsBinding = ItemDetailsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DetailsViewHolder(binding, isSearchUI)
    }

    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
        holder.bind(requirementList[position])
    }

    override fun getItemCount(): Int =
        if (requirementList.isNullOrEmpty()) 0 else requirementList.size
}