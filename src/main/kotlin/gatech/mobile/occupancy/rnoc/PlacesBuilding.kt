package gatech.mobile.occupancy.rnoc

import com.fasterxml.jackson.annotation.JsonProperty

data class PlacesBuilding(
        @JsonProperty("b_id") val buildingId: String,
        val name: String,
        val address: String,
        @JsonProperty("image_url") val imageUrl: String,
        val longitude: Double,
        val latitude: Double,
        @JsonProperty("phone_num") val phoneNum: String
)
