package com.sahilmehra.expensemanager.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TransactionDao {
    @Query("SELECT * FROM upcoming_transactions")
    fun getUpcomingTransactions(): LiveData<List<UpcomingTransactions>>

    @Query("SELECT * FROM past_transactions ORDER BY date")
    fun getPastTransaction(): LiveData<List<PastTransactions>>

    @Query("SELECT * from upcoming_transactions WHERE category= :category")
    fun getUpcomingTransactionsByCategory(category: TransactionCategory): LiveData<List<UpcomingTransactions>>

    @Query("SELECT * from past_transactions WHERE category= :category")
    fun getPastTransactionsByCategory(category: TransactionCategory): LiveData<List<PastTransactions>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPastTransaction(pastTransactions: PastTransactions)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUpcomingTransaction(upcomingTransactions: UpcomingTransactions)

    @Update
    suspend fun updateUpcomingTransaction(upcomingTransactions: UpcomingTransactions)

    @Update
    suspend fun updatePastTransaction(pastTransactions: PastTransactions)

    @Delete
    suspend fun deleteUpcomingTransaction(upcomingTransactions: UpcomingTransactions)

    @Delete
    suspend fun deletePastTransaction(pastTransactions: PastTransactions)
}