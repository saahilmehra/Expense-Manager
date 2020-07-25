package com.sahilmehra.expensemanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.sahilmehra.expensemanager.data.PastTransaction
import com.sahilmehra.expensemanager.data.TransactionRepository
import com.sahilmehra.expensemanager.data.UpcomingTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application):AndroidViewModel(application) {
    private val repo:TransactionRepository=TransactionRepository(application)

    val upcomingTransactions:LiveData<List<UpcomingTransaction>> = repo.getUpcomingTransactions()

    val pastTransactions:LiveData<List<PastTransaction>> = repo.getPastTransactions()

    fun insertPastTransaction(transaction: PastTransaction){
        viewModelScope.launch(Dispatchers.IO){
            repo.insertPastTransaction(transaction)
        }
    }

    fun insertUpcomingTransaction(transaction: UpcomingTransaction){
        viewModelScope.launch(Dispatchers.IO){
            repo.insertUpcomingTransaction(transaction)
        }
    }
}