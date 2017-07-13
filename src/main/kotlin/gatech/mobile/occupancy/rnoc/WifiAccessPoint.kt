package gatech.mobile.occupancy.rnoc

import com.fasterxml.jackson.annotation.JsonProperty

data class WifiAccessPoint(
        val location: String,
        val name: String,
        @JsonProperty("building_id") val buildingId: String,
        @JsonProperty("building_name") val buildingName: String,
        val floor: String,
        val room: String,
        @JsonProperty("clientcount") val clientCount: Int
)
