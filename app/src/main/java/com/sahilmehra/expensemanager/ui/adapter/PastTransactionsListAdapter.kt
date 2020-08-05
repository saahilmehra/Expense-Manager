package com.sahilmehra.expensemanager.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sahilmehra.expensemanager.R
import com.sahilmehra.expensemanager.data.PastTransaction
import com.sahilmehra.expensemanager.data.TransactionMode
import com.sahilmehra.expensemanager.data.TransactionType
import com.sahilmehra.expensemanager.readableFormat
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.transactions_list_item.*

class PastTransactionsListAdapter(
    private val context: Context,
    private val editListener: (Long) -> Unit,
    private val deleteListener: (PastTransaction) -> Unit
) :
    ListAdapter<PastTransaction, PastTransactionsListAdapter.ViewHolder>(DiffCallbackPtList()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PastTransactionsListAdapter.ViewHolder {
        val itemLayout =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.transactions_list_item, parent, false)
        return ViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: PastTransactionsListAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        init {
            //set the popup menu
            itemView.setOnClickListener {
                val popupMenu = PopupMenu(context, itemView)

                popupMenu.inflate(R.menu.past_menu) //inflate the menu layout

                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        //edit the transaction
                        R.id.editPastItem -> {
                            editListener.invoke(getItem(adapterPosition).id)
                        }
                        //delete the transaction
                        R.id.deletePastItem -> {
                            deleteListener.invoke(getItem(adapterPosition))
                        }
                    }
                    true
                }

                popupMenu.show() //show the popup menu
            }
        }

        fun bind(pastTransaction: PastTransaction) {
            //show data in text views
            with(pastTransaction) {
                tvNameList.text = name
                tvDateList.text = date.readableFormat()
                when (mode) {
                    0 -> tvTransactionTypeList.text = TransactionMode.Cash.name
                    1 -> tvTransactionTypeList.text = TransactionMode.Card.name
                    2 -> tvTransactionTypeList.text = TransactionMode.Cheque.name
                    3 -> tvTransactionTypeList.text = TransactionMode.Others.name
                }

                //if type is expense, mark its color as red, otherwise green
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

//check if the list has been updated or not
class DiffCallbackPtList : DiffUtil.ItemCallback<PastTransaction>() {
    override fun areItemsTheSame(oldItem: PastTransaction, newItem: PastTransaction): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PastTransaction, newItem: PastTransaction): Boolean {
        return oldItem == newItem
    }
}