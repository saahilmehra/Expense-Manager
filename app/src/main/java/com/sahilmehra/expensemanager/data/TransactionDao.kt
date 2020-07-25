package com.sahilmehra.expensemanager.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TransactionDao {
    @Query("SELECT * FROM upcoming_transaction")
    fun getUpcomingTransactions(): LiveData<List<UpcomingTransaction>>

    @Query("SELECT * FROM past_transaction ORDER BY date")
    fun getPastTransaction(): LiveData<List<PastTransaction>>

    @Query("SELECT * from upcoming_transaction WHERE category= :category")
    fun getUpcomingTransactionsByCategory(category: Int): LiveData<List<UpcomingTransaction>>

    @Query("SELECT * from past_transaction WHERE category= :category")
    fun getPastTransactionsByCategory(category: Int): LiveData<List<PastTransaction>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPastTransaction(pastTransactions: PastTransaction)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUpcomingTransaction(upcomingTransactions: UpcomingTransaction)

    @Update
    suspend fun updateUpcomingTransaction(upcomingTransaction: UpcomingTransaction)

    @Update
    suspend fun updatePastTransaction(pastTransaction: PastTransaction)

    @Delete
    suspend fun deleteUpcomingTransaction(upcomingTransaction: UpcomingTransaction)

    @Delete
    suspend fun deletePastTransaction(pastTransaction: PastTransaction)
}