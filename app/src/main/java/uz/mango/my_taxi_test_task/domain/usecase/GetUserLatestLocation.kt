package uz.mango.my_taxi_test_task.domain.usecase

import kotlinx.coroutines.flow.Flow
import uz.mango.my_taxi_test_task.data.model.UserLocation2
import uz.mango.my_taxi_test_task.presentation.ui.screen.home_ui.UserLocation
import uz.mango.my_taxi_test_task.domain.repository.MainRepository
import javax.inject.Inject

class GetUserLatestLocation @Inject constructor(
    private val mainRepository: MainRepository
) {
    suspend operator fun invoke(): Flow<UserLocation2?> {
        return mainRepository.getLatestUserLocation()
    }
}