package com.sahilmehra.expensemanager.data

import android.app.Application
import androidx.lifecycle.LiveData
import java.util.*

class TransactionRepository(context: Application) {
    private val transactionDao: TransactionDao = Database.getDatabase(context).getTransactionDao()

    fun getUpcomingTransactions(): LiveData<List<UpcomingTransaction>> {
        return transactionDao.getUpcomingTransactions()
    }

    fun getPastTransactions(): LiveData<List<PastTransaction>> =
        transactionDao.getPastTransactions()

    fun getPastTransaction(id: Long): LiveData<PastTransaction> =
        transactionDao.getPastTransaction(id)

    fun getUpcomingTransaction(id: Long): LiveData<UpcomingTransaction> =
        transactionDao.getUpcomingTransaction(id)

    fun getAmountByMode(transactionMode: Int): LiveData<Float> =
        transactionDao.getAmountByMode(transactionMode)

    fun getPersonalUpcomingTransactions(): LiveData<List<UpcomingTransaction>> =
        transactionDao.getPersonalUpcomingTransactions()

    fun getBusinessUpcomingTransactions(): LiveData<List<UpcomingTransaction>> =
        transactionDao.getBusinessUpcomingTransactions()

    fun getPersonalPastTransactions(): LiveData<List<PastTransaction>> =
        transactionDao.getPersonalPastTransaction()

    fun getBusinessPastTransactions(): LiveData<List<PastTransaction>> =
        transactionDao.getBusinessPastTransaction()

    fun getMonths(): LiveData<List<Month>> = transactionDao.getMonths()

    fun getPastTransactionsByMonth(from: Date, to: Date): LiveData<List<PastTransaction>> =
        transactionDao.getPastTransactionsByMonth(from, to)

    suspend fun getPastTransactionsByMonthTemp(from: Date, to: Date): List<PastTransaction> =
        transactionDao.getPastTransactionsByMonthTemp(from, to)

    fun getExpenseByMonth(from: Date, to: Date): LiveData<Float> =
        transactionDao.getExpenseByMonth(from, to)

    fun getIncomeByMonth(from: Date, to: Date): LiveData<Float> =
        transactionDao.getIncomeByMonth(from, to)

    suspend fun insertPastTransaction(pastTransaction: PastTransaction) =
        transactionDao.insertPastTransaction(pastTransaction)

    suspend fun insertUpcomingTransaction(upcomingTransaction: UpcomingTransaction) =
        transactionDao.insertUpcomingTransaction(upcomingTransaction)

    suspend fun insertMonth(month: Month) = transactionDao.insertMonth(month)

    suspend fun updateUpcomingTransaction(upcomingTransaction: UpcomingTransaction) =
        transactionDao.updateUpcomingTransaction(upcomingTransaction)

    suspend fun updatePastTransaction(pastTransaction: PastTransaction) =
        transactionDao.updatePastTransaction(pastTransaction)

    suspend fun deleteUpcomingTransaction(upcomingTransaction: UpcomingTransaction) =
        transactionDao.deleteUpcomingTransaction(upcomingTransaction)

    suspend fun deletePastTransaction(pastTransaction: PastTransaction) =
        transactionDao.deletePastTransaction(pastTransaction)
}