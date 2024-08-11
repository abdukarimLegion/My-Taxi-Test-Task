package uz.mango.my_taxi_test_task.di

import android.content.Context
import androidx.room.Room
import com.plcoding.weatherapp.domain.location.LocationTracker
import com.plcoding.weatherapp.domain.location.LocationTrackerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.mango.my_taxi_test_task.data.dao.UserLocationDao
import uz.mango.my_taxi_test_task.data.db.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun test(): Int {
        return 1
    }

    @Provides
    @Singleton
    fun provideLocationTracker(
        @ApplicationContext context: Context
    ): LocationTracker {
        return LocationTrackerImpl(context)
    }

//    @Provides
//    @Singleton
//    fun provideLocationTracker(): LocationTracker {
//        return LocationTracker() // Agar `LocalLocationTracker` sinfi mavjud bo'lsa
//    }

//    @Provides
//    @Singleton
//    fun provideMainRepository(
//        locationTracker: LocationTrackerImpl
//    ): MainRepository {
//        return MainRepositoryImpl(locationTracker)
//    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext appContext: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "location"
        ).build()
    }

    @Provides
    fun provideLocationDao(db: AppDatabase): UserLocationDao {
        return db.userLocationDao()
    }
//    fun provideFusedLocationProviderClient(app: Application): FusedLocationProviderClient {
//        return LocationServices.getFusedLocationProviderClient(app)
//    }
}