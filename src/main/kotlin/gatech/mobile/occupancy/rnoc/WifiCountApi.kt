package gatech.mobile.occupancy.rnoc

import gatech.mobile.occupancy.OccupancyConfigurationProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class WifiCountApi(clientBuilder: RestTemplateBuilder, appProps: OccupancyConfigurationProperties
)
{
    private val client: RestTemplate = clientBuilder.rootUri(appProps.rnoc.wifiApi)
            .build()

    private fun fetchForPath(path: String, withDetails: Boolean = false): WifiCount
            = client.getForObject("$path/?details=$withDetails", WifiCount::class.java)

    fun fetchAll(withDetails: Boolean = false) = fetchForPath("/", withDetails)

    fun fetchBuilding(id: String, withDetails: Boolean = false) = fetchForPath("/building_id=$id", withDetails)

    fun fetchFloor(buildingId: String, floor: String, withDetails: Boolean = false)
            = fetchForPath("/building_id=$buildingId/floor=$floor", withDetails)
}
