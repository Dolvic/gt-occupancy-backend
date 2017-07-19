package gatech.mobile.occupancy.controllers

import gatech.mobile.occupancy.entities.Room
import gatech.mobile.occupancy.repositories.RoomRepository
import gatech.mobile.occupancy.rnoc.WifiCountApi
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/buildings/{building}/floors/{floor}")
class RoomController(
        private val roomRepo: RoomRepository,
        private val wifiApi: WifiCountApi
)
{
    @GetMapping("/rooms")
    fun fetchRooms(@PathVariable building: String, @PathVariable floor: String): Map<String, List<OccupiedRoom>> =
            mapOf("results" to roomRepo.findByBuildingCodeAndFloor(building, floor, Sort("room"))
                    .map { it.toOccupiedRoom() })

    @GetMapping("/rooms/{room}")
    fun fetchRoom(
            @PathVariable building: String,
            @PathVariable floor: String,
            @PathVariable room: String
    ): ResponseEntity<OccupiedRoom>
    {
        val roomEntity = roomRepo.findByBuildingCodeAndFloorAndRoom(building, floor, room)
        val response: ResponseEntity<OccupiedRoom>
        response = if (roomEntity != null)
        {
            val count = wifiApi.fetchRoom(roomEntity.buildingId, floor, room).clientCount
            ResponseEntity.ok(roomEntity.toOccupiedRoom(count))
        }
        else
        {
            ResponseEntity.notFound().build()
        }
        return response
    }

    private fun Room.toOccupiedRoom(count: Int? = null) = OccupiedRoom(buildingCode, floor, room, count)
}
