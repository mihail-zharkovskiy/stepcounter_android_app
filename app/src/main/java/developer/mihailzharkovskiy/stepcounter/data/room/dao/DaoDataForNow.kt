package developer.mihailzharkovskiy.stepcounter.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import developer.mihailzharkovskiy.stepcounter.data.room.entity.StepForNowDataEntity

@Dao
interface DaoDataForNow {

    @Query("SELECT * FROM table_steps_data_for_today")
    suspend fun getDanieNaDaniyMoment(): StepForNowDataEntity

    @Query("SELECT COUNT(*) FROM table_steps_data_for_today")
    suspend fun proverkaEstLiDanieVtablice(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveDanieNaDaniyMoment(danieNaDaniyMoment: StepForNowDataEntity)

    @Query("DELETE FROM table_steps_data_for_today")
    suspend fun deleteAll()

}