package gatech.mobile.occupancy.startup

import gatech.mobile.occupancy.entities.Building
import gatech.mobile.occupancy.entities.Floor
import gatech.mobile.occupancy.entities.MissingBuilding
import gatech.mobile.occupancy.entities.Room
import gatech.mobile.occupancy.repositories.BuildingCodeRepository
import gatech.mobile.occupancy.repositories.BuildingRepository
import gatech.mobile.occupancy.repositories.FloorRepository
import gatech.mobile.occupancy.repositories.MissingBuildingRepository
import gatech.mobile.occupancy.repositories.RoomRepository
import gatech.mobile.occupancy.rnoc.WifiAccessPoint
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
        private val floorRepo: FloorRepository,
        private val roomRepo: RoomRepository
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

        val newMongoBuildings = coded.map {
            Building(codes.getValue(it.buildingId), it.buildingId, it.buildingName.trim())
        }
        buildingRepo.save(newMongoBuildings)

        val newMissingBuildings = uncoded.map { MissingBuilding(it.buildingId, it.buildingName) }
        missingRepo.save(newMissingBuildings)

        logger.info { "New buildings initialization complete." }

        val newIds = coded.map { it.buildingId }
        val newAccessPoints = accessPoints.filter { it.buildingId in newIds }
        addMissingFloors(newAccessPoints, newMongoBuildings)
        addMissingRooms(newAccessPoints, codes)
    }

    @Async
    fun addMissingFloors(accessPoints: List<WifiAccessPoint>, buildings: List<Building>)
    {
        val wifiByBuildings = accessPoints.groupBy { it.buildingId }
        val buildingsMap = buildings.associateBy { it.buildingId }

        val newFloors = wifiByBuildings.mapKeys { buildingsMap.getValue(it.key) }
                .flatMap { convertToFloors(it.key, it.value) }
        floorRepo.save(newFloors)
        logger.info { "New floors initialization complete." }
    }

    private fun convertToFloors(building: Building, accessPoints: List<WifiAccessPoint>)
            = accessPoints.map { it.floor }.distinct().map { Floor(building.code, building.buildingId, it) }

    @Async
    fun addMissingRooms(accessPoints: List<WifiAccessPoint>, codes: Map<String, String>)
    {
        val newRooms = accessPoints.map { Room(codes.getValue(it.buildingId), it.buildingId, it.floor, it.room) }
        roomRepo.save(newRooms)
        logger.info { "New rooms initialization complete." }
    }
}
