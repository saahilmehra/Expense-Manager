package com.sahilmehra.expensemanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(DbTypeConverters::class)
@Database(
    entities = [UpcomingTransactions::class, PastTransactions::class, Month::class],
    version = 1
)
abstract class Database : RoomDatabase() {
    abstract fun getTransactionDao(): TransactionDao

    companion object {
        @Volatile
        private var instance: com.sahilmehra.expensemanager.data.Database? = null

        fun getDatabase(context: Context) = instance
            ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    com.sahilmehra.expensemanager.data.Database::class.java,
                    "em_transaction_database"
                ).build().also { instance = it }
            }
    }
}