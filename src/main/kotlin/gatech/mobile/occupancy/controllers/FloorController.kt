package gatech.mobile.occupancy.controllers

import gatech.mobile.occupancy.entities.Floor
import gatech.mobile.occupancy.repositories.FloorRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/buildings/{building}/floors")
class FloorController(val floorRepository: FloorRepository)
{
    @GetMapping
    fun fetchFloors(@PathVariable building: String): Map<String, List<Floor>> =
            mapOf("results" to floorRepository.findByBuilding(building))

    @GetMapping("/{floor}")
    fun fetchFloor(@PathVariable building: String, @PathVariable floor: Int): Any =
            floorRepository.findByBuildingAndFloor(building, floor) ?: ResponseEntity.notFound().build<Floor>()
}
