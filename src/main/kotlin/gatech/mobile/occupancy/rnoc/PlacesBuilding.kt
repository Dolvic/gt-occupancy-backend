package gatech.mobile.occupancy.rnoc

data class PlacesBuilding(
        val b_id: String,
        val name: String,
        val address: String,
        val image_url: String,
        val longitude: Double,
        val latitude: Double,
        val phone_num: String
)
