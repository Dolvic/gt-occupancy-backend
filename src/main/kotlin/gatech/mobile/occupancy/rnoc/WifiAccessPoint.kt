package gatech.mobile.occupancy.rnoc

data class WifiAccessPoint(
        val location: String,
        val name: String,
        val building_id: String,
        val building_name: String,
        val floor: String,
        val room: String,
        val clientcount: Int
)
