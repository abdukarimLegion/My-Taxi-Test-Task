package uz.mango.my_taxi_test_task.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.mango.my_taxi_test_task.data.model.UserLocationEntity

@Dao
interface UserLocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userLocation: UserLocationEntity)

    @Query("SELECT * FROM location ORDER BY timeStamp DESC LIMIT 1")
    fun getFirstLocation(): UserLocationEntity

}