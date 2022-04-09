package developer.mihailzharkovskiy.stepcounter.data.room.type_converter

import androidx.room.TypeConverter
import java.util.*

class TypeConverterForRoom {
    @TypeConverter
    fun fromDateToLong(date: Date): Long = date.time

    @TypeConverter
    fun fromLongToDate(seconds: Long): Date = Date(seconds)
}