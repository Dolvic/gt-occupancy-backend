package gatech.mobile.occupancy.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "missing_buildings")
data class MissingBuilding(
        @Indexed(unique = true) val buildingId: String,
        val name: String
)
{
    @Id @JsonIgnore lateinit var id: String
}
