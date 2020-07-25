package com.sahilmehra.expensemanager.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sahilmehra.expensemanager.R
import com.sahilmehra.expensemanager.data.TransactionType
import com.sahilmehra.expensemanager.data.UpcomingTransaction
import com.sahilmehra.expensemanager.readableFormat
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.home_list_item.*

class UpcomingTransactionsAdapter(private val context:Context): ListAdapter<UpcomingTransaction, UpcomingTransactionsAdapter.ViewHolder>(DiffCallbackUT()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UpcomingTransactionsAdapter.ViewHolder {
        val itemLayout=
            LayoutInflater.from(parent.context).inflate(R.layout.home_list_item, parent, false)
        return ViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder:UpcomingTransactionsAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(override val containerView: View): RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(upcomingTransaction: UpcomingTransaction){
            with(upcomingTransaction){
                tvName.text=name
                tvDate.text=date.readableFormat()
                if(type == TransactionType.Income.ordinal){
                    tvAmount.text="+$amount"
                    tvAmount.setTextColor(ContextCompat.getColor(context, R.color.income_text_color))
                }
                else{
                    tvAmount.text="-$amount"
                    tvAmount.setTextColor(ContextCompat.getColor(context, R.color.expense_text_color))
                }
            }
        }
    }
}

class DiffCallbackUT: DiffUtil.ItemCallback<UpcomingTransaction>(){
    override fun areItemsTheSame(oldItem: UpcomingTransaction, newItem: UpcomingTransaction): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UpcomingTransaction, newItem: UpcomingTransaction): Boolean {
        return oldItem==newItem
    }
}