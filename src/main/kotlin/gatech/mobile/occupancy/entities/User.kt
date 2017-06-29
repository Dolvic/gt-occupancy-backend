package gatech.mobile.occupancy.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
data class User(@Indexed(unique = true) val username: String, val name: String, val favorites: List<Favorite> = emptyList())
{
    @Id @JsonIgnore lateinit var id: String
}
