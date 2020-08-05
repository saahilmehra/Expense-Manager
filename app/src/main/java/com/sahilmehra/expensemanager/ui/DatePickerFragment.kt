package com.sahilmehra.expensemanager.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class DatePickerFragment : DialogFragment() {
    //create object of OnDateSetListener
    lateinit var onDateSet: DatePickerDialog.OnDateSetListener
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0

    //to pass the value of OnDateSetListener object
    fun setCallBack(onDate: DatePickerDialog.OnDateSetListener) {
        onDateSet = onDate
    }

    //get the default value to be set in date picker fragment from arguments
    override fun setArguments(args: Bundle?) {
        super.setArguments(args)

        if (args != null) {
            year = args.getInt("year")
            month = args.getInt("month")
            day = args.getInt("day")
        }
    }

    //create the fragment
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DatePickerDialog(requireActivity(), onDateSet, year, month, day)
    }
}