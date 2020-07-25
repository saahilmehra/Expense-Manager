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
import com.sahilmehra.expensemanager.data.PastTransaction
import com.sahilmehra.expensemanager.data.TransactionType
import com.sahilmehra.expensemanager.readableFormat
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.home_list_item.*

class PastTransactionsAdapter(private val context:Context):ListAdapter<PastTransaction, PastTransactionsAdapter.ViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PastTransactionsAdapter.ViewHolder {
        val itemLayout=LayoutInflater.from(parent.context).inflate(R.layout.home_list_item, parent, false)
        return ViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: PastTransactionsAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(override val containerView:View):RecyclerView.ViewHolder(containerView), LayoutContainer{
        fun bind(pastTransaction: PastTransaction){
            with(pastTransaction){
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

class DiffCallback:DiffUtil.ItemCallback<PastTransaction>(){
    override fun areItemsTheSame(oldItem: PastTransaction, newItem: PastTransaction): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PastTransaction, newItem: PastTransaction): Boolean {
        return oldItem==newItem
    }
}