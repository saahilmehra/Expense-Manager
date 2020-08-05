package com.sahilmehra.expensemanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sahilmehra.expensemanager.R
import com.sahilmehra.expensemanager.ui.adapter.PastTransactionsListAdapter
import com.sahilmehra.expensemanager.viewmodel.TransactionViewModel
import kotlinx.android.synthetic.main.fragment_month.*

class MonthFragment : Fragment() {
    //create object of view model
    private lateinit var viewModel: TransactionViewModel
    private var expense: Float = 0.0F
    private var income: Float = 0.0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //instantiate view model
        viewModel = ViewModelProvider(requireActivity()).get(TransactionViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_month, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //get the monthId from arguments
        val monthId = MonthFragmentArgs.fromBundle(requireArguments()).monthId
        viewModel.setMonthId(monthId) //update the monthId in view model

        //setup recycler view
        with(rvMonthTransactions) {
            adapter =
                PastTransactionsListAdapter(
                    requireContext(),
                    //edit listener and pass the monthId and type as 2(past transaction)
                    {
                        findNavController().navigate(
                            MonthFragmentDirections.actionMonthDetailToAddTransaction(it, 2)
                        )
                    }) { viewModel.deletePastTransaction(it) } //delete listener
            layoutManager = LinearLayoutManager(context)
        }

        //observe for changes in past transactions and update the ui
        viewModel.pastTransactionsByMonth.observe(viewLifecycleOwner, Observer {
            (rvMonthTransactions.adapter as PastTransactionsListAdapter).submitList(it)
        })

        //observe for changes in amount spent and update the ui
        viewModel.expenseByMonth.observe(viewLifecycleOwner, Observer {
            expense = it ?: 0F
            setAmount()
        })

        //observe for changes in amount earned and update the ui
        viewModel.incomeByMonth.observe(viewLifecycleOwner, Observer {
            income = it ?: 0F
            setAmount()
        })
    }

    private fun setAmount() {
        //update the text views with the required data
        tvAmountSpent.text = "$expense"
        tvAmountSaved.text = "$income"

        val netBalance = income - expense

        tvNetBalance.text = "$netBalance"

        //if net balance is less than 0, mark its color as red, otherwise green
        if (netBalance < 0)
            tvNetBalance.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.expense_text_color
                )
            )
        else
            tvNetBalance.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.income_text_color
                )
            )

        //calculate the amount saved percentage and show it in progress bar
        val savedPercentage = (income / (income + expense)) * 100
        progressBarMonth.progress = savedPercentage.toInt()
    }
}