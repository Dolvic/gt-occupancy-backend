package gatech.mobile.occupancy.repositories

import gatech.mobile.occupancy.entities.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String>
