package home.powiatle.district

import home.powiatle.district.domain.DistrictFacade
import home.powiatle.district.dto.responses.UserGuessResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/powiatle-ms")
class DistrictController(private val districtFacade: DistrictFacade) {

    @GetMapping("/today-image")
    fun getGeneratedNumber(): ResponseEntity<ByteArray> {
        val image: ByteArray = districtFacade.getTodayDistrictImage()

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("image/png"))
            .body(image)
    }

    @PostMapping("/take-guess")
    fun checkUserGuess(@RequestParam districtId: String): UserGuessResponse {
        return districtFacade.takeUserGuess(districtId)
    }
}