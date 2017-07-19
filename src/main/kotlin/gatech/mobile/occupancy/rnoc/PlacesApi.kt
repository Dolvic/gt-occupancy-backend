package gatech.mobile.occupancy.rnoc

import gatech.mobile.occupancy.OccupancyConfigurationProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class PlacesApi(
        clientBuilder: RestTemplateBuilder,
        appProps: OccupancyConfigurationProperties
)
{
    private val fixContentTypeInterceptor = ClientHttpRequestInterceptor { request, body, execution ->
        val response = execution.execute(request, body)
        response.headers.contentType = MediaType.APPLICATION_JSON
        response
    }

    private val client: RestTemplate = clientBuilder.rootUri(appProps.rnoc.placesApi)
            .additionalInterceptors(fixContentTypeInterceptor)
            .build()

    private val buildingsListType = object : ParameterizedTypeReference<List<PlacesBuilding>>()
    {}

    fun fetchAllBuildings(): List<PlacesBuilding>
            = client.exchange("/buildings", HttpMethod.GET, null, buildingsListType).body
}
