package gatech.mobile.occupancy.controllers

import gatech.mobile.occupancy.entities.Building
import gatech.mobile.occupancy.repositories.BuildingRepository
import gatech.mobile.occupancy.repositories.FloorRepository
import gatech.mobile.occupancy.rnoc.WifiCountApi
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/buildings")
class BuildingController(
        private val buildingRepo: BuildingRepository,
        private val floorRepo: FloorRepository,
        private val wifiApi: WifiCountApi
)
{
    @GetMapping
    fun fetchBuildings(@RequestParam(required = false) name: String?): Map<String, List<OccupiedBuilding>>
    {
        val results =
                if (name == null) buildingRepo.findAll(Sort("name"))
                else buildingRepo.findByNameLikeIgnoreCase(name)
        return mapOf("results" to results.map { it.toOccupiedBuilding() })
    }

    @GetMapping("/{code}")
    fun fetchBuilding(@PathVariable code: String): ResponseEntity<OccupiedBuilding>
    {
        val building = buildingRepo.findByCode(code)
        val entity: ResponseEntity<OccupiedBuilding>
        entity = if (building != null)
        {
            val count = wifiApi.fetchBuilding(building.buildingId).clientCount
            val floors = floorRepo.findByBuildingCode(building.code, Sort("floor")).map { it.floor }
            ResponseEntity.ok(building.toOccupiedBuilding(count, floors))
        }
        else
        {
            ResponseEntity.notFound().build()
        }
        return entity
    }

    private fun Building.toOccupiedBuilding(count: Int? = null, floors: List<String> = emptyList())
            = OccupiedBuilding(code, name, count, floors)
}
