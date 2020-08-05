package com.sahilmehra.expensemanager.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sahilmehra.expensemanager.R
import com.sahilmehra.expensemanager.data.Month
import com.sahilmehra.expensemanager.data.PastTransaction
import com.sahilmehra.expensemanager.monthName
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.month_list_item.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MonthsAdapter(
    private val monthCardTransactions: MonthCardTransactions,
    private val context1: Context,
    private val listener: (String) -> Unit
) :
    ListAdapter<Month, MonthsAdapter.ViewHolder>(DiffCallbackMonth()) {

    //create interface for transactions and amount spent in a particular month
    interface MonthCardTransactions {
        suspend fun getPastTransactionsByMonth(monthId: String): List<PastTransaction>
        suspend fun getMonthExpense(monthId: String): Float
    }

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

    override fun getItemCount(): Int {
        return if(currentList.size<=3)
            super.getItemCount()
        else
            3
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(month: Month) {
            //populate the ui with data
            tvMonthName.text = month.id.substring(0, 2).monthName()

            //set up transactions recycler view
            with(rvMonthTransactions) {
                adapter = MonthsInnerListAdapter(
                    context1
                )
                layoutManager = LinearLayoutManager(context)
            }

            CoroutineScope(Dispatchers.Main).launch {
                //fetch transactions on IO thread
                val task = async(Dispatchers.IO) {
                    monthCardTransactions.getPastTransactionsByMonth(month.id)
                }

                //fetch amount spent on IO thread
                val expenseTask = async(Dispatchers.IO) {
                    monthCardTransactions.getMonthExpense(month.id)
                }

                //wait for the results to arrive
                val list = task.await()
                val expense = expenseTask.await()

                //update the ui on arrival of results
                if (list != null) {
                    (rvMonthTransactions.adapter as MonthsInnerListAdapter).submitList(list)
                }

                /*
                If amount spent in a particular month is more than the budget set for that month,
                then show "budget exceed" and mark the card top color as red.
                 */
                if (expense != null) {
                    if (expense > month.budget) {
                        tvBudgetExcceeded.visibility = View.VISIBLE
                        colorView.setBackgroundColor(
                            ContextCompat.getColor(
                                context1,
                                R.color.expense_text_color
                            )
                        )
                    }
                }
            }

            //navigate to month detail fragment
            btnMonthListNext.setOnClickListener { listener.invoke(month.id) }
        }
    }
}

//check if the list has been updated or not
class DiffCallbackMonth : DiffUtil.ItemCallback<Month>() {
    override fun areItemsTheSame(oldItem: Month, newItem: Month): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Month, newItem: Month): Boolean {
        return oldItem == newItem
    }
}