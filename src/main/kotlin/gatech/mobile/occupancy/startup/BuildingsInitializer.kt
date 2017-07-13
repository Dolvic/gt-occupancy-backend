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
        val wifiBuildings = accessPoints.distinctBy { it.buildingId }

        val mongoBuildings = buildingRepo.findAll()
        val mongoBuildingIds = mongoBuildings.map { it.buildingId }

        val codeEntries = codeRepo.findAll()
        val codes = codeEntries.associateBy(keySelector = { it.buildingId }, valueTransform = { it.code })
        val codedIds = codes.keys

        val missingBuildings = missingRepo.findAll()
        val missingIds = missingBuildings.map { it.buildingId }

        val newBuildings = wifiBuildings.filterNot { mongoBuildingIds.contains(it.buildingId) }
                .partition { codedIds.contains(it.buildingId) }
        val coded = newBuildings.first
        val uncoded = newBuildings.second.toMutableList()
        uncoded.removeIf { missingIds.contains(it.buildingId) }

        logger.info { "Found new buildings: ${coded.size} known, ${uncoded.size} unknown" }

        val newMongoBuildings = coded.map { Building(codes.getValue(it.buildingId), it.buildingId, it.buildingName) }
        buildingRepo.save(newMongoBuildings)
        val newMissingBuildings = uncoded.map { MissingBuilding(it.buildingId, it.buildingName) }
        missingRepo.save(newMissingBuildings)
        logger.debug { "New buildings saved." }
    }
}
