package com.sahilmehra.expensemanager.ui.tablayout

import android.content.Context
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
import com.sahilmehra.expensemanager.ui.adapter.PastTransactionsListAdapter
import com.sahilmehra.expensemanager.ui.adapter.UpcomingTransactionsListAdapter
import com.sahilmehra.expensemanager.viewmodel.TransactionViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class PersonalFragment : Fragment() {
    //create object of view model
    private lateinit var viewModel: TransactionViewModel

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
        return inflater.inflate(R.layout.fragment_personal, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //setup upcoming transactions recycler view
        with(rvUpcomingTransactions) {
            adapter = UpcomingTransactionsListAdapter(requireContext(), {
                findNavController().navigate(
                    TabFragmentDirections.actionTabToAddTransaction(it, 1)
                )
            }) {
                viewModel.deleteUpcomingTransaction(it)
            }
            layoutManager = LinearLayoutManager(context)
        }

        //setup past transactions recycler view
        with(rvPastTransactions) {
            adapter =
                PastTransactionsListAdapter(requireContext(), {
                    findNavController().navigate(
                        TabFragmentDirections.actionTabToAddTransaction(it, 2)
                    )
                }) { viewModel.deletePastTransaction(it) }
            layoutManager = LinearLayoutManager(context)
        }

        val sharedPreferences =
            requireActivity().getSharedPreferences("EXPENSE_MANAGER", Context.MODE_PRIVATE)
        val budget = sharedPreferences.getFloat("MONTHLY_BUDGET", 1000F)

        tvNetBalance.text = "$budget"

        //observe for changes in upcoming transactions and update the ui
        viewModel.personalUpcomingTransactions.observe(viewLifecycleOwner, Observer {
            (rvUpcomingTransactions.adapter as UpcomingTransactionsListAdapter).submitList(it)
        })

        //observe for changes in past transactions and update the ui
        viewModel.personalPastTransactions.observe(viewLifecycleOwner, Observer {
            (rvPastTransactions.adapter as PastTransactionsListAdapter).submitList(it)
        })
    }
}