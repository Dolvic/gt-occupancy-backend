package gatech.mobile.occupancy.startup

import gatech.mobile.occupancy.entities.Building
import gatech.mobile.occupancy.entities.MissingBuilding
import gatech.mobile.occupancy.repositories.BuildingCodeRepository
import gatech.mobile.occupancy.repositories.BuildingRepository
import gatech.mobile.occupancy.repositories.MissingBuildingRepository
import gatech.mobile.occupancy.rnoc.WifiCountApi
import mu.KLogging
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class BuildingsInitializer(
        private val buildingRepo: BuildingRepository,
        private val wifiApi: WifiCountApi,
        private val codeRepo: BuildingCodeRepository,
        private val missingRepo: MissingBuildingRepository
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

        val codeEntries = codeRepo.findAll()
        val codes = codeEntries.associateBy(keySelector = { it.buildingId }, valueTransform = { it.code })
        val codedIds = codes.keys

        val missingBuildings = missingRepo.findAll()
        val missingIds = missingBuildings.map { it.buildingId }

        val newBuildings = wifiBuildings.filterNot { mongoBuildingIds.contains(it.building_id) }
                .partition { codedIds.contains(it.building_id) }
        val coded = newBuildings.first
        val uncoded = newBuildings.second.toMutableList()
        uncoded.removeIf { missingIds.contains(it.building_id) }

        logger.info { "Found new buildings: ${coded.size} known, ${uncoded.size} unknown" }

        val newMongoBuildings = coded.map { Building(codes.getValue(it.building_id), it.building_id, it.building_name) }
        buildingRepo.save(newMongoBuildings)
        val newMissingBuildings = uncoded.map { MissingBuilding(it.building_id, it.building_name) }
        missingRepo.save(newMissingBuildings)
        logger.debug { "New buildings saved." }
    }
}
