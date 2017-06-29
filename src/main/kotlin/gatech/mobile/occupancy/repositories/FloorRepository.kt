package gatech.mobile.occupancy.repositories

import gatech.mobile.occupancy.entities.Floor
import org.springframework.data.mongodb.repository.MongoRepository

interface FloorRepository : MongoRepository<Floor, String>
{
    fun findByBuilding(building: String): List<Floor>

    fun findByBuildingAndFloor(building: String, floor: Int): Floor?
}
