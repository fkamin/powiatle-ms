package home.powiatle.district.domain

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import home.powiatle.district.dto.exceptions.DistrictDoesNotExists
import home.powiatle.district.dto.responses.UserGuessResponse
import home.powiatle.numberGenerator.NumberGeneratorFacade
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.math.*

class DistrictFacade(
    private val mongoDistrictRepository: MongoDistrictRepository,
    private val objectMapper: ObjectMapper,
    private val numberGeneratorFacade: NumberGeneratorFacade
) {

    @Value("\${files.import.districts.path}")
    private val districtsImportFilePath: String = ""

    fun checkForDistricts() {
        if (mongoDistrictRepository.count() == 0L) {
            val resource = ClassPathResource(districtsImportFilePath)
            val districtsData: List<DistrictHelpData> =
                objectMapper.readValue(resource.inputStream)
            val districts: List<District> = districtsData.map {
                District(it.id.toString(), it.name, GeoJsonPoint(it.centroid[1], it.centroid[0]))
            }

            mongoDistrictRepository.saveAll(districts)
        }
    }

    fun getTodayDistrictImage(): ByteArray {
        val resource = ClassPathResource("images/")
        val imagePath: Path = Paths.get(resource.uri)
        val number: Int = numberGeneratorFacade.getGeneratedNumberForToday()

        val newImagePath: Path = imagePath.resolve("$number.png")
        return Files.readAllBytes(newImagePath)
    }

    fun takeUserGuess(districtId: String): UserGuessResponse {
        val todayNumber: String = numberGeneratorFacade.getGeneratedNumberForToday().toString()
        val todayDistrict: District =
            mongoDistrictRepository.findById(todayNumber).orElseThrow { DistrictDoesNotExists() }

        val userGuessDistrict: District =
            mongoDistrictRepository.findById(districtId).orElseThrow { DistrictDoesNotExists() }

        return getGuessResult(todayDistrict, userGuessDistrict)
    }

    private fun getGuessResult(todayDistrict: District, userGuessDistrict: District): UserGuessResponse {
        val distance: String = calculateDistance(todayDistrict.location, userGuessDistrict.location)
        val direction: String = calculateDirection(todayDistrict.location, userGuessDistrict.location)
        val percentage: String = calculatePercentage(todayDistrict.location, userGuessDistrict.location)

        return UserGuessResponse(
            distance = distance,
            direction = direction,
            percentage = percentage
        )
    }

    private fun calculateDistance(p1: GeoJsonPoint, p2: GeoJsonPoint): String {
        val distance: Double = acos(
            sin(Math.toRadians(p1.y))
                    * sin(Math.toRadians(p2.y))
                    + cos(Math.toRadians(p1.y))
                    * cos(Math.toRadians(p2.y))
                    * cos(Math.toRadians(p2.x) - Math.toRadians(p1.x))
        ) * 6371


        return distance.roundToInt().toString()
    }

    private fun calculateDirection(p1: GeoJsonPoint, p2: GeoJsonPoint): String {
        val angle: Double = atan2(p2.y - p1.y, p2.x - p1.x)
        val angleDegrees: Double = Math.toDegrees(angle)

        val normalizedAngle: Double = (angleDegrees + 360) % 360

        val sectors = 8
        val sectorSize: Double = 360.0 / sectors

        val sector: Int = ((normalizedAngle + sectorSize / 2) % 360 / sectorSize).toInt()

        return when (sector) {
            0 -> Direction.WEST
            1 -> Direction.SOUTH_WEST
            2 -> Direction.SOUTH
            3 -> Direction.SOUTH_EAST
            4 -> Direction.EAST
            5 -> Direction.NORTH_EAST
            6 -> Direction.NORTH
            7 -> Direction.NORTH_WEST
            else -> throw IllegalArgumentException("Invalid sector")
        }.toString()
    }

    private fun calculatePercentage(todayLocation: GeoJsonPoint, userGuessLocation: GeoJsonPoint): String {
        val maxDistanceDistrict: District = mongoDistrictRepository.findAll().maxBy { district ->
            calculateDistance(todayLocation, district.location).toDouble()
        }

        val maxDistance: String = calculateDistance(todayLocation, maxDistanceDistrict.location)
        val userGuessDistance: String = calculateDistance(todayLocation, userGuessLocation)

        val percentage: Double = (1 - (userGuessDistance.toDouble() / maxDistance.toDouble())) * 100

        return percentage.toInt().toString()
    }

}
