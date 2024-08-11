package uz.mango.my_taxi_test_task.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location")
data class UserLocationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timeStamp: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
)