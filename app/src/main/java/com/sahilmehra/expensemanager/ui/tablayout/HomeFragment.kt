package com.sahilmehra.expensemanager.ui.tablayout

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
import com.sahilmehra.expensemanager.ui.adapter.MonthsAdapter
import com.sahilmehra.expensemanager.ui.adapter.PastTransactionsAdapter
import com.sahilmehra.expensemanager.ui.adapter.UpcomingTransactionsAdapter
import com.sahilmehra.expensemanager.viewmodel.TransactionViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    private lateinit var viewModel: TransactionViewModel
    private var cash: Float = 0.0F
    private var card: Float = 0.0F
    private var cheque: Float = 0.0F
    private var others: Float = 0.0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(TransactionViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(rvUpcomingTransactions) {
            adapter = UpcomingTransactionsAdapter(requireContext())
            layoutManager = LinearLayoutManager(context)
        }

        with(rvPastTransactions) {
            adapter = PastTransactionsAdapter(requireContext())
            layoutManager = LinearLayoutManager(context)
        }

        with(rvMonths) {
            adapter = MonthsAdapter(requireContext()) {
                findNavController().navigate(TabFragmentDirections.actionTabToMonthDetail(it))
            }
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.upcomingTransactions.observe(viewLifecycleOwner, Observer {
            (rvUpcomingTransactions.adapter as UpcomingTransactionsAdapter).submitList(it)
        })

        viewModel.pastTransactions.observe(viewLifecycleOwner, Observer {
            (rvPastTransactions.adapter as PastTransactionsAdapter).submitList(it)
        })

        viewModel.amountInCash.observe(viewLifecycleOwner, Observer {
            if (it != null)
                cash = it
            setAmount()
        })

        viewModel.amountInCard.observe(viewLifecycleOwner, Observer {
            if (it != null)
                card = it
            setAmount()
        })

        viewModel.amountInCheque.observe(viewLifecycleOwner, Observer {
            if (it != null)
                cheque = it
            setAmount()
        })

        viewModel.amountInOthers.observe(viewLifecycleOwner, Observer {
            if (it != null)
                others = it
            setAmount()
        })

        viewModel.months.observe(viewLifecycleOwner, Observer {
            (rvMonths.adapter as MonthsAdapter).submitList(it)
        })

        btnUpcomingNext.setOnClickListener {
            findNavController().navigate(R.id.action_tab_to_upcoming_transactions_list)
        }

        btnPastNext.setOnClickListener {
            findNavController().navigate(R.id.action_tab_to_past_transactions_list)
        }

        btnMonthsNext.setOnClickListener {
            findNavController().navigate(R.id.action_tab_to_month_list)
        }
    }

    private fun setAmount() {
        tvCash.text = "$cash"
        tvCard.text = "$card"
        tvCheque.text = "$cheque"
        tvOthers.text = "$others"
        tvNetBalance.text = "${cash + card + cheque + others}"
    }
}