package com.sahilmehra.expensemanager

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

//convert date to specific format
fun Date.readableFormat(): String {
    val dateFormat = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault())
    return dateFormat.format(this)
}

//get first day of month from monthId
fun String.firstDayOfMonth(): Date {
    val startDateString = "01$this"
    val dateFormat = SimpleDateFormat("ddMMyyyy")
    val convertedDate: Date = dateFormat.parse(startDateString)
    Log.e("startDateStringPast", convertedDate.toString())

    val calendar = Calendar.getInstance()

    calendar.time = convertedDate

    return calendar.time
}

//get last day of month from monthId
fun String.lastDayOfMonth(): Date {
    val startDateString = "01$this"
    val dateFormat = SimpleDateFormat("ddMMyyyy")
    val convertedDate: Date = dateFormat.parse(startDateString)

    val calendar = Calendar.getInstance()
    calendar.time = convertedDate
    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))

    return calendar.time
}

//get month name from month number
fun String.monthName(): String =
    when (this) {
        "01" -> "January"
        "02" -> "February"
        "03" -> "March"
        "04" -> "April"
        "05" -> "May"
        "06" -> "June"
        "07" -> "July"
        "08" -> "August"
        "09" -> "September"
        "10" -> "October"
        "11" -> "November"
        "12" -> "December"
        else -> "Month"
    }
