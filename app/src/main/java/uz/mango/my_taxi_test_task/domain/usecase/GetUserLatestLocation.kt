package uz.mango.my_taxi_test_task.domain.usecase

import uz.mango.my_taxi_test_task.data.model.UserLocationEntity
import uz.mango.my_taxi_test_task.data.repository.MainRepositoryImpl
import javax.inject.Inject

class GetUserLatestLocation @Inject constructor(
    private val mainRepository: MainRepositoryImpl
) {
    suspend operator fun invoke(): UserLocationEntity{
        return mainRepository.getUserLocation()
    }
}