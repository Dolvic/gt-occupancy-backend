package gatech.mobile.occupancy.repositories

import gatech.mobile.occupancy.entities.BuildingCode
import org.springframework.data.mongodb.repository.MongoRepository

interface BuildingCodeRepository : MongoRepository<BuildingCode, String>
