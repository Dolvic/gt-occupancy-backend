package gatech.mobile.occupancy.controllers

import com.fasterxml.jackson.annotation.JsonInclude

data class OccupiedRoom(
        val building: String,
        val floor: String,
        val room: String,
        @JsonInclude(JsonInclude.Include.NON_ABSENT) val count: Int?
)
