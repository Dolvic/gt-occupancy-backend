package gatech.mobile.occupancy.startup

import gatech.mobile.occupancy.entities.Building
import gatech.mobile.occupancy.repositories.BuildingRepository
import gatech.mobile.occupancy.rnoc.WifiAccessPoint
import gatech.mobile.occupancy.rnoc.WifiCountApi
import mu.KLogging
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class BuildingsInitializer(
        private val buildingRepo: BuildingRepository,
        private val wifiApi: WifiCountApi
)
{
    companion object : KLogging()

    @Async
    fun addMissingBuildings()
    {
        val (_, accessPoints) = wifiApi.fetchAll()
        val wifiBuildings = accessPoints.distinctBy { it.building_id }

        val mongoBuildings = buildingRepo.findAll()
        val mongoBuildingIds = mongoBuildings.map { it.buildingId }

        val newBuildings = wifiBuildings.filterNot { mongoBuildingIds.contains(it.building_id) }
        logger.info { "Found ${newBuildings.size} new buildings." }
        val newMongoBuildings = newBuildings.map { it.toMongoBuilding() }
        buildingRepo.save(newMongoBuildings)
        logger.debug { "New buildings saved." }
    }

    private fun WifiAccessPoint.toMongoBuilding(): Building = Building(building_id, building_id, building_name)
}
