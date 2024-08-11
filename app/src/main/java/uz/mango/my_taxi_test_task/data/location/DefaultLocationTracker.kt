package com.plcoding.weatherapp.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

//
//@ExperimentalCoroutinesApi
//class DefaultLocationTracker @Inject constructor(
//    private val locationClient: FusedLocationProviderClient,
//    private val application: Application
//): LocationTracker {
//    override  fun insertCurrentLocation(userLocation: UserLocation2) {
//
//    }
//
//    override  fun getCurrentLocation(): UserLocation2? {
//        val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
//            application,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED
//        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
//            application,
//            Manifest.permission.ACCESS_COARSE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED
//
//        val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
//                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//        if(!hasAccessCoarseLocationPermission || !hasAccessFineLocationPermission || !isGpsEnabled) {
//            return null
//        }
//
//        return suspendCancellableCoroutine { cont ->
//            locationClient.lastLocation.apply {
//                if(isComplete) {
//                    if(isSuccessful) {
//                        cont.resume(result)
//                    } else {
//                        cont.resume(null)
//                    }
//                    return@suspendCancellableCoroutine
//                }
//                addOnSuccessListener {
//                    cont.resume(it)
//                }
//                addOnFailureListener {
//                    cont.resume(null)
//                }
//                addOnCanceledListener {
//                    cont.cancel()
//                }
//            }
//        }
//    }
//}

class DefaultLocationClient(
    private val context: Context,
    private val client: FusedLocationProviderClient
) : LocationClient {

    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(interval: Long): Flow<Location> {
        return callbackFlow {
            if (!context.hasLocationPermission()) {
                throw LocationClient.LocationException(message = "Missing location permission")
            }

            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!isGpsEnabled && !isNetworkEnabled) {
                throw LocationClient.LocationException(message = "GPS is disabled")
            }

            val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, interval)
                .build()


            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    result.locations.lastOrNull()?.let { location ->
                        launch { send(location) }
                    }
                }
            }

            client.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )

            awaitClose {
                client.removeLocationUpdates(locationCallback)
            }
        }
    }
}

interface LocationClient {
    fun getLocationUpdates(interval: Long): Flow<Location>

    class LocationException(message: String): Exception()
}

fun Context.hasLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
}