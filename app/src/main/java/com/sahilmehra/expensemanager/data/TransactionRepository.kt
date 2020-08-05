package com.sahilmehra.expensemanager.data

import android.app.Application
import androidx.lifecycle.LiveData
import java.util.*

class TransactionRepository(context: Application) {
    //get object of dao
    private val transactionDao: TransactionDao = Database.getDatabase(context).getTransactionDao()

    //get all upcoming transaction from dao
    fun getUpcomingTransactions(): LiveData<List<UpcomingTransaction>> {
        return transactionDao.getUpcomingTransactions()
    }

    //get all past transaction from dao
    fun getPastTransactions(): LiveData<List<PastTransaction>> =
        transactionDao.getPastTransactions()

    //get a particular past transaction
    fun getPastTransaction(id: Long): LiveData<PastTransaction> =
        transactionDao.getPastTransaction(id)

    //get a particular upcoming transaction
    fun getUpcomingTransaction(id: Long): LiveData<UpcomingTransaction> =
        transactionDao.getUpcomingTransaction(id)

    //get amount earned by each mode of transaction
    fun getAmountByMode(transactionMode: Int): LiveData<Float> =
        transactionDao.getAmountByMode(transactionMode)

    //get total amount earned or total amount spent
    fun getAmountByType(type: Int): LiveData<Float> = transactionDao.getAmountByType(type)

    //get all personal upcoming transactions
    fun getPersonalUpcomingTransactions(): LiveData<List<UpcomingTransaction>> =
        transactionDao.getPersonalUpcomingTransactions()

    //get all business upcoming transactions
    fun getBusinessUpcomingTransactions(): LiveData<List<UpcomingTransaction>> =
        transactionDao.getBusinessUpcomingTransactions()

    //get all personal past transactions
    fun getPersonalPastTransactions(): LiveData<List<PastTransaction>> =
        transactionDao.getPersonalPastTransaction()

    //get all business past transactions
    fun getBusinessPastTransactions(): LiveData<List<PastTransaction>> =
        transactionDao.getBusinessPastTransaction()

    //get all months from database
    fun getMonths(): LiveData<List<Month>> = transactionDao.getMonths()

    //get past transactions of a particular month as live data
    fun getPastTransactionsByMonth(from: Date, to: Date): LiveData<List<PastTransaction>> =
        transactionDao.getPastTransactionsByMonth(from, to)

    //get past transactions of a particular month as simple list
    suspend fun getPastTransactionsByMonthTemp(from: Date, to: Date): List<PastTransaction> =
        transactionDao.getPastTransactionsByMonthTemp(from, to)

    //get amount spent in a particular month as live data
    fun getExpenseByMonth(from: Date, to: Date): LiveData<Float> =
        transactionDao.getExpenseByMonth(from, to)

    //get amount earned in a particular month as live data
    fun getIncomeByMonth(from: Date, to: Date): LiveData<Float> =
        transactionDao.getIncomeByMonth(from, to)

    //get amount spent in a particular month as float
    suspend fun getExpenseByMonthTemp(from: Date, to: Date): Float =
        transactionDao.getExpenseByMonthTemp(from, to)

    //save a past transaction in database
    suspend fun insertPastTransaction(pastTransaction: PastTransaction) =
        transactionDao.insertPastTransaction(pastTransaction)

    //save an upcoming transaction in database
    suspend fun insertUpcomingTransaction(upcomingTransaction: UpcomingTransaction) =
        transactionDao.insertUpcomingTransaction(upcomingTransaction)

    //save a month in database
    suspend fun insertMonth(month: Month) = transactionDao.insertMonth(month)

    //update an upcoming transaction
    suspend fun updateUpcomingTransaction(upcomingTransaction: UpcomingTransaction) =
        transactionDao.updateUpcomingTransaction(upcomingTransaction)

    //update a past transaction
    suspend fun updatePastTransaction(pastTransaction: PastTransaction) =
        transactionDao.updatePastTransaction(pastTransaction)

    //delete an upcoming transaction
    suspend fun deleteUpcomingTransaction(upcomingTransaction: UpcomingTransaction) =
        transactionDao.deleteUpcomingTransaction(upcomingTransaction)

    //delete a past transaction
    suspend fun deletePastTransaction(pastTransaction: PastTransaction) =
        transactionDao.deletePastTransaction(pastTransaction)
}