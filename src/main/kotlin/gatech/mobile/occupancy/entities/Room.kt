package gatech.mobile.occupancy.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "rooms")
data class Room(
        val buildingCode: String,
        val buildingId: String,
        val floor: String,
        val room: String
)
{
    @Id @JsonIgnore lateinit var id: String
}
