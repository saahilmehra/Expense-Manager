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

@Entity(tableName = "past_transaction")
data class PastTransaction(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val amount: Float,
    val date: Date,
    val comments: String,
    val category: Int,
    val mode: Int,
    val type: Int
)

@Entity(tableName = "upcoming_transaction")
data class UpcomingTransaction(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val amount: Float,
    val date: Date,
    val from: Date,
    val to: Date,
    @ColumnInfo(name = "recurring_period") val recurringPeriod: Int,
    val comments: String,
    val category: Int,
    val mode: Int,
    val type: Int
)

@Entity(tableName = "month_data")
data class Month(
    @PrimaryKey val id: String,
    val budget: Float
)