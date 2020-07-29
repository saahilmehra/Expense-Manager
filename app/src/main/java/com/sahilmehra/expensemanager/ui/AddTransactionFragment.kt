package com.sahilmehra.expensemanager.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.sahilmehra.expensemanager.R
import com.sahilmehra.expensemanager.data.*
import com.sahilmehra.expensemanager.viewmodel.TransactionViewModel
import kotlinx.android.synthetic.main.fragment_add_transaction.*
import java.text.SimpleDateFormat
import java.util.*

class AddTransactionFragment : Fragment() {
    private lateinit var viewModel: TransactionViewModel
    private var recurring = false

    private lateinit var tilTransactionName: TextInputLayout
    private lateinit var tilAmount: TextInputLayout
    private lateinit var tilTransactionDate: TextInputLayout
    private lateinit var tilFrom: TextInputLayout
    private lateinit var tilTo: TextInputLayout
    private lateinit var tilRecurringPeriod: TextInputLayout
    private lateinit var tilComments: TextInputLayout

    private lateinit var tietTransactionName: EditText
    private lateinit var tietAmount: EditText
    private lateinit var tietTransactionDate: EditText
    private lateinit var tietFrom: EditText
    private lateinit var tietTo: EditText
    private lateinit var tietRecurringPeriod: EditText
    private lateinit var tietComments: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        viewModel = ViewModelProvider(requireActivity()).get(TransactionViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tietTransactionName = view.findViewById(R.id.tietTransactionName)
        tietAmount = view.findViewById(R.id.tietAmount)
        tietTransactionDate = view.findViewById(R.id.tietTransactionDate)
        tietFrom = view.findViewById(R.id.tietFrom)
        tietTo = view.findViewById(R.id.tietTo)
        tietRecurringPeriod = view.findViewById(R.id.tietRecurringPeriod)
        tietComments = view.findViewById(R.id.tietComments)

        tilTransactionName = view.findViewById(R.id.tilTransactionName)
        tilAmount = view.findViewById(R.id.tilAmount)
        tilTransactionDate = view.findViewById(R.id.tilTransactionDate)
        tilFrom = view.findViewById(R.id.tilFrom)
        tilTo = view.findViewById(R.id.tilTo)
        tilRecurringPeriod = view.findViewById(R.id.tilRecurringPeriod)
        tilComments = view.findViewById(R.id.tilComments)

        validateText(tietTransactionName, tilTransactionName)
        validateText(tietAmount, tilAmount)
        validateText(tietTransactionDate, tilTransactionDate)
        validateText(tietFrom, tilFrom)
        validateText(tietTo, tilTo)
        validateText(tietRecurringPeriod, tilRecurringPeriod)
        validateText(tietComments, tilComments)

        val categories = mutableListOf<String>()
        TransactionCategory.values().forEach {
            categories.add(it.name)
        }
        val categoryAdapter =
            ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, categories)
        spinnerCategory.adapter = categoryAdapter

        val transactionModes = mutableListOf<String>()
        TransactionMode.values().forEach {
            transactionModes.add(it.name)
        }
        val transactionModeAdapter =
            ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, transactionModes)
        spinnerTransactionType.adapter = transactionModeAdapter

        btnExpense.setOnClickListener {
            combineData(TransactionType.Expense)
        }

        btnIncome.setOnClickListener {
            combineData(TransactionType.Income)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        cbRecurringTransaction.setOnClickListener {
            recurring = cbRecurringTransaction.isChecked

            if (recurring) {
                tilFrom.visibility = View.VISIBLE
                tilTo.visibility = View.VISIBLE
                tilRecurringPeriod.visibility = View.VISIBLE
            } else {
                tilFrom.visibility = View.GONE
                tilTo.visibility = View.GONE
                tilRecurringPeriod.visibility = View.GONE
            }
        }
    }

    private fun validateText(editText: EditText, textLayout: TextInputLayout) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty())
                    textLayout.error = "Required Field !"
                else
                    textLayout.error = null
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        editText.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (editText.text.toString().isEmpty())
                    textLayout.error = "Required Field!"
                else
                    textLayout.error = null
            }
        }
    }

    private fun combineData(transactionType: TransactionType) {
        val calendar: Calendar = Calendar.getInstance()

        if (tietTransactionName.text.isEmpty() || tietAmount.text.isEmpty() || tietTransactionDate.text.isEmpty() || tietComments.text.isEmpty())
            Toast.makeText(requireContext(), "Please fill all fields!", Toast.LENGTH_LONG).show()
        else
            if (recurring)
                if (tietFrom.text.isEmpty() || tietTo.text.isEmpty() || tietRecurringPeriod.text.isEmpty())
                    Toast.makeText(requireContext(), "Please fill all fields!", Toast.LENGTH_LONG)
                        .show()
                else {
                    val upcomingTransaction = UpcomingTransaction(
                        0,
                        tietTransactionName.text.toString().trim(),
                        tietAmount.text.toString().toFloat(),
                        calendar.time,
                        calendar.time,
                        calendar.time,
                        tietRecurringPeriod.text.toString().toInt(),
                        tietComments.text.toString().trim(),
                        spinnerCategory.selectedItemPosition,
                        spinnerTransactionType.selectedItemPosition,
                        transactionType.ordinal
                    )
                    viewModel.insertUpcomingTransaction(upcomingTransaction)

                    insertMonth(calendar.time) //transaction date
                    insertMonth(calendar.time) //transaction from date

                    findNavController().navigate(R.id.action_addTransaction_to_tab)
                }
            else {
                val pastTransaction = PastTransaction(
                    0,
                    tietTransactionName.text.toString().trim(),
                    tietAmount.text.toString().toFloat(),
                    calendar.time,
                    tietComments.text.toString().trim(),
                    spinnerCategory.selectedItemPosition,
                    spinnerTransactionType.selectedItemPosition,
                    transactionType.ordinal
                )
                viewModel.insertPastTransaction(pastTransaction)

                insertMonth(calendar.time) //transaction date

                findNavController().navigate(R.id.action_addTransaction_to_tab)
            }
    }

    private fun insertMonth(date: Date) {
        val dateFormat = SimpleDateFormat("MMyyyy", Locale.getDefault())
        val monthId: String = dateFormat.format(date)

        Log.e("monthId", monthId)

        val monthData = Month(monthId, 35000F)

        viewModel.insertMonth(monthData)
    }
}