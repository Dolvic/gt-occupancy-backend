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

        val mongoBuildingIds = buildingRepo.findAll().map { it.buildingId }

        val codes = codeRepo.findAll().associateBy(keySelector = { it.buildingId }, valueTransform = { it.code })
        val codedIds = codes.keys

        val missingIds = missingRepo.findAll().map { it.buildingId }

        val (coded, uncoded) = accessPoints.filter { it.buildingId !in mongoBuildingIds }
                .partition { it.buildingId in codedIds }

        val newAPs = coded.distinctBy { it.buildingId }
        val newMissingAPs = uncoded.distinctBy { it.buildingId }.filter { it.buildingId !in missingIds }
        logger.info { "New buildings: ${newAPs.size} known, ${newMissingAPs.size} unknown" }

        val newMongoBuildings = newAPs.map { (id, name) -> Building(codes.getValue(id), id, name) }
        buildingRepo.save(newMongoBuildings)

        val newMissingBuildings = newMissingAPs.map { MissingBuilding(it.buildingId, it.buildingName) }
        missingRepo.save(newMissingBuildings)

        logger.info { "New buildings initialization complete." }

        addMissingFloors(coded, codes)
        addMissingRooms(coded, codes)
    }

    @Async
    fun addMissingFloors(accessPoints: List<WifiAccessPoint>, codes: Map<String, String>)
    {
        val apsByBuildings = accessPoints.groupBy { it.buildingId }
        val newFloors = apsByBuildings.flatMap { it.value.toFloors(codes.getValue(it.key)) }
        floorRepo.save(newFloors)
        logger.info { "New floors initialization complete." }
    }

    private fun List<WifiAccessPoint>.toFloors(code: String)
            = distinctBy { it.floor }.map { Floor(code, it.buildingId, it.floor) }

    @Async
    fun addMissingRooms(accessPoints: List<WifiAccessPoint>, codes: Map<String, String>)
    {
        val newRooms = accessPoints.map { Room(codes.getValue(it.buildingId), it.buildingId, it.floor, it.room) }
        roomRepo.save(newRooms)
        logger.info { "New rooms initialization complete." }
    }
}
