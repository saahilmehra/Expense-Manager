package com.sahilmehra.expensemanager.data

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface TransactionDao {
    //get all upcoming transaction from database
    @Query("SELECT * FROM upcoming_transaction")
    fun getUpcomingTransactions(): LiveData<List<UpcomingTransaction>>

    //get all past transaction from database
    @Query("SELECT * FROM past_transaction")
    fun getPastTransactions(): LiveData<List<PastTransaction>>

    //get a particular upcoming transaction
    @Query("SELECT * FROM past_transaction where id= :id")
    fun getPastTransaction(id: Long): LiveData<PastTransaction>

    //get a particular past transaction
    @Query("SELECT * FROM upcoming_transaction where id= :id")
    fun getUpcomingTransaction(id: Long): LiveData<UpcomingTransaction>

    //get amount earned by each mode of transaction
    @Query("SELECT SUM(amount) FROM past_transaction WHERE mode= :transactionMode AND type=1")
    fun getAmountByMode(transactionMode: Int): LiveData<Float>

    //get total amount earned or total amount spent
    @Query("SELECT SUM(amount) FROM past_transaction WHERE type= :type")
    fun getAmountByType(type: Int): LiveData<Float>

    //get all personal upcoming transactions
    @Query("SELECT * FROM upcoming_transaction WHERE category=0")
    fun getPersonalUpcomingTransactions(): LiveData<List<UpcomingTransaction>>

    //get all business upcoming transactions
    @Query("SELECT * FROM upcoming_transaction WHERE category=1")
    fun getBusinessUpcomingTransactions(): LiveData<List<UpcomingTransaction>>

    //get all personal past transactions
    @Query("SELECT * FROM past_transaction WHERE category=0")
    fun getPersonalPastTransaction(): LiveData<List<PastTransaction>>

    //get all business past transactions
    @Query("SELECT * FROM past_transaction WHERE category=1")
    fun getBusinessPastTransaction(): LiveData<List<PastTransaction>>

    //get all months from database
    @Query("SELECT * FROM month_data")
    fun getMonths(): LiveData<List<Month>>

    //get past transactions of a particular month as live data
    @Query("SELECT * FROM past_transaction WHERE date BETWEEN :from AND :to")
    fun getPastTransactionsByMonth(from: Date, to: Date): LiveData<List<PastTransaction>>

    //get past transactions of a particular month as simple list
    @Query("SELECT * FROM past_transaction WHERE date BETWEEN :from AND :to")
    suspend fun getPastTransactionsByMonthTemp(from: Date, to: Date): List<PastTransaction>

    //get amount spent in a particular month as live data
    @Query("SELECT SUM(amount) FROM past_transaction WHERE date BETWEEN :from AND :to AND type=0")
    fun getExpenseByMonth(from: Date, to: Date): LiveData<Float>

    //get amount earned in a particular month as live data
    @Query("SELECT SUM(amount) FROM past_transaction WHERE date BETWEEN :from AND :to AND type=1")
    fun getIncomeByMonth(from: Date, to: Date): LiveData<Float>

    //get amount spent in a particular month as float
    @Query("SELECT SUM(amount) FROM past_transaction WHERE date BETWEEN :from AND :to AND type=0")
    suspend fun getExpenseByMonthTemp(from: Date, to: Date): Float

    //save a past transaction in database
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPastTransaction(pastTransactions: PastTransaction)

    //save an upcoming transaction in database
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUpcomingTransaction(upcomingTransactions: UpcomingTransaction)

    //save a month in database
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMonth(month: Month)

    //update an upcoming transaction
    @Update
    suspend fun updateUpcomingTransaction(upcomingTransaction: UpcomingTransaction)

    //update a past transaction
    @Update
    suspend fun updatePastTransaction(pastTransaction: PastTransaction)

    //delete an upcoming transaction
    @Delete
    suspend fun deleteUpcomingTransaction(upcomingTransaction: UpcomingTransaction)

    //delete a past transaction
    @Delete
    suspend fun deletePastTransaction(pastTransaction: PastTransaction)
}