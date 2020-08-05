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
import com.sahilmehra.expensemanager.ui.adapter.PastTransactionsListAdapter
import com.sahilmehra.expensemanager.viewmodel.TransactionViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class PastTransactionsListFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_past_transactions_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //set up the recycler view to show list of past transactions
        with(rvPastTransactions) {
            adapter = PastTransactionsListAdapter(
                requireContext(),
                //edit listener and pass the monthId and type as 2(past transaction)
                {
                    findNavController().navigate(
                        PastTransactionsListFragmentDirections.actionPastTransactionsListToAddTransaction(
                            it,
                            2
                        )
                    )
                }) {
                viewModel.deletePastTransaction(it) //delete transaction
            }
            layoutManager = LinearLayoutManager(context)
        }

        //observe for changes in past transactions and update the ui
        viewModel.pastTransactions.observe(viewLifecycleOwner, Observer {
            (rvPastTransactions.adapter as PastTransactionsListAdapter).submitList(it)
        })
    }
}