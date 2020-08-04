package com.sahilmehra.expensemanager.ui

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.sahilmehra.expensemanager.readableFormat
import com.sahilmehra.expensemanager.viewmodel.TransactionViewModel
import kotlinx.android.synthetic.main.fragment_add_transaction.*
import java.text.SimpleDateFormat
import java.util.*

class AddTransactionFragment : Fragment() {
    private lateinit var viewModel: TransactionViewModel
    private var recurring = false
    private val calendar = Calendar.getInstance()
    private var transactionDate: Date = calendar.time
    private var fromDate: Date = calendar.time
    private var toDate: Date = calendar.time

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

        tietTransactionDate.setOnClickListener {
            showDatePicker(1)
        }

        tietFrom.setOnClickListener {
            showDatePicker(2)
        }

        tietTo.setOnClickListener {
            showDatePicker(3)
        }
    }

    private fun showDatePicker(dateId: Int) {
        val dateFragment = DatePickerFragment()

        val calendar = Calendar.getInstance()
        val args = Bundle()
        args.putInt("year", calendar.get(Calendar.YEAR))
        args.putInt("month", calendar.get(Calendar.MONTH))
        args.putInt("day", calendar.get(Calendar.DAY_OF_MONTH))

        dateFragment.arguments = args

        when (dateId) {
            1 -> dateFragment.setCallBack(onDate1)
            2 -> dateFragment.setCallBack(onDate2)
            3 -> dateFragment.setCallBack(onDate3)
        }

        fragmentManager?.let { dateFragment.show(it, "Date Picker") }
    }

    private val onDate1: DatePickerDialog.OnDateSetListener =
        DatePickerDialog.OnDateSetListener { viewDate, year, monthOfYear, dayOfMonth ->
            transactionDate = getSelectedDate(dayOfMonth, monthOfYear, year)
            tietTransactionDate.setText(transactionDate.readableFormat())
        }

    private val onDate2: DatePickerDialog.OnDateSetListener =
        DatePickerDialog.OnDateSetListener { viewDate, year, monthOfYear, dayOfMonth ->
            fromDate = getSelectedDate(dayOfMonth, monthOfYear, year)
            tietFrom.setText(fromDate.readableFormat())
        }

    private val onDate3: DatePickerDialog.OnDateSetListener =
        DatePickerDialog.OnDateSetListener { viewDate, year, monthOfYear, dayOfMonth ->
            toDate = getSelectedDate(dayOfMonth, monthOfYear, year)
            tietTo.setText(toDate.readableFormat())
        }

    private fun getSelectedDate(day: Int, month: Int, year: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        val date: Date = calendar.time
        return date
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val transactionId = AddTransactionFragmentArgs.fromBundle(requireArguments()).transactionId
        viewModel.setTransactionId(transactionId)

        val type = AddTransactionFragmentArgs.fromBundle(requireArguments()).type

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

        when (type) {
            1 -> viewModel.upcomingTransaction.observe(
                viewLifecycleOwner,
                androidx.lifecycle.Observer {
                    if (it != null)
                        setUpcomingTransaction(it)
                })
            2 -> viewModel.pastTransaction.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                if (it != null)
                    setPastTransaction(it)
            })
        }
    }

    private fun setUpcomingTransaction(upcomingTransaction: UpcomingTransaction) {
        cbRecurringTransaction.isChecked = true
        cbRecurringTransaction.isEnabled = false
        recurring = true
        tilFrom.visibility = View.VISIBLE
        tilTo.visibility = View.VISIBLE
        tilRecurringPeriod.visibility = View.VISIBLE

        with(upcomingTransaction) {
            tietTransactionName.setText(name)
            tietAmount.setText("$amount")
            tietTransactionDate.text

            transactionDate = date
            tietTransactionDate.setText(transactionDate.readableFormat())

            fromDate = from
            tietFrom.setText(fromDate.readableFormat())

            toDate = to
            tietTo.setText(toDate.readableFormat())

            tietRecurringPeriod.setText("$recurringPeriod")
            spinnerCategory.setSelection(category)
            spinnerTransactionType.setSelection(mode)
            tietComments.setText("$comments")
        }
    }

    private fun setPastTransaction(pastTransaction: PastTransaction) {
        cbRecurringTransaction.isChecked = false
        cbRecurringTransaction.isEnabled = false
        recurring = false

        tilFrom.visibility = View.GONE
        tilTo.visibility = View.GONE
        tilRecurringPeriod.visibility = View.GONE

        with(pastTransaction) {
            tietTransactionName.setText(name)
            tietAmount.setText("$amount")
            tietTransactionDate.text

            transactionDate = date
            tietTransactionDate.setText(transactionDate.readableFormat())

            spinnerCategory.setSelection(category)
            spinnerTransactionType.setSelection(mode)
            tietComments.setText("$comments")
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

        if (tietTransactionName.text.isEmpty() || tietAmount.text.isEmpty() || tietTransactionDate.text.isEmpty() || tietComments.text.isEmpty())
            Toast.makeText(requireContext(), "Please fill all fields!", Toast.LENGTH_LONG).show()
        else
            if (recurring)
                if (tietFrom.text.isEmpty() || tietTo.text.isEmpty() || tietRecurringPeriod.text.isEmpty())
                    Toast.makeText(requireContext(), "Please fill all fields!", Toast.LENGTH_LONG)
                        .show()
                else {
                    val upcomingTransaction = UpcomingTransaction(
                        viewModel.transactionId.value!!,
                        tietTransactionName.text.toString().trim(),
                        tietAmount.text.toString().toFloat(),
                        transactionDate,
                        fromDate,
                        toDate,
                        tietRecurringPeriod.text.toString().toInt(),
                        tietComments.text.toString().trim(),
                        spinnerCategory.selectedItemPosition,
                        spinnerTransactionType.selectedItemPosition,
                        transactionType.ordinal
                    )
                    viewModel.insertUpcomingTransaction(upcomingTransaction)

                    insertMonth(transactionDate) //transaction date
                    //insertMonth(fromDate) //transaction from date

                    findNavController().navigate(R.id.action_addTransaction_to_tab)
                }
            else {
                val pastTransaction = PastTransaction(
                    viewModel.transactionId.value!!,
                    tietTransactionName.text.toString().trim(),
                    tietAmount.text.toString().toFloat(),
                    transactionDate,
                    tietComments.text.toString().trim(),
                    spinnerCategory.selectedItemPosition,
                    spinnerTransactionType.selectedItemPosition,
                    transactionType.ordinal
                )
                viewModel.insertPastTransaction(pastTransaction)

                insertMonth(transactionDate) //transaction date

                findNavController().navigate(R.id.action_addTransaction_to_tab)
            }
    }

    private fun insertMonth(date: Date) {
        val dateFormat = SimpleDateFormat("MMyyyy", Locale.getDefault())
        val monthId: String = dateFormat.format(date)

        val sharedPreferences =
            requireActivity().getSharedPreferences("EXPENSE_MANAGER", Context.MODE_PRIVATE)
        val budget = sharedPreferences.getFloat("MONTHLY_BUDGET", 1000F)

        val monthData = Month(monthId, budget)

        viewModel.insertMonth(monthData)
    }
}