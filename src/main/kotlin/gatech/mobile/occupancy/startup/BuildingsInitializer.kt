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
        private val missingRepo: MissingBuildingRepository,
        private val floorInitializer: FloorInitializer
)
{
    companion object : KLogging()

    @Async
    fun addMissingBuildings()
    {
        val (_, accessPoints) = wifiApi.fetchAll(true)
        val wifiBuildings = accessPoints.distinctBy { it.buildingId }

        val mongoBuildingIds = buildingRepo.findAll().map { it.buildingId }

        val codes = codeRepo.findAll().associateBy(keySelector = { it.buildingId }, valueTransform = { it.code })
        val codedIds = codes.keys

        val missingIds = missingRepo.findAll().map { it.buildingId }

        val newBuildings = wifiBuildings.filter { it.buildingId !in mongoBuildingIds }
                .partition { it.buildingId in codedIds }
        val coded = newBuildings.first
        val uncoded = newBuildings.second.toMutableList()
        uncoded.removeIf { it.buildingId in missingIds }

        logger.info { "New buildings: ${coded.size} known, ${uncoded.size} unknown" }

        val newMongoBuildings = coded.map { Building(codes.getValue(it.buildingId), it.buildingId, it.buildingName) }
        buildingRepo.save(newMongoBuildings)

        val newMissingBuildings = uncoded.map { MissingBuilding(it.buildingId, it.buildingName) }
        missingRepo.save(newMissingBuildings)

        logger.info { "New buildings initialization complete." }

        val newBuildingIds = newMongoBuildings.map { it.buildingId }
        val accessPointsForNewBuildings = accessPoints.filter { it.buildingId in newBuildingIds }
        floorInitializer.addMissingFloors(accessPointsForNewBuildings, codes)
    }
}
