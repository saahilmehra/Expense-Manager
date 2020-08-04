package com.sahilmehra.expensemanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sahilmehra.expensemanager.R
import com.sahilmehra.expensemanager.ui.adapter.UpcomingTransactionsListAdapter
import com.sahilmehra.expensemanager.viewmodel.TransactionViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class UpcomingTransactionsListFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_upcoming_transactions_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(rvUpcomingTransactions) {
            adapter = UpcomingTransactionsListAdapter(
                requireContext(),
                {
                    findNavController().navigate(
                        UpcomingTransactionsListFragmentDirections.actionUpcomingTransactionsListToAddTransaction(
                            it,
                            1
                        )
                    )
                }) {
                viewModel.deleteUpcomingTransaction(it)
            }
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.upcomingTransactions.observe(viewLifecycleOwner, Observer {
            (rvUpcomingTransactions.adapter as UpcomingTransactionsListAdapter).submitList(it)
        })
    }
}