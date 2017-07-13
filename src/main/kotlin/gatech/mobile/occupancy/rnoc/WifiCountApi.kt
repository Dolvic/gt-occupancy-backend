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

    fun fetchAll(withDetails: Boolean = true): WifiCount
            = client.getForObject("/?details=$withDetails", WifiCount::class.java)
}
