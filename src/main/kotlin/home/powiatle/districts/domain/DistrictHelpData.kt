package home.powiatle.districts.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class DistrictHelpData(
    val id: Int,
    val name: String,
    @JsonProperty("centroid")
    val centroid: List<Double>
)