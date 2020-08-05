package com.sahilmehra.expensemanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(DbTypeConverters::class) //for date
@Database(
    entities = [UpcomingTransaction::class, PastTransaction::class, Month::class],
    version = 1
)
abstract class Database : RoomDatabase() {
    //dao for database operations
    abstract fun getTransactionDao(): TransactionDao

    //creates a single instance of database for the whole app to avoid memory usage
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