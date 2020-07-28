package com.sahilmehra.expensemanager.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.sahilmehra.expensemanager.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: TransactionRepository = TransactionRepository(application)

    val upcomingTransactions: LiveData<List<UpcomingTransaction>> = repo.getUpcomingTransactions()

    val pastTransactions: LiveData<List<PastTransaction>> = repo.getPastTransactions()

    val amountInCash: LiveData<Float> = repo.getAmountByMode(TransactionMode.Cash.ordinal)
    val amountInCard: LiveData<Float> = repo.getAmountByMode(TransactionMode.Card.ordinal)
    val amountInCheque: LiveData<Float> = repo.getAmountByMode(TransactionMode.Cheque.ordinal)
    val amountInOthers: LiveData<Float> = repo.getAmountByMode(TransactionMode.Others.ordinal)

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
        val startDateString = "01$id"
        val dateFormat = SimpleDateFormat("ddMMyyyy")
        val convertedDate: Date = dateFormat.parse(startDateString)
        Log.e("startDateStringPast", convertedDate.toString())

        val calendar = Calendar.getInstance()

        calendar.time = convertedDate
        val startDate = calendar.time
        Log.e("startDatePast", startDate.toString())

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        val endDate = calendar.time
        Log.e("endDatePast", endDate.toString())

        repo.getExpenseByMonth(startDate, endDate)
    }

    val incomeByMonth: LiveData<Float> = Transformations.switchMap(_monthId) { id ->
        val startDateString = "01$id"
        val dateFormat = SimpleDateFormat("ddMMyyyy")
        val convertedDate: Date = dateFormat.parse(startDateString)
        Log.e("startDateStringPast", convertedDate.toString())

        val calendar = Calendar.getInstance()

        calendar.time = convertedDate
        val startDate = calendar.time
        Log.e("startDatePast", startDate.toString())

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        val endDate = calendar.time
        Log.e("endDatePast", endDate.toString())

        repo.getIncomeByMonth(startDate, endDate)
    }

    val pastTransactionsByMonth: LiveData<List<PastTransaction>> =
        Transformations.switchMap(_monthId) { id ->
            val startDateString = "01$id"
            val dateFormat = SimpleDateFormat("ddMMyyyy")
            val convertedDate: Date = dateFormat.parse(startDateString)
            Log.e("startDateStringPast", convertedDate.toString())

            val calendar = Calendar.getInstance()

            calendar.time = convertedDate
            val startDate = calendar.time
            Log.e("startDatePast", startDate.toString())

            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
            val endDate = calendar.time
            Log.e("endDatePast", endDate.toString())


            repo.getPastTransactionsByMonth(startDate, endDate)
        }

    fun insertPastTransaction(transaction: PastTransaction) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertPastTransaction(transaction)
        }
    }

    fun insertUpcomingTransaction(transaction: UpcomingTransaction) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertUpcomingTransaction(transaction)
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
}