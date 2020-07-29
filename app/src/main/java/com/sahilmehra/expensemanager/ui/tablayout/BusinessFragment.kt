package com.sahilmehra.expensemanager.ui.tablayout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sahilmehra.expensemanager.R
import com.sahilmehra.expensemanager.ui.adapter.PastTransactionsListAdapter
import com.sahilmehra.expensemanager.ui.adapter.UpcomingTransactionsListAdapter
import com.sahilmehra.expensemanager.viewmodel.TransactionViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class BusinessFragment : Fragment() {
    private lateinit var viewModel: TransactionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(TransactionViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_business, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(rvUpcomingTransactions) {
            adapter = UpcomingTransactionsListAdapter(requireContext()) {
                viewModel.deleteUpcomingTransaction(it)
            }
            layoutManager = LinearLayoutManager(context)
        }

        with(rvPastTransactions) {
            adapter =
                PastTransactionsListAdapter(requireContext()) { viewModel.deletePastTransaction(it) }
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.businessUpcomingTransactions.observe(viewLifecycleOwner, Observer {
            (rvUpcomingTransactions.adapter as UpcomingTransactionsListAdapter).submitList(it)
        })

        viewModel.businessPastTransactions.observe(viewLifecycleOwner, Observer {
            (rvPastTransactions.adapter as PastTransactionsListAdapter).submitList(it)
        })
    }
}