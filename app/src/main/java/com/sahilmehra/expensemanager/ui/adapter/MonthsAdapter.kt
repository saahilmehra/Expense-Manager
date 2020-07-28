package com.sahilmehra.expensemanager.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sahilmehra.expensemanager.R
import com.sahilmehra.expensemanager.data.Month
import com.sahilmehra.expensemanager.monthName
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.month_list_item.*

class MonthsAdapter(
    private val context: Context,
    private val listener: (String) -> Unit
) :
    ListAdapter<Month, MonthsAdapter.ViewHolder>(DiffCallbackMonth()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MonthsAdapter.ViewHolder {
        val itemLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.month_list_item, parent, false)
        return ViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MonthsAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(month: Month) {
            with(month) {
                tvMonthName.text = id.substring(0, 2).monthName()
            }

            btnMonthListNext.setOnClickListener { listener.invoke(month.id) }
        }
    }
}

class DiffCallbackMonth : DiffUtil.ItemCallback<Month>() {
    override fun areItemsTheSame(oldItem: Month, newItem: Month): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Month, newItem: Month): Boolean {
        return oldItem == newItem
    }
}