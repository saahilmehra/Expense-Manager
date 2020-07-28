package com.sahilmehra.expensemanager.data

import androidx.lifecycle.LiveData

data class MonthTemp(
    val id: String,
    val pastTransactions: LiveData<List<PastTransaction>>
)

