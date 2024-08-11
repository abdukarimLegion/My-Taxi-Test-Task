package uz.mango.my_taxi_test_task

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application(){

//    override fun onCreate() {
//        super.onCreate()
//        createNotificationChannel()
//    }

//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val serviceChannel = NotificationChannel(
//                LocationService.CHANNEL_ID,
//                "Location Service Channel",
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            val manager = getSystemService(NotificationManager::class.java)
//            manager.createNotificationChannel(serviceChannel)
//        }
//    }
}