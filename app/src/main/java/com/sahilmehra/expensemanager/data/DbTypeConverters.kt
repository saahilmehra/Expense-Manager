package com.sahilmehra.expensemanager.data

import androidx.room.TypeConverter
import java.util.*

/*
Since sql-lite doesnot support date data-type, we convert date to long and store long in database.
When getting data from database, long is again converted to date for use
 */
class DbTypeConverters {
    //convert date to long
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    //convert long to date
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}