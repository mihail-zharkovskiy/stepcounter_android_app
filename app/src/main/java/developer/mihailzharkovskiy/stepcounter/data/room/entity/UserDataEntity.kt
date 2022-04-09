package developer.mihailzharkovskiy.stepcounter.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_user_data")
data class UserDataEntity(
    @PrimaryKey
    @ColumnInfo(name = "height") val height: Int,
    @ColumnInfo(name = "weight") val weight: Int,
    @ColumnInfo(name = "step_plane") val stepPlane: Int,
)

