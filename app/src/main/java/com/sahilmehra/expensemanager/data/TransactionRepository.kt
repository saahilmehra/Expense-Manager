package com.sahilmehra.expensemanager.data

import android.app.Application
import androidx.lifecycle.LiveData

class TransactionRepository(context: Application) {
    private val transactionDao: TransactionDao = Database.getDatabase(context).getTransactionDao()

    fun getUpcomingTransactions(): LiveData<List<UpcomingTransaction>> {
        return transactionDao.getUpcomingTransactions()
    }

    fun getPastTransactions(): LiveData<List<PastTransaction>> = transactionDao.getPastTransaction()

    fun getUpcomingTransactionsByCategory(category: Int): LiveData<List<UpcomingTransaction>> =
        transactionDao.getUpcomingTransactionsByCategory(category)

    fun getPastTransactionsByCategory(category: Int): LiveData<List<PastTransaction>> =
        transactionDao.getPastTransactionsByCategory(category)

    suspend fun insertPastTransaction(pastTransaction: PastTransaction) =
        transactionDao.insertPastTransaction(pastTransaction)

    suspend fun insertUpcomingTransaction(upcomingTransaction: UpcomingTransaction) =
        transactionDao.insertUpcomingTransaction(upcomingTransaction)

    suspend fun updateUpcomingTransaction(upcomingTransaction: UpcomingTransaction) =
        transactionDao.updateUpcomingTransaction(upcomingTransaction)

    suspend fun updatePastTransaction(pastTransaction: PastTransaction) =
        transactionDao.updatePastTransaction(pastTransaction)

    suspend fun deleteUpcomingTransaction(upcomingTransaction: UpcomingTransaction) =
        transactionDao.deleteUpcomingTransaction(upcomingTransaction)

    suspend fun deletePastTransaction(pastTransaction: PastTransaction) =
        transactionDao.deletePastTransaction(pastTransaction)
}