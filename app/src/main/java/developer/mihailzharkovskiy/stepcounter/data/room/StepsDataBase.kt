package developer.mihailzharkovskiy.stepcounter.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import developer.mihailzharkovskiy.stepcounter.data.room.dao.DaoAllTimeData
import developer.mihailzharkovskiy.stepcounter.data.room.dao.DaoDataForNow
import developer.mihailzharkovskiy.stepcounter.data.room.dao.DaoDataUser
import developer.mihailzharkovskiy.stepcounter.data.room.entity.StepAllTimeDataEntity
import developer.mihailzharkovskiy.stepcounter.data.room.entity.StepForNowDataEntity
import developer.mihailzharkovskiy.stepcounter.data.room.entity.UserDataEntity
import developer.mihailzharkovskiy.stepcounter.data.room.type_converter.TypeConverterForRoom

@Database(
    entities = [
        StepForNowDataEntity::class,
        StepAllTimeDataEntity::class,
        UserDataEntity::class
    ],
    version = 1
)
@TypeConverters(TypeConverterForRoom::class)
abstract class StepsDataBase : RoomDatabase() {

    abstract fun getDaoDataForNow(): DaoDataForNow
    abstract fun getDaoAllTimeData(): DaoAllTimeData
    abstract fun getDaoUserData(): DaoDataUser

    companion object {
        //        private var INSTANCE: StepsDataBase? = null
        const val dataBaseName = "steps_date_base"

//        fun getDatebase(context: Context): StepsDataBase {
//            if (INSTANCE == null) {
//                INSTANCE = Room.databaseBuilder(
//                        context,
//                        StepsDataBase::class.java,
//                        "shagi_datebase")
//                        .fallbackToDestructiveMigration()
//                        .build()
//            }
//            return INSTANCE as StepsDataBase
//        }
    }
}
