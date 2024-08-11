package uz.mango.my_taxi_test_task.data.repository

import uz.mango.my_taxi_test_task.data.dao.UserLocationDao
import uz.mango.my_taxi_test_task.data.model.UserLocationEntity
import javax.inject.Inject

interface MainRepository {
    suspend fun insertUserLocation(userLocation: UserLocationEntity)
    suspend fun getUserLocation(): UserLocationEntity
}

class MainRepositoryImpl @Inject constructor(
    private val userLocationDao: UserLocationDao,
) : MainRepository {
    override suspend fun insertUserLocation(userLocation: UserLocationEntity) {
        userLocationDao.insert(userLocation)
    }

    override suspend fun getUserLocation(): UserLocationEntity {
        return userLocationDao.getFirstLocation()
    }
}
