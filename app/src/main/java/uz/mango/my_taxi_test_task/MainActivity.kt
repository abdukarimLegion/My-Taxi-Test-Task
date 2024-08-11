package uz.mango.my_taxi_test_task

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import com.mapbox.maps.MapboxExperimental
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mango.my_taxi_test_task.domain.model.Location
import uz.mango.my_taxi_test_task.domain.usecase.LocationForegroundService
import uz.mango.my_taxi_test_task.presentation.ui.screen.home_ui.HomeScreen
import uz.mango.my_taxi_test_task.presentation.ui.screen.home_ui.HomeViewModel
import uz.mango.my_taxi_test_task.presentation.ui.theme.MyTaxiTestTaskTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var locationForegroundService: LocationForegroundService? = null
    private val viewModel: HomeViewModel by viewModels() // Use Hilt to obtain the ViewModel    private var serviceBoundState by mutableStateOf(false)
    private var serviceBoundState by mutableStateOf(false)

    @OptIn(MapboxExperimental::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyTaxiTestTaskTheme {
                HomeScreen(Modifier.fillMaxSize(), viewModel =  viewModel)
            }
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            Log.d(TAG, "onServiceConnected")

            val binder = service as LocationForegroundService.LocalBinder
            locationForegroundService = binder.getService()
            serviceBoundState = true

            onServiceConnected()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            Log.d(TAG, "onServiceDisconnected")

            serviceBoundState = false
            locationForegroundService = null
        }
    }

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
//        viewModel.sendEvent(HomeScreenReducer.HomeEvent.GrantNotificationPermission(isGranted = it))
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted, service can run
                startForegroundService()
//                viewModel.sendEvent(HomeScreenReducer.HomeEvent.GrantLocationPermission(isGranted = true))
            }

            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted, service can still run.
                startForegroundService()
//                viewModel.sendEvent(HomeScreenReducer.HomeEvent.GrantLocationPermission(isGranted = true))
            }

            else -> {
                Toast.makeText(
                    this,
                    "Iltimos ruhsat bering!", Toast.LENGTH_SHORT
                ).show()
//                viewModel.sendEvent(HomeScreenReducer.HomeEvent.GrantLocationPermission(isGranted = false))
            }
        }
    }



    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when (ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            )) {
                PackageManager.PERMISSION_GRANTED -> {
                }
                else -> {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    private fun startForegroundServiceOnMapReady() {
        val fineLocationGranted = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationGranted = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fineLocationGranted || coarseLocationGranted) {
            startForegroundService()
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun startForegroundService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                checkAndRequestNotificationPermission()
            }
        }

        val serviceIntent = Intent(this, LocationForegroundService::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
        tryToBindToServiceIfRunning()
    }

    private fun tryToBindToServiceIfRunning() {
        val serviceIntent = Intent(this, LocationForegroundService::class.java)
        bindService(serviceIntent, serviceConnection, 0)
    }

    private fun onServiceConnected() {
        lifecycleScope.launch {
            locationForegroundService?.locationFlow?.collectLatest {

                it?.let {
                    val time = getCurrentTimeStamp()
                    val location = Location(
                        latitude = it.latitude,
                        longitude = it.longitude,
                        timestamp = time.toString()
                    )
                    viewModel.insertUserLocation(userLocation = location)
                }
                Log.d(TAG, "onServiceConnected: $it")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (serviceBoundState) {
            unbindService(serviceConnection)
        }
        locationForegroundService?.stopForegroundService()
    }

    private fun getCurrentTimeStamp(): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
    }
    companion object {
        private const val TAG = "MainActivity"
    }

}
