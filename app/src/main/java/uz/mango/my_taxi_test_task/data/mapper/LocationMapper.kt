package uz.mango.my_taxi_test_task.data.mapper

import uz.mango.my_taxi_test_task.data.model.UserLocationEntity
import uz.mango.my_taxi_test_task.domain.model.Location


object LocationMapper {
    class LiveLocation internal constructor(private val dto: UserLocationEntity) {
        operator fun invoke(): Location = with(dto) {
            Location(
                latitude!!,
                longitude!!,
                timeStamp!!
            )
        }
    }

    class LiveLocationEntity internal constructor(private val dto: Location) {
        operator fun invoke(): UserLocationEntity = with(dto) {
            UserLocationEntity(
                latitude = latitude!!,
                longitude = longitude!!,
                timeStamp = timestamp!!
            )
        }
    }
}