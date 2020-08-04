package com.sahilmehra.expensemanager.data

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface TransactionDao {
    @Query("SELECT * FROM upcoming_transaction")
    fun getUpcomingTransactions(): LiveData<List<UpcomingTransaction>>

    @Query("SELECT * FROM past_transaction")
    fun getPastTransactions(): LiveData<List<PastTransaction>>

    @Query("SELECT * FROM past_transaction where id= :id")
    fun getPastTransaction(id: Long): LiveData<PastTransaction>

    @Query("SELECT * FROM upcoming_transaction where id= :id")
    fun getUpcomingTransaction(id: Long): LiveData<UpcomingTransaction>

    @Query("SELECT SUM(amount) FROM past_transaction WHERE mode= :transactionMode AND type=1")
    fun getAmountByMode(transactionMode: Int): LiveData<Float>

    @Query("SELECT SUM(amount) FROM past_transaction WHERE type= :type")
    fun getAmountByType(type: Int): LiveData<Float>

    @Query("SELECT * FROM upcoming_transaction WHERE category=0")
    fun getPersonalUpcomingTransactions(): LiveData<List<UpcomingTransaction>>

    @Query("SELECT * FROM upcoming_transaction WHERE category=1")
    fun getBusinessUpcomingTransactions(): LiveData<List<UpcomingTransaction>>

    @Query("SELECT * FROM past_transaction WHERE category=0")
    fun getPersonalPastTransaction(): LiveData<List<PastTransaction>>

    @Query("SELECT * FROM past_transaction WHERE category=1")
    fun getBusinessPastTransaction(): LiveData<List<PastTransaction>>

    @Query("SELECT * FROM month_data")
    fun getMonths(): LiveData<List<Month>>

    @Query("SELECT * FROM past_transaction WHERE date BETWEEN :from AND :to")
    fun getPastTransactionsByMonth(from: Date, to: Date): LiveData<List<PastTransaction>>

    @Query("SELECT * FROM past_transaction WHERE date BETWEEN :from AND :to")
    suspend fun getPastTransactionsByMonthTemp(from: Date, to: Date): List<PastTransaction>

    @Query("SELECT SUM(amount) FROM past_transaction WHERE date BETWEEN :from AND :to AND type=0")
    fun getExpenseByMonth(from: Date, to: Date): LiveData<Float>

    @Query("SELECT SUM(amount) FROM past_transaction WHERE date BETWEEN :from AND :to AND type=1")
    fun getIncomeByMonth(from: Date, to: Date): LiveData<Float>

    @Query("SELECT SUM(amount) FROM past_transaction WHERE date BETWEEN :from AND :to AND type=0")
    suspend fun getExpenseByMonthTemp(from: Date, to: Date): Float

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPastTransaction(pastTransactions: PastTransaction)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUpcomingTransaction(upcomingTransactions: UpcomingTransaction)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMonth(month: Month)

    @Update
    suspend fun updateUpcomingTransaction(upcomingTransaction: UpcomingTransaction)

    @Update
    suspend fun updatePastTransaction(pastTransaction: PastTransaction)

    @Delete
    suspend fun deleteUpcomingTransaction(upcomingTransaction: UpcomingTransaction)

    @Delete
    suspend fun deletePastTransaction(pastTransaction: PastTransaction)
}