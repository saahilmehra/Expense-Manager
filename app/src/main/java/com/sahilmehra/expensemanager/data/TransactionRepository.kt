package com.sahilmehra.expensemanager.data

import android.app.Application
import androidx.lifecycle.LiveData

class TransactionRepository(context: Application) {
    private val transactionDao: TransactionDao = Database.getDatabase(context).getTransactionDao()

    fun getUpcomingTransactions(): LiveData<List<UpcomingTransactions>> {
        return transactionDao.getUpcomingTransactions()
    }

    fun getPastTransaction(): LiveData<List<PastTransactions>> = transactionDao.getPastTransaction()

    fun getUpcomingTransactionsByCategory(category: TransactionCategory): LiveData<List<UpcomingTransactions>> =
        transactionDao.getUpcomingTransactionsByCategory(category)

    fun getPastTransactionsByCategory(category: TransactionCategory): LiveData<List<PastTransactions>> =
        transactionDao.getPastTransactionsByCategory(category)

    suspend fun insertPastTransaction(pastTransactions: PastTransactions) =
        transactionDao.insertPastTransaction(pastTransactions)

    suspend fun insertUpcomingTransaction(upcomingTransactions: UpcomingTransactions) =
        transactionDao.insertUpcomingTransaction(upcomingTransactions)

    suspend fun updateUpcomingTransaction(upcomingTransactions: UpcomingTransactions) =
        transactionDao.updateUpcomingTransaction(upcomingTransactions)

    suspend fun updatePastTransaction(pastTransactions: PastTransactions) =
        transactionDao.updatePastTransaction(pastTransactions)

    suspend fun deleteUpcomingTransaction(upcomingTransactions: UpcomingTransactions) =
        transactionDao.deleteUpcomingTransaction(upcomingTransactions)

    suspend fun deletePastTransaction(pastTransactions: PastTransactions) =
        transactionDao.deletePastTransaction(pastTransactions)
}