package gatech.mobile.occupancy.controllers

import com.fasterxml.jackson.annotation.JsonInclude

data class OccupiedBuilding(
        val code: String,
        val name: String,
        @JsonInclude(JsonInclude.Include.NON_ABSENT) val count: Int?,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) val floors: List<String>
)
