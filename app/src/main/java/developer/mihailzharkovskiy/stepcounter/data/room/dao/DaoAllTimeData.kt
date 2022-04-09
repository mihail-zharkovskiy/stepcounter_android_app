package developer.mihailzharkovskiy.stepcounter.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import developer.mihailzharkovskiy.stepcounter.data.room.entity.StepAllTimeDataEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*

@Dao
interface DaoAllTimeData {

    @Query("SELECT * FROM table_steps_data_for_specific_day WHERE date = :date LIMIT 1")
    fun getLastSaveData(
        date: String = SimpleDateFormat("EE\ndd.MM", Locale.getDefault()).format(Date()),
    ): StepAllTimeDataEntity?

    @Query("SELECT * FROM table_steps_data_for_specific_day")
    fun getAllTimeData(): Flow<List<StepAllTimeDataEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveData(data: StepAllTimeDataEntity)

    @Query("DELETE FROM table_steps_data_for_specific_day")
    suspend fun deleteAll()

    @Query("SELECT * FROM table_steps_data_for_specific_day LIMIT :howManyDays")
    suspend fun getDataForSpecificTime(howManyDays: Int): List<StepAllTimeDataEntity>

}