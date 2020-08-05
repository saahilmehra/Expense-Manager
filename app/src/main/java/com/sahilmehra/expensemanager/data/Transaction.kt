package com.sahilmehra.expensemanager.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

//user can choose between personal and business transactions
enum class TransactionCategory {
    Personal,
    Business
}

//user can choose between following different modes of transaction
enum class TransactionMode {
    Cash,
    Card,
    Cheque,
    Others
}

//to mark a transaction as expense or income
enum class TransactionType {
    Expense,
    Income
}

//table to store past transactions
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

//table to store upcoming transactions
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

/*
table to store months
id will be month number + year
eg- July, 2020 will have id as 072020
 */
@Entity(tableName = "month_data")
data class Month(
    @PrimaryKey val id: String,
    val budget: Float
)