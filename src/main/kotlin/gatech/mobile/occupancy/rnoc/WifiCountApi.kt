package gatech.mobile.occupancy.rnoc

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import gatech.mobile.occupancy.OccupancyConfigurationProperties
import mu.KLogging
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Component
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate

@Component
class WifiCountApi(
        clientBuilder: RestTemplateBuilder,
        appProps: OccupancyConfigurationProperties,
        private val objectMapper: ObjectMapper
)
{
    companion object : KLogging()

    private val client: RestTemplate = clientBuilder.rootUri(appProps.rnoc.wifiApi)
            .build()

    private fun fetchForPath(path: String, withDetails: Boolean = false): WifiCount
    {
        try
        {
            return client.getForObject("$path/?details=$withDetails", WifiCount::class.java)
        }
        catch(ignored: ResourceAccessException)
        {
            logger.warn { "Couldn't connect to WiFi API" }
        }

        val resourceFile = when
        {
            "floor" in path -> "/floor.json"
            "building" in path -> "/building.json"
            else -> "/all.json"
        }
        val resource = javaClass.getResourceAsStream(resourceFile)
        return objectMapper.readValue<WifiCount>(resource)
    }

    fun fetchAll(withDetails: Boolean = false) = fetchForPath("/", withDetails)

    fun fetchBuilding(id: String, withDetails: Boolean = false) = fetchForPath("/building_id=$id", withDetails)

    fun fetchFloor(buildingId: String, floor: String, withDetails: Boolean = false)
            = fetchForPath("/building_id=$buildingId/floor=$floor", withDetails)

    fun fetchRoom(buildingId: String, floor: String, room: String, withDetails: Boolean = false): WifiCount
    {
        val info = fetchAll(true)
        val roomAP = info.accessPoints.first { it.buildingId == buildingId && it.floor == floor && it.room == room }
        return if (withDetails) WifiCount(roomAP.clientCount, listOf(roomAP)) else WifiCount(roomAP.clientCount)
    }
}
