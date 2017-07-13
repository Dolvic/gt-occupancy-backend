package gatech.mobile.occupancy.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "buildings_codes")
data class BuildingCode(
        @Indexed(unique = true) val buildingId: String,
        @Indexed(unique = true) val code: String)
{
    @Id @JsonIgnore lateinit var id: String
}
