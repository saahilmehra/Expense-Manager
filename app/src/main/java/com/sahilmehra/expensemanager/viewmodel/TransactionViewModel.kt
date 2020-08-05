package com.sahilmehra.expensemanager.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.sahilmehra.expensemanager.data.*
import com.sahilmehra.expensemanager.firstDayOfMonth
import com.sahilmehra.expensemanager.lastDayOfMonth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    //create an object of repository
    private val repo: TransactionRepository = TransactionRepository(application)

    //get all upcoming transactions
    val upcomingTransactions: LiveData<List<UpcomingTransaction>> = repo.getUpcomingTransactions()

    //get all past transactions
    val pastTransactions: LiveData<List<PastTransaction>> = repo.getPastTransactions()

    //get amount earned through different modes
    val amountInCash: LiveData<Float> = repo.getAmountByMode(TransactionMode.Cash.ordinal)
    val amountInCard: LiveData<Float> = repo.getAmountByMode(TransactionMode.Card.ordinal)
    val amountInCheque: LiveData<Float> = repo.getAmountByMode(TransactionMode.Cheque.ordinal)
    val amountInOthers: LiveData<Float> = repo.getAmountByMode(TransactionMode.Others.ordinal)

    //get total amount earned
    val totalIncome: LiveData<Float> = repo.getAmountByType(TransactionType.Income.ordinal)

    //get total amount spent
    val totalExpense: LiveData<Float> = repo.getAmountByType(TransactionType.Expense.ordinal)

    //get boh personal and business past and upcoming transactions
    val personalUpcomingTransactions: LiveData<List<UpcomingTransaction>> =
        repo.getPersonalUpcomingTransactions()
    val personalPastTransactions: LiveData<List<PastTransaction>> =
        repo.getPersonalPastTransactions()
    val businessUpcomingTransactions: LiveData<List<UpcomingTransaction>> =
        repo.getBusinessUpcomingTransactions()
    val businessPastTransactions: LiveData<List<PastTransaction>> =
        repo.getBusinessPastTransactions()

    //get months stored
    val months: LiveData<List<Month>> = repo.getMonths()

    //stores the current monthId
    private val _monthId = MutableLiveData<String>("")
    val monthId: LiveData<String>
        get() = _monthId

    //amount spent in a particular month
    val expenseByMonth: LiveData<Float> = Transformations.switchMap(_monthId) { id ->
        val startDate = id.firstDayOfMonth()
        val endDate = id.lastDayOfMonth()

        repo.getExpenseByMonth(startDate, endDate)
    }

    //amount earned in a particular month
    val incomeByMonth: LiveData<Float> = Transformations.switchMap(_monthId) { id ->
        val startDate = id.firstDayOfMonth()
        val endDate = id.lastDayOfMonth()

        repo.getIncomeByMonth(startDate, endDate)
    }

    //get all past transactions of a month
    val pastTransactionsByMonth: LiveData<List<PastTransaction>> =
        Transformations.switchMap(_monthId) { id ->
            val startDate = id.firstDayOfMonth()
            val endDate = id.lastDayOfMonth()

            repo.getPastTransactionsByMonth(startDate, endDate)
        }

    //stores the current transactionId
    private val _transactionId = MutableLiveData<Long>(0)
    val transactionId: LiveData<Long>
        get() = _transactionId

    //get a particular pastTransaction
    val pastTransaction: LiveData<PastTransaction> =
        Transformations.switchMap(_transactionId) { id ->
            repo.getPastTransaction(id)
        }

    //get a particular upcoming transaction
    val upcomingTransaction: LiveData<UpcomingTransaction> =
        Transformations.switchMap(_transactionId) { id ->
            repo.getUpcomingTransaction(id)
        }

    //insert or update past transaction
    fun insertPastTransaction(transaction: PastTransaction) {
        viewModelScope.launch(Dispatchers.IO) {
            //if transactionId is 0, save as new transaction. Otherwise, update the current transaction
            if (_transactionId.value == 0L)
                repo.insertPastTransaction(transaction)
            else
                repo.updatePastTransaction(transaction)
        }
    }

    //insert or update upcoming transaction
    fun insertUpcomingTransaction(transaction: UpcomingTransaction) {
        viewModelScope.launch(Dispatchers.IO) {
            //if transactionId is 0, save as new transaction. Otherwise, update the current transaction
            if (_transactionId.value == 0L)
                repo.insertUpcomingTransaction(transaction)
            else
                repo.updateUpcomingTransaction(transaction)
        }
    }

    //save a month in database
    fun insertMonth(month: Month) {
        viewModelScope.launch {
            repo.insertMonth(month)
        }
    }

    //update the current monthId
    fun setMonthId(id: String) {
        if (monthId.value != id)
            _monthId.value = id
    }

    //update the current transactionId
    fun setTransactionId(id: Long) {
        if (transactionId.value != id)
            _transactionId.value = id
    }

    //delete a past transaction
    fun deletePastTransaction(pastTransaction: PastTransaction) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deletePastTransaction(pastTransaction)
        }
    }

    //delete an upcoming transaction
    fun deleteUpcomingTransaction(upcomingTransaction: UpcomingTransaction) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteUpcomingTransaction(upcomingTransaction)
        }
    }

    //get all past transactions of a particular month
    suspend fun getTrans(monthId: String): List<PastTransaction> {
        val startDate = monthId.firstDayOfMonth()
        val endDate = monthId.lastDayOfMonth()

        return repo.getPastTransactionsByMonthTemp(startDate, endDate)
    }

    //amount spent in a particular month
    suspend fun getExpenseTemp(monthId: String): Float {
        val startDate = monthId.firstDayOfMonth()
        val endDate = monthId.lastDayOfMonth()

        return repo.getExpenseByMonthTemp(startDate, endDate)
    }
}