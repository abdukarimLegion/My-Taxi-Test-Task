package uz.mango.my_taxi_test_task.data.repository

import com.plcoding.weatherapp.domain.location.LocationTracker
import com.plcoding.weatherapp.domain.location.LocationTrackerImpl
import kotlinx.coroutines.flow.Flow
import uz.mango.my_taxi_test_task.data.model.UserLocation2
import uz.mango.my_taxi_test_task.domain.repository.MainRepository
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val localDataSource: LocationTrackerImpl
) : MainRepository{

     override suspend fun insertUserCurrentLocation(userLocation: UserLocation2) {
        localDataSource.insertCurrentLocation(userLocation)
    }



     override suspend fun getLatestUserLocation(): Flow<UserLocation2?> {
        return localDataSource.getCurrentLocation()
    }
}
