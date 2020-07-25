package com.sahilmehra.expensemanager.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TransactionDao {
    @Query("SELECT * FROM upcoming_transaction")
    fun getUpcomingTransactions(): LiveData<List<UpcomingTransaction>>

    @Query("SELECT * FROM past_transaction")
    fun getPastTransaction(): LiveData<List<PastTransaction>>

    @Query("SELECT * from upcoming_transaction WHERE category= :category")
    fun getUpcomingTransactionsByCategory(category: Int): LiveData<List<UpcomingTransaction>>

    @Query("SELECT * from past_transaction WHERE category= :category")
    fun getPastTransactionsByCategory(category: Int): LiveData<List<PastTransaction>>

    @Query("SELECT SUM(amount) FROM past_transaction WHERE mode= :transactionMode AND type=1")
    fun getAmountByMode(transactionMode: Int): LiveData<Float>

    @Query("SELECT * FROM upcoming_transaction WHERE category=0")
    fun getPersonalUpcomingTransactions(): LiveData<List<UpcomingTransaction>>

    @Query("SELECT * FROM upcoming_transaction WHERE category=1")
    fun getBusinessUpcomingTransactions(): LiveData<List<UpcomingTransaction>>

    @Query("SELECT * FROM past_transaction WHERE category=0")
    fun getPersonalPastTransaction(): LiveData<List<PastTransaction>>

    @Query("SELECT * FROM past_transaction WHERE category=1")
    fun getBusinessPastTransaction(): LiveData<List<PastTransaction>>

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