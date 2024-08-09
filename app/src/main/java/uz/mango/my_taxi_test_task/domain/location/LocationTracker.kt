package com.plcoding.weatherapp.domain.location

import android.location.Location
import kotlinx.coroutines.flow.Flow
import uz.mango.my_taxi_test_task.data.model.UserLocation2
import javax.inject.Inject

interface LocationTracker {
    suspend fun insertCurrentLocation(userLocation: UserLocation2)

    suspend fun getCurrentLocation(): Flow<UserLocation2?>
}

class LocationTrackerImpl @Inject constructor(
) : LocationTracker {

    override suspend fun insertCurrentLocation(userLocation: UserLocation2) {
        // Implement the method to insert the location
    }

    override suspend fun getCurrentLocation(): Flow<UserLocation2?> {
        // Implement the method to get the current location
        return getCurrentLocation()
    }
}
