package gatech.mobile.occupancy.controllers

import gatech.mobile.occupancy.entities.Floor
import gatech.mobile.occupancy.repositories.FloorRepository
import gatech.mobile.occupancy.repositories.RoomRepository
import gatech.mobile.occupancy.rnoc.WifiCountApi
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/buildings/{building}/floors")
class FloorController(
        private val floorRepo: FloorRepository,
        private val roomRepo: RoomRepository,
        private val wifiApi: WifiCountApi
)
{
    @GetMapping
    fun fetchFloors(@PathVariable building: String): Map<String, List<OccupiedFloor>> =
            mapOf("results" to floorRepo.findByBuilding(building).map { it.toOccupiedFloor() })

    @GetMapping("/{floor}")
    fun fetchFloor(@PathVariable building: String, @PathVariable floor: String): ResponseEntity<OccupiedFloor>
    {
        val floorEntity = floorRepo.findByBuildingAndFloor(building, floor)
        val entity: ResponseEntity<OccupiedFloor>
        entity = if (floorEntity != null)
        {
            val count = wifiApi.fetchFloor(floorEntity.buildingId, floor).clientCount
            val rooms = roomRepo.findByBuildingAndFloor(building, floor).map { it.room }
            ResponseEntity.ok(floorEntity.toOccupiedFloor(count, rooms))
        }
        else
        {
            ResponseEntity.notFound().build()
        }
        return entity
    }

    private fun Floor.toOccupiedFloor(count: Int? = null, rooms: List<String> = emptyList())
            = OccupiedFloor(building, floor, count, rooms)
}
