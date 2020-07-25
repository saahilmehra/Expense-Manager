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
import com.sahilmehra.expensemanager.data.TransactionMode
import com.sahilmehra.expensemanager.data.TransactionType
import com.sahilmehra.expensemanager.data.UpcomingTransaction
import com.sahilmehra.expensemanager.readableFormat
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.transactions_list_item.*

class UpcomingTransactionsListAdapter(private val context: Context) :
    ListAdapter<UpcomingTransaction, UpcomingTransactionsListAdapter.ViewHolder>(DiffCallbackUtList()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UpcomingTransactionsListAdapter.ViewHolder {
        val itemLayout =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.transactions_list_item, parent, false)
        return ViewHolder(itemLayout)
    }

    override fun onBindViewHolder(
        holder: UpcomingTransactionsListAdapter.ViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(upcomingTransaction: UpcomingTransaction) {
            with(upcomingTransaction) {
                tvNameList.text = name
                tvDateList.text = date.readableFormat()
                when (mode) {
                    0 -> tvTransactionTypeList.text = TransactionMode.Cash.name
                    1 -> tvTransactionTypeList.text = TransactionMode.Card.name
                    2 -> tvTransactionTypeList.text = TransactionMode.Cheque.name
                    3 -> tvTransactionTypeList.text = TransactionMode.Others.name
                }

                if (type == TransactionType.Income.ordinal) {
                    tvAmountList.text = "+$amount"
                    tvAmountList.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.income_text_color
                        )
                    )
                } else {
                    tvAmountList.text = "-$amount"
                    tvAmountList.setTextColor(
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

class DiffCallbackUtList : DiffUtil.ItemCallback<UpcomingTransaction>() {
    override fun areItemsTheSame(
        oldItem: UpcomingTransaction,
        newItem: UpcomingTransaction
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: UpcomingTransaction,
        newItem: UpcomingTransaction
    ): Boolean {
        return oldItem == newItem
    }
}