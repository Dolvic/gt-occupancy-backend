package gatech.mobile.occupancy.rnoc

data class WifiCount(
        val clientcount: Int,
        val AccessPoints: List<WifiAccessPoint> = emptyList()
)

