package developer.mihailzharkovskiy.stepcounter.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import developer.mihailzharkovskiy.stepcounter.domain.usecase.step_counter.TickerUseCaseModel
import java.util.*

@Entity(tableName = "table_steps_data_for_today")
data class StepForNowDataEntity(
    @PrimaryKey
    @ColumnInfo(name = "time") val date: Date = Date(),
    @ColumnInfo(name = "key") val steps: Int = 0,
    @ColumnInfo(name = "meters") val km: Double = 0.0,
    @ColumnInfo(name = "progress") val progress: Int = 0,
    @ColumnInfo(name = "kkal") val kkal: Double = 0.0,
    @ColumnInfo(name = "plane") val stepPlane: Int = 0,
)

fun StepForNowDataEntity.mapToDomainModel(): TickerUseCaseModel {
    return TickerUseCaseModel(
        steps = this.steps,
        km = this.km,
        progress = this.progress,
        kkal = this.kkal,
        stepsPlane = this.stepPlane,
        date = this.date
    )
}



