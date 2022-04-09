package developer.mihailzharkovskiy.stepcounter.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import developer.mihailzharkovskiy.stepcounter.data.room.entity.UserDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoDataUser {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserData(userData: UserDataEntity)

    @Query("SELECT * FROM table_user_data LIMIT 1")
    fun getUserData(): Flow<UserDataEntity?>

    @Query("DELETE FROM table_user_data")
    suspend fun deleteAll()
}