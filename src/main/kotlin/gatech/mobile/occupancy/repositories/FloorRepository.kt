package gatech.mobile.occupancy.repositories

import gatech.mobile.occupancy.entities.Floor
import org.springframework.data.mongodb.repository.MongoRepository

interface FloorRepository : MongoRepository<Floor, String>
{
    fun findByBuildingCode(building: String): List<Floor>

    fun findByBuildingCodeAndFloor(building: String, floor: String): Floor?
}
