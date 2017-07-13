package gatech.mobile.occupancy.startup

import gatech.mobile.occupancy.entities.Floor
import gatech.mobile.occupancy.repositories.BuildingCodeRepository
import gatech.mobile.occupancy.repositories.FloorRepository
import gatech.mobile.occupancy.repositories.MissingBuildingRepository
import gatech.mobile.occupancy.rnoc.WifiAccessPoint
import mu.KLogging
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class FloorInitializer(private val floorRepo: FloorRepository)
{
    companion object : KLogging()

    fun addMissingFloors(accessPoints: List<WifiAccessPoint>, buildingIdToCode: Map<String, String>)
    {
        val wifiByBuildings = accessPoints.groupBy { it.buildingId }

        wifiByBuildings.forEach {
            buildingId, ap ->
            this.addMissingFloorsForBuilding(buildingId, buildingIdToCode.getValue(buildingId), ap)
        }
    }

    @Async
    private fun addMissingFloorsForBuilding(buildingId: String,
                                            buildingCode: String,
                                            accessPoints: List<WifiAccessPoint>)
    {
        val floors = accessPoints.map { it.floor }.distinct()
        val newFloors = floors.map { Floor(buildingCode, buildingId, it) }
        floorRepo.save(newFloors)
        logger.debug { "${floors.size} new floors for building ID $buildingId saved." }
    }
}
