package com.sahilmehra.expensemanager.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

enum class TransactionCategory {
    Personal,
    Business
}

enum class TransactionMode {
    Cash,
    Card,
    Cheque,
    Others
}

enum class TransactionType {
    Expense,
    Income
}

@Entity(tableName = "past_transactions")
data class PastTransactions(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val amount: Int,
    val date: Date,
    val comments: String,
    val category: TransactionCategory,
    val mode: TransactionMode,
    val type: TransactionType
)

@Entity(tableName = "upcoming_transactions")
data class UpcomingTransactions(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val amount: Int,
    val date: Date,
    val from: Date,
    val to: Date,
    @ColumnInfo(name = "recurring_period") val recurringPeriod: Int,
    val comments: String,
    val category: TransactionCategory,
    val mode: TransactionMode,
    val type: TransactionType
)

@Entity(tableName = "month_data")
data class Month(
    @PrimaryKey val month: Int,
    val budget: Int
)