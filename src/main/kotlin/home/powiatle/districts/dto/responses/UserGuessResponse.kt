package home.powiatle.districts.dto.responses

data class UserGuessResponse(
    val distance: String,
    val direction: String,
    val percentage: String
)