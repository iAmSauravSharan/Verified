package com.coronawarriors.verified.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.Executors

abstract class BaseListAdapter<ItemType>(
    callBack: DiffUtil.ItemCallback<ItemType> = DefaultItemDiffCallback(),
    private inline val onItemClicked: ((ItemType, Int) -> Unit)? = null
) : ListAdapter<ItemType, BaseItemViewHolder>(
    AsyncDifferConfig.Builder<ItemType>(callBack)
        .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
        .build()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemViewHolder {

        return BaseItemViewHolder(

            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                getLayoutRes(viewType),
                parent, false
            )
        ).apply {
            onViewHolderCreated(this, viewType, binding)
        }

    }

    fun createCustomViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return BaseItemViewHolder(

            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                getLayoutRes(viewType),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(
        holder: BaseItemViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val item: ItemType? = currentList.getOrNull(position)

        item?.let {
//            holder.binding.setVariable(BR.item, item)
            onViewHolderBound(holder.binding, item, position, payloads)
            holder.binding.executePendingBindings()
        }

    }

    override fun onBindViewHolder(holder: BaseItemViewHolder, position: Int) {

    }


    /**
     * get layout res based on view type
     */
    protected abstract fun getLayoutRes(viewType: Int): Int

    /**
     * Called when a ViewHolder is created. ViewHolder is either created first time or
     * when data is refreshed.
     *
     * This method is not called when RecyclerView is being scrolled
     */
    open fun onViewHolderCreated(
        viewHolder: RecyclerView.ViewHolder,
        viewType: Int,
        binding: ViewDataBinding
    ) {

        binding.root.setOnClickListener {
            onItemClicked?.invoke(getItem(viewHolder.bindingAdapterPosition), viewHolder.bindingAdapterPosition)
        }
    }

    /**
     * bind view while RecyclerView is being scrolled and new items are bound
     */
    open fun onViewHolderBound(
        binding: ViewDataBinding,
        item: ItemType,
        position: Int,
        payloads: MutableList<Any>
    ) {

    }


}

open class BaseItemViewHolder(
    val binding: ViewDataBinding
) : RecyclerView.ViewHolder(binding.root)


class DefaultItemDiffCallback<ItemType> : DiffUtil.ItemCallback<ItemType>() {

    override fun areItemsTheSame(
        oldItem: ItemType,
        newItem: ItemType
    ): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(
        oldItem: ItemType,
        newItem: ItemType
    ): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }
}