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
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.home_list_item.*

class MonthsInnerListAdapter(
    private val context: Context
) :
    ListAdapter<PastTransaction, MonthsInnerListAdapter.ViewHolder>(DiffCallbackMt()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MonthsInnerListAdapter.ViewHolder {
        val itemLayout =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.month_transactions_list_item, parent, false)
        return ViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: MonthsInnerListAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(pastTransaction: PastTransaction) {
            with(pastTransaction) {
                tvName.text = name

                if (type == TransactionType.Income.ordinal) {
                    tvAmount.text = "+$amount"
                    tvAmount.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.income_text_color
                        )
                    )
                } else {
                    tvAmount.text = "-$amount"
                    tvAmount.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.expense_text_color
                        )
                    )
                }
            }
        }
    }
}

class DiffCallbackMt : DiffUtil.ItemCallback<PastTransaction>() {
    override fun areItemsTheSame(oldItem: PastTransaction, newItem: PastTransaction): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PastTransaction, newItem: PastTransaction): Boolean {
        return oldItem == newItem
    }
}