package gatech.mobile.occupancy.controllers

import gatech.mobile.occupancy.entities.Building
import gatech.mobile.occupancy.repositories.BuildingRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/buildings")
class BuildingController(private val buildingRepository: BuildingRepository)
{
    @GetMapping
    fun fetchBuildings(@RequestParam(required = false) name: String?): Map<String, List<Building>>
    {
        val results =
                if (name == null) buildingRepository.findAll()
                else buildingRepository.findByNameLikeIgnoreCase(name)
        return mapOf("results" to results)
    }

    @GetMapping("/{code}")
    fun fetchBuilding(@PathVariable code: String): Any =
            buildingRepository.findByCode(code) ?: ResponseEntity.notFound().build<Building>()
}
