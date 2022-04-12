package developer.mihailzharkovskiy.stepcounter.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "table_steps_data_for_specific_day")
data class StepAllTimeDataEntity(
    @PrimaryKey
    @ColumnInfo(name = "date")
    val date: String = SimpleDateFormat("EE\ndd.MM", Locale.getDefault()).format(Date()),
    @ColumnInfo(name = "km") val km: Double = 0.0,
    @ColumnInfo(name = "kkal") val kkal: Double = 0.0,
    @ColumnInfo(name = "steps") val steps: Int = 0,
    @ColumnInfo(name = "plane") val stepPlane: Int = 0,
    @ColumnInfo(name = "progress") val progress: Int = 0,

    ) {

    override fun equals(other: Any?): Boolean {
        return if (other != null && other is StepAllTimeDataEntity && other !== this) {
            this.date == other.date
        } else false
    }

    override fun hashCode(): Int {
        var result = date.hashCode()
        result = 31 * result + steps
        result = 31 * result + km.hashCode()
        result = 31 * result + progress
        result = 31 * result + kkal.hashCode()
        result = 31 * result + stepPlane
        return result
    }

    fun plusEntity(newEntity: StepAllTimeDataEntity): StepAllTimeDataEntity {
        return StepAllTimeDataEntity(
            date = newEntity.date,
            stepPlane = newEntity.stepPlane,
            progress = ((this.steps + newEntity.steps) * 100) / newEntity.stepPlane,
            steps = this.steps + newEntity.steps,
            kkal = this.kkal + newEntity.kkal,
            km = this.km + newEntity.km
        )
    }
}
