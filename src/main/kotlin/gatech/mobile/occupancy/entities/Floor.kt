package gatech.mobile.occupancy.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "floors")
data class Floor(
        val building: String,
        val floor: String
)
{
    @Id @JsonIgnore lateinit var id: String
}
