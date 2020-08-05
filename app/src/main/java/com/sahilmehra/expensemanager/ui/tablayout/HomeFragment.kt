package com.sahilmehra.expensemanager.ui.tablayout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sahilmehra.expensemanager.R
import com.sahilmehra.expensemanager.data.PastTransaction
import com.sahilmehra.expensemanager.ui.adapter.MonthsAdapter
import com.sahilmehra.expensemanager.ui.adapter.PastTransactionsAdapter
import com.sahilmehra.expensemanager.ui.adapter.UpcomingTransactionsAdapter
import com.sahilmehra.expensemanager.viewmodel.TransactionViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), MonthsAdapter.MonthCardTransactions {
    private lateinit var viewModel: TransactionViewModel
    private var cash: Float = 0.0F
    private var card: Float = 0.0F
    private var cheque: Float = 0.0F
    private var others: Float = 0.0F
    private var totalIncome: Float = 0.0F
    private var totalExpense: Float = 0.0F

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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //setup upcoming transactions recycler view
        with(rvUpcomingTransactions) {
            adapter = UpcomingTransactionsAdapter(requireContext())
            layoutManager = LinearLayoutManager(context)
        }

        //setup past transactions recycler view
        with(rvPastTransactions) {
            adapter = PastTransactionsAdapter(requireContext())
            layoutManager = LinearLayoutManager(context)
        }

        //setup month recycler view
        with(rvMonths) {
            adapter = MonthsAdapter(this@HomeFragment, requireContext()) {
                findNavController().navigate(TabFragmentDirections.actionTabToMonthDetail(it))
            }
            layoutManager = LinearLayoutManager(context)
        }

        //observe for changes in upcoming transactions and update the ui
        viewModel.upcomingTransactions.observe(viewLifecycleOwner, Observer {
            (rvUpcomingTransactions.adapter as UpcomingTransactionsAdapter).submitList(it)
        })

        //observe for changes in past transactions and update the ui
        viewModel.pastTransactions.observe(viewLifecycleOwner, Observer {
            (rvPastTransactions.adapter as PastTransactionsAdapter).submitList(it)
        })

        //observe for changes in amount earned in cash and update the ui
        viewModel.amountInCash.observe(viewLifecycleOwner, Observer {
            cash = it ?: 0F
            setAmount()
        })

        //observe for changes in amount earned in card and update the ui
        viewModel.amountInCard.observe(viewLifecycleOwner, Observer {
            cheque = it ?: 0F
            setAmount()
        })

        //observe for changes in amount earned in cheque and update the ui
        viewModel.amountInCheque.observe(viewLifecycleOwner, Observer {
            cheque = it ?: 0F
            setAmount()
        })

        //observe for changes in amount earned in others and update the ui
        viewModel.amountInOthers.observe(viewLifecycleOwner, Observer {
            others = it ?: 0F
            setAmount()
        })

        //observe for changes in total amount earned and update the ui
        viewModel.totalIncome.observe(viewLifecycleOwner, Observer {
            totalIncome = it ?: 0F
            setAmount()
        })

        //observe for changes in total amount spent and update the ui
        viewModel.totalExpense.observe(viewLifecycleOwner, Observer {
            totalExpense = it ?: 0F
            setAmount()
        })

        //observe for changes in months and update the ui
        viewModel.months.observe(viewLifecycleOwner, Observer {
            (rvMonths.adapter as MonthsAdapter).submitList(it)
        })

        btnUpcomingNext.setOnClickListener {
            //show all upcoming transactions
            findNavController().navigate(R.id.action_tab_to_upcoming_transactions_list)
        }

        btnPastNext.setOnClickListener {
            //show all past transactions
            findNavController().navigate(R.id.action_tab_to_past_transactions_list)
        }

        btnMonthsNext.setOnClickListener {
            //show all months
            findNavController().navigate(R.id.action_tab_to_month_list)
        }
    }

    private fun setAmount() {
        //update the text views
        tvCash.text = "$cash"
        tvCard.text = "$card"
        tvCheque.text = "$cheque"
        tvOthers.text = "$others"

        //calculate net balance and update its text view
        val netBalance = totalIncome - totalExpense
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
    }

    //return past transactions of a particular month
    override suspend fun getPastTransactionsByMonth(monthId: String): List<PastTransaction> =
        viewModel.getTrans(monthId)

    //return amount spent in a particular month
    override suspend fun getMonthExpense(monthId: String): Float =
        viewModel.getExpenseTemp(monthId)
}