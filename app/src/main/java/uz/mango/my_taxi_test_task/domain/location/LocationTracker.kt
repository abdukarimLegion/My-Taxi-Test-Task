package com.plcoding.weatherapp.domain.location

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import uz.mango.my_taxi_test_task.data.model.UserLocationEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


interface LocationTracker {
    fun getLocationUpdates(): LiveData<UserLocationEntity?>
    fun stopLocationUpdates()
}

class LocationTrackerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LocationTracker {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val locationLiveData = MutableLiveData<UserLocationEntity?>()

    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(): LiveData<UserLocationEntity?> {
        fusedLocationClient.requestLocationUpdates(
            createLocationRequest(),
            locationCallback,
            Looper.getMainLooper()
        )
        return locationLiveData
    }

    private fun createLocationRequest(): LocationRequest {
        return LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(1000)
            .setMaxUpdateDelayMillis(20000)
            .build()
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val lastLocation = locationResult.lastLocation
            val userLocation = lastLocation?.let {
                UserLocationEntity(
                    latitude = it.latitude,
                    longitude = it.longitude,
                    timeStamp = getCurrentTimeStamp()
                )
            }
            locationLiveData.postValue(userLocation)
        }
    }

    override fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun getCurrentTimeStamp(): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
    }
}
