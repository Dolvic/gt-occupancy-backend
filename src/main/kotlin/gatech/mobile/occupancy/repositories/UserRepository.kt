package gatech.mobile.occupancy.repositories

import gatech.mobile.occupancy.entities.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>
