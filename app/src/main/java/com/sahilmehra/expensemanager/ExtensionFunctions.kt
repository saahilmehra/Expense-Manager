package com.sahilmehra.expensemanager

import java.text.SimpleDateFormat
import java.util.*

fun Date.readableFormat(): String {
    val dateFormat = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault())
    return dateFormat.format(this)
}

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
