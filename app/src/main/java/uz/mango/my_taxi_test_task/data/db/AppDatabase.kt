package uz.mango.my_taxi_test_task.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.mango.my_taxi_test_task.data.dao.UserLocationDao
import uz.mango.my_taxi_test_task.data.model.UserLocationEntity

@Database(entities = [UserLocationEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userLocationDao(): UserLocationDao
}