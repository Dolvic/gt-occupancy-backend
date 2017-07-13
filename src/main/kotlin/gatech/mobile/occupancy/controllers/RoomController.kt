package gatech.mobile.occupancy.controllers

import gatech.mobile.occupancy.entities.Room
import gatech.mobile.occupancy.repositories.RoomRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/buildings/{building}/floors/{floor}")
class RoomController(private val roomRepository: RoomRepository)
{
    @GetMapping("/rooms")
    fun fetchRooms(@PathVariable building: String, @PathVariable floor: String): Map<String, List<Room>> =
            mapOf("results" to roomRepository.findByBuildingAndFloor(building, floor))

    @GetMapping("/rooms/{room}")
    fun fetchFloor(@PathVariable building: String, @PathVariable floor: String, @PathVariable room: String): Any
    {
        val roomFromDb = roomRepository.findByBuildingAndFloorAndRoom(building, floor, room)
        return roomFromDb ?: ResponseEntity.notFound().build<Room>()
    }
}
