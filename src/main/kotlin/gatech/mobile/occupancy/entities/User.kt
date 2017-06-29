package gatech.mobile.occupancy.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
data class User(val username: String)
{
    @Id @JsonIgnore lateinit var id: String
}
