package gatech.mobile.occupancy.entities

data class Favorite(
        val building: String,
        val floor: Int?,
        val room: String?
)
