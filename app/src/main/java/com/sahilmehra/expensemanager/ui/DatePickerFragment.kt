package com.sahilmehra.expensemanager.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class DatePickerFragment : DialogFragment() {
    lateinit var onDateSet: DatePickerDialog.OnDateSetListener
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0

    fun setCallBack(onDate: DatePickerDialog.OnDateSetListener) {
        onDateSet = onDate
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)

        if (args != null) {
            year = args.getInt("year")
            month = args.getInt("month")
            day = args.getInt("day")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DatePickerDialog(requireActivity(), onDateSet, year, month, day)
    }
}