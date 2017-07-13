package gatech.mobile.occupancy.repositories

import gatech.mobile.occupancy.entities.MissingBuilding
import org.springframework.data.mongodb.repository.MongoRepository

interface MissingBuildingRepository : MongoRepository<MissingBuilding, String>
