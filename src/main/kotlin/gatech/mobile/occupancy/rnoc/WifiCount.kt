package gatech.mobile.occupancy.rnoc

import com.fasterxml.jackson.annotation.JsonProperty

data class WifiCount(
        @JsonProperty("clientcount") val clientCount: Int,
        @JsonProperty("AccessPoints") val accessPoints: List<WifiAccessPoint> = emptyList()
)

