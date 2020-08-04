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
    private lateinit var viewModel: TransactionViewModel
    private var expense: Float = 0.0F
    private var income: Float = 0.0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        val monthId = MonthFragmentArgs.fromBundle(requireArguments()).monthId
        viewModel.setMonthId(monthId)

        with(rvMonthTransactions) {
            adapter =
                PastTransactionsListAdapter(
                    requireContext(),
                    {
                        findNavController().navigate(
                            MonthFragmentDirections.actionMonthDetailToAddTransaction(it)
                        )
                    }) { viewModel.deletePastTransaction(it) }
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.pastTransactionsByMonth.observe(viewLifecycleOwner, Observer {
            (rvMonthTransactions.adapter as PastTransactionsListAdapter).submitList(it)
        })

        viewModel.expenseByMonth.observe(viewLifecycleOwner, Observer {
            expense = it ?: 0F
            setAmount()
        })

        viewModel.incomeByMonth.observe(viewLifecycleOwner, Observer {
            income = it ?: 0F
            setAmount()
        })
    }

    private fun setAmount() {
        tvAmountSpent.text = "$expense"
        tvAmountSaved.text = "$income"

        val netBalance = income - expense

        tvNetBalance.text = "$netBalance"

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

        val savedPercentage = (income / (income + expense)) * 100
        progressBarMonth.progress = savedPercentage.toInt()
    }
}