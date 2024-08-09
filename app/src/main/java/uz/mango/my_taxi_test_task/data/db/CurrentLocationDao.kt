package uz.mango.my_taxi_test_task.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import uz.mango.my_taxi_test_task.data.model.UserLocation2

@Dao
interface UserLocationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserCurrentLocation(userLocation: UserLocation2)

    @Query("SELECT * FROM location ORDER BY timeStamp DESC LIMIT 1")
    fun getLatestUserLocation(): Flow<UserLocation2?>

}