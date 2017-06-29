package gatech.mobile.occupancy.entities

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "users")
data class User(@Id val userId: Long, val username: String)
