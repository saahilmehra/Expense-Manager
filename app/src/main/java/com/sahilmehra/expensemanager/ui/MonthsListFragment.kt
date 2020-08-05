package com.sahilmehra.expensemanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sahilmehra.expensemanager.R
import com.sahilmehra.expensemanager.data.PastTransaction
import com.sahilmehra.expensemanager.ui.adapter.MonthsAdapter
import com.sahilmehra.expensemanager.viewmodel.TransactionViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class MonthsListFragment : Fragment(), MonthsAdapter.MonthCardTransactions {
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
        return inflater.inflate(R.layout.fragment_months_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //set up the months recycler view
        with(rvMonths) {
            adapter = MonthsAdapter(this@MonthsListFragment, requireContext()) {
                findNavController().navigate(
                    MonthsListFragmentDirections.actionMonthListToMonthDetail(
                        it
                    )
                )
            }
            layoutManager = LinearLayoutManager(context)
        }

        //observe for changes in months and update the ui
        viewModel.months.observe(viewLifecycleOwner, Observer {
            (rvMonths.adapter as MonthsAdapter).submitList(it)
        })
    }

    //return past transaction of a particular month
    override suspend fun getPastTransactionsByMonth(monthId: String): List<PastTransaction> =
        viewModel.getTrans(monthId)

    //return amount spent in a particular month
    override suspend fun getMonthExpense(monthId: String): Float =
        viewModel.getExpenseTemp(monthId)
}