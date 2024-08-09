package uz.mango.my_taxi_test_task.domain.repository


import kotlinx.coroutines.flow.Flow
import uz.mango.my_taxi_test_task.data.model.UserLocation2
import javax.inject.Inject

interface MainRepository  {

    suspend fun insertUserCurrentLocation(userLocation: UserLocation2)
    suspend fun getLatestUserLocation():Flow<UserLocation2?>

}