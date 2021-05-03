package com.coronawarriors.verified.ui.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.coronawarriors.verified.data.models.ContactDetail
import com.coronawarriors.verified.databinding.ItemContactBinding

class ContactsAdapter(
    private val contactList: ArrayList<ContactDetail>?,
    private val isSearchUI: Boolean
): RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {

    inner class ContactsViewHolder(
        private val binding: ItemContactBinding,
        private val isSearchUI: Boolean
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.vgVerifyView.visibility = if (isSearchUI) View.GONE else View.VISIBLE
        }

        fun bind(contact: ContactDetail?) {
            val number = String.format("%s %s", contact?.countryCode, contact?.phoneNumber)
            binding.tvContact.text = number
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val binding: ItemContactBinding = ItemContactBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ContactsViewHolder(binding, isSearchUI)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bind(contactList?.get(position))
    }

    override fun getItemCount(): Int = if (contactList.isNullOrEmpty()) 0 else contactList.size
}