package home.powiatle.district.dto.responses

data class UserGuessResponse(
    val distance: String,
    val direction: String,
    val percentage: String
)