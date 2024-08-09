package uz.mango.my_taxi_test_task.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.mango.my_taxi_test_task.data.model.UserLocation2

@Database(entities = [UserLocation2::class], version = 2, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userLocationDao(): UserLocationDao
}