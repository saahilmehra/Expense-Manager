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
    //create object of view model
    private lateinit var viewModel: TransactionViewModel
    private var recurring = false //true if it is upcoming
    private val calendar = Calendar.getInstance()
    private var transactionDate: Date = calendar.time
    private var fromDate: Date = calendar.time
    private var toDate: Date = calendar.time

    //Text Input Layouts
    private lateinit var tilTransactionName: TextInputLayout
    private lateinit var tilAmount: TextInputLayout
    private lateinit var tilTransactionDate: TextInputLayout
    private lateinit var tilFrom: TextInputLayout
    private lateinit var tilTo: TextInputLayout
    private lateinit var tilRecurringPeriod: TextInputLayout
    private lateinit var tilComments: TextInputLayout

    //Text Input Edit Texts
    private lateinit var tietTransactionName: EditText
    private lateinit var tietAmount: EditText
    private lateinit var tietTransactionDate: EditText
    private lateinit var tietFrom: EditText
    private lateinit var tietTo: EditText
    private lateinit var tietRecurringPeriod: EditText
    private lateinit var tietComments: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //hide action bar
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        //instantiate view model
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

        //create a list of categories
        val categories = mutableListOf<String>()
        TransactionCategory.values().forEach {
            categories.add(it.name)
        }
        //add the list of categories to spinner
        val categoryAdapter =
            ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, categories)
        spinnerCategory.adapter = categoryAdapter

        //create a list of transaction modes
        val transactionModes = mutableListOf<String>()
        TransactionMode.values().forEach {
            transactionModes.add(it.name)
        }
        //add the list of modes to spinner
        val transactionModeAdapter =
            ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, transactionModes)
        spinnerTransactionType.adapter = transactionModeAdapter

        //this button is clicked if transaction is of expense type
        btnExpense.setOnClickListener {
            combineData(TransactionType.Expense)
        }

        //this button is clicked if transaction is of income type
        btnIncome.setOnClickListener {
            combineData(TransactionType.Income)
        }

        //show date picker fragment to select the required date
        tietTransactionDate.setOnClickListener {
            showDatePicker(1)
        }

        //show date picker fragment to select the required date
        tietFrom.setOnClickListener {
            showDatePicker(2)
        }

        //show date picker fragment to select the required date
        tietTo.setOnClickListener {
            showDatePicker(3)
        }
    }

    private fun showDatePicker(dateId: Int) {
        //create an object of date picker fragment
        val dateFragment = DatePickerFragment()

        //pass the current date to the date picker fragment
        val calendar = Calendar.getInstance()
        val args = Bundle()
        args.putInt("year", calendar.get(Calendar.YEAR))
        args.putInt("month", calendar.get(Calendar.MONTH))
        args.putInt("day", calendar.get(Calendar.DAY_OF_MONTH))
        dateFragment.arguments = args

        /*
        integer is passed on button click to check which edit text was clicked and hence show the
        selected in it
         */
        when (dateId) {
            1 -> dateFragment.setCallBack(onDate1)
            2 -> dateFragment.setCallBack(onDate2)
            3 -> dateFragment.setCallBack(onDate3)
        }

        //show the date picker fragment
        fragmentManager?.let { dateFragment.show(it, "Date Picker") }
    }

    //get the selected date and show it in appropriate edit text
    private val onDate1: DatePickerDialog.OnDateSetListener =
        DatePickerDialog.OnDateSetListener { viewDate, year, monthOfYear, dayOfMonth ->
            transactionDate = getSelectedDate(dayOfMonth, monthOfYear, year)
            tietTransactionDate.setText(transactionDate.readableFormat())
        }

    //get the selected date and show it in appropriate edit text
    private val onDate2: DatePickerDialog.OnDateSetListener =
        DatePickerDialog.OnDateSetListener { viewDate, year, monthOfYear, dayOfMonth ->
            fromDate = getSelectedDate(dayOfMonth, monthOfYear, year)
            tietFrom.setText(fromDate.readableFormat())
        }

    //get the selected date and show it in appropriate edit text
    private val onDate3: DatePickerDialog.OnDateSetListener =
        DatePickerDialog.OnDateSetListener { viewDate, year, monthOfYear, dayOfMonth ->
            toDate = getSelectedDate(dayOfMonth, monthOfYear, year)
            tietTo.setText(toDate.readableFormat())
        }

    //convert the day, month and year to date object
    private fun getSelectedDate(day: Int, month: Int, year: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        return calendar.time
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //get the transactionId from arguments
        val transactionId = AddTransactionFragmentArgs.fromBundle(requireArguments()).transactionId
        viewModel.setTransactionId(transactionId) //set the transactionId in view model

        //get the type of transaction (past/upcoming) from arguments
        val type = AddTransactionFragmentArgs.fromBundle(requireArguments()).type

        cbRecurringTransaction.setOnClickListener {
            //check if check box is checked or not
            recurring = cbRecurringTransaction.isChecked

            /*
            if the transaction is recurring, then show the from, to and recurring period views.
            Otherwise, hide them.
             */
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

        /*
        If the transaction is of type 1 ie upcoming transaction, observe the upcoming transaction.
        If the transaction is of type 2 ie past transaction, observe the past transaction.
         */
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
        //set the checked box to checked as the transaction is recurring and disable it
        cbRecurringTransaction.isChecked = true
        cbRecurringTransaction.isEnabled = false
        recurring = true
        //make the from, to and recurring period edit text views visible
        tilFrom.visibility = View.VISIBLE
        tilTo.visibility = View.VISIBLE
        tilRecurringPeriod.visibility = View.VISIBLE

        //update the views with the upcoming transaction data
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
        //set the checked box to not checked as the transaction is not recurring and disable it
        cbRecurringTransaction.isChecked = false
        cbRecurringTransaction.isEnabled = false
        recurring = false

        //make the from, to and recurring period edit text views invisible
        tilFrom.visibility = View.GONE
        tilTo.visibility = View.GONE
        tilRecurringPeriod.visibility = View.GONE

        //update the views with the past transaction data
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
        //if the edit text is made empty, then show the error
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

        //If the focus is changed from edit text, then show the error if it is empty
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
        /*
        If any edit text has been left empty, then show the error message in toast
         */
        if (tietTransactionName.text.isEmpty() || tietAmount.text.isEmpty() || tietTransactionDate.text.isEmpty() || tietComments.text.isEmpty())
            Toast.makeText(requireContext(), "Please fill all fields!", Toast.LENGTH_LONG).show()
        else
            if (recurring) //upcoming transaction
                if (tietFrom.text.isEmpty() || tietTo.text.isEmpty() || tietRecurringPeriod.text.isEmpty())
                    Toast.makeText(requireContext(), "Please fill all fields!", Toast.LENGTH_LONG)
                        .show()
                else {
                    //create upcoming transaction object
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
                    //add or update it in database
                    viewModel.insertUpcomingTransaction(upcomingTransaction)

                    //insertMonth(transactionDate) //transaction date
                    //insertMonth(fromDate) //transaction from date

                    //navigate to tab fragment
                    findNavController().navigate(R.id.action_addTransaction_to_tab)
                }
            else { //past transaction
                //create past transaction object
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
                //add or update it in database
                viewModel.insertPastTransaction(pastTransaction)

                //insert month in database
                insertMonth(transactionDate) //transaction date

                //navigate to tab fragment
                findNavController().navigate(R.id.action_addTransaction_to_tab)
            }
    }

    private fun insertMonth(date: Date) {
        //make month id from date
        val dateFormat = SimpleDateFormat("MMyyyy", Locale.getDefault())
        val monthId: String = dateFormat.format(date)

        //get the monthly budget set by the user
        val sharedPreferences =
            requireActivity().getSharedPreferences("EXPENSE_MANAGER", Context.MODE_PRIVATE)
        val budget = sharedPreferences.getFloat("MONTHLY_BUDGET", 1000F)

        //make month object
        val monthData = Month(monthId, budget)

        //add month to database if it doesn't exist already
        viewModel.insertMonth(monthData)
    }
}