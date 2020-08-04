package com.sahilmehra.expensemanager.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.sahilmehra.expensemanager.data.*
import com.sahilmehra.expensemanager.firstDayOfMonth
import com.sahilmehra.expensemanager.lastDayOfMonth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: TransactionRepository = TransactionRepository(application)

    val upcomingTransactions: LiveData<List<UpcomingTransaction>> = repo.getUpcomingTransactions()

    val pastTransactions: LiveData<List<PastTransaction>> = repo.getPastTransactions()

    val amountInCash: LiveData<Float> = repo.getAmountByMode(TransactionMode.Cash.ordinal)
    val amountInCard: LiveData<Float> = repo.getAmountByMode(TransactionMode.Card.ordinal)
    val amountInCheque: LiveData<Float> = repo.getAmountByMode(TransactionMode.Cheque.ordinal)
    val amountInOthers: LiveData<Float> = repo.getAmountByMode(TransactionMode.Others.ordinal)

    val totalIncome: LiveData<Float> = repo.getAmountByType(TransactionType.Income.ordinal)
    val totalExpense: LiveData<Float> = repo.getAmountByType(TransactionType.Expense.ordinal)

    val personalUpcomingTransactions: LiveData<List<UpcomingTransaction>> =
        repo.getPersonalUpcomingTransactions()
    val personalPastTransactions: LiveData<List<PastTransaction>> =
        repo.getPersonalPastTransactions()
    val businessUpcomingTransactions: LiveData<List<UpcomingTransaction>> =
        repo.getBusinessUpcomingTransactions()
    val businessPastTransactions: LiveData<List<PastTransaction>> =
        repo.getBusinessPastTransactions()

    val months: LiveData<List<Month>> = repo.getMonths()

    private val _monthId = MutableLiveData<String>("")
    val monthId: LiveData<String>
        get() = _monthId

    val expenseByMonth: LiveData<Float> = Transformations.switchMap(_monthId) { id ->
        val startDate = id.firstDayOfMonth()
        val endDate = id.lastDayOfMonth()

        repo.getExpenseByMonth(startDate, endDate)
    }

    val incomeByMonth: LiveData<Float> = Transformations.switchMap(_monthId) { id ->
        val startDate = id.firstDayOfMonth()
        val endDate = id.lastDayOfMonth()

        repo.getIncomeByMonth(startDate, endDate)
    }

    val pastTransactionsByMonth: LiveData<List<PastTransaction>> =
        Transformations.switchMap(_monthId) { id ->
            val startDate = id.firstDayOfMonth()
            val endDate = id.lastDayOfMonth()

            repo.getPastTransactionsByMonth(startDate, endDate)
        }

    private val _transactionId = MutableLiveData<Long>(0)
    val transactionId: LiveData<Long>
        get() = _transactionId

    val pastTransaction: LiveData<PastTransaction> =
        Transformations.switchMap(_transactionId) { id ->
            repo.getPastTransaction(id)
        }

    val upcomingTransaction: LiveData<UpcomingTransaction> =
        Transformations.switchMap(_transactionId) { id ->
            repo.getUpcomingTransaction(id)
        }

    fun insertPastTransaction(transaction: PastTransaction) {
        viewModelScope.launch(Dispatchers.IO) {
            if (_transactionId.value == 0L)
                repo.insertPastTransaction(transaction)
            else
                repo.updatePastTransaction(transaction)
        }
    }

    fun insertUpcomingTransaction(transaction: UpcomingTransaction) {
        viewModelScope.launch(Dispatchers.IO) {
            if (_transactionId.value == 0L)
                repo.insertUpcomingTransaction(transaction)
            else
                repo.updateUpcomingTransaction(transaction)
        }
    }

    fun insertMonth(month: Month) {
        viewModelScope.launch {
            repo.insertMonth(month)
        }
    }

    fun setMonthId(id: String) {
        if (monthId.value != id)
            _monthId.value = id
    }

    fun setTransactionId(id: Long) {
        if (transactionId.value != id)
            _transactionId.value = id
    }

    fun deletePastTransaction(pastTransaction: PastTransaction) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deletePastTransaction(pastTransaction)
        }
    }

    fun deleteUpcomingTransaction(upcomingTransaction: UpcomingTransaction) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteUpcomingTransaction(upcomingTransaction)
        }
    }

    suspend fun getTrans(monthId: String): List<PastTransaction> {
        val startDate = monthId.firstDayOfMonth()
        val endDate = monthId.lastDayOfMonth()

        return repo.getPastTransactionsByMonthTemp(startDate, endDate)
    }

    suspend fun getExpenseTemp(monthId: String): Float {
        val startDate = monthId.firstDayOfMonth()
        val endDate = monthId.lastDayOfMonth()

        return repo.getExpenseByMonthTemp(startDate, endDate)
    }
}