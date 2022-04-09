package developer.mihailzharkovskiy.stepcounter.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import developer.mihailzharkovskiy.stepcounter.domain.usecase.user_data.UserDataUseCaseModel

@Entity(tableName = "table_user_data")
data class UserDataEntity(
    @PrimaryKey val rost: Int,
    @ColumnInfo(name = "ves") val ves: Int,
    @ColumnInfo(name = "norma_shagov") val normaShagov: Int,
)

fun UserDataEntity.mapToDomainModel(): UserDataUseCaseModel {
    return UserDataUseCaseModel(
        height = this.rost,
        weight = this.ves,
        stepPlane = this.normaShagov
    )
}
