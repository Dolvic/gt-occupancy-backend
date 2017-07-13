package gatech.mobile.occupancy

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("occupancy")
class OccupancyConfigurationProperties
{
    var rnoc: Rnoc = Rnoc()

    class Rnoc
    {
        lateinit var placesApi: String
        lateinit var wifiApi: String
    }
}
