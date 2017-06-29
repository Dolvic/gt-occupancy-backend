package gatech.mobile.occupancy.repositories

import gatech.mobile.occupancy.entities.Building
import org.springframework.data.mongodb.repository.MongoRepository

interface BuildingRepository : MongoRepository<Building, String>
{
    fun findByCode(code: String): Building?

    fun findByNameLikeIgnoreCase(searchTerm: String): List<Building>
}
