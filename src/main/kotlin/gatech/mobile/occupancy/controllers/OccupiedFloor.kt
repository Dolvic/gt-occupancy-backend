package gatech.mobile.occupancy.controllers

import com.fasterxml.jackson.annotation.JsonInclude

data class OccupiedFloor(
        val building: String,
        val floor: String,
        @JsonInclude(JsonInclude.Include.NON_ABSENT) val count: Int?,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) val rooms: List<String>
)
