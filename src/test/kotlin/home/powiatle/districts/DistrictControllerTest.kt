package home.powiatle.districts

import com.fasterxml.jackson.databind.ObjectMapper
import home.powiatle.MongoTestContainer
import home.powiatle.districts.domain.Direction
import home.powiatle.districts.domain.District
import home.powiatle.districts.domain.DistrictFacade
import home.powiatle.districts.domain.DistrictRepository
import home.powiatle.districts.dto.responses.UserGuessResponse
import home.powiatle.number.Number
import home.powiatle.number.NumberRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DistrictControllerTest {

    private val mongo: MongoDBContainer = MongoTestContainer.instance

    init {
        System.setProperty("spring.data.mongodb.uri", mongo.replicaSetUrl)
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(DistrictControllerTest::class.java)
    }

    @Autowired
    private lateinit var districtFacade: DistrictFacade

    @Autowired
    private lateinit var districtRepository: DistrictRepository

    @Autowired
    private lateinit var numberRepository: NumberRepository

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun cleanBefore() {
        mongoTemplate.dropCollection("districts")
        mongoTemplate.dropCollection("numbers")
    }

    @Nested
    inner class DistrictOperationTests {
        private val baseUrl: String = "/powiatle-ms"

        @Test
        fun `should return image for today`() {
            // given
            val expectedByteArray: ByteArray = thereIsTodayByteArray()

            // when
            val result: MvcResult = mockMvc.perform(
                get("$baseUrl/today-image")
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andReturn()

            // then
            val responseJsonByteArray: ByteArray = result.response.contentAsByteArray

            Assertions.assertThat(responseJsonByteArray).isEqualTo(expectedByteArray)
        }

        @Test
        fun `should return info about user attempt`() {
            // given
            thereAreDistricts()
            thereIsTodayNumber()
            val userGuessDistrictId = 230

            val expectedDistance = 436
            val expectedDirection = Direction.NORTH_EAST
            val expectedPercentage = 23

            // when
            val result: MvcResult = mockMvc.perform(
                post("$baseUrl/take-guess")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("districtId", userGuessDistrictId.toString())
            )
                .andReturn()

            // then
            val responseJson: String = result.response.contentAsString
            val userResponse: UserGuessResponse = objectMapper.readValue(responseJson, UserGuessResponse::class.java)

            logger.info(responseJson)

            Assertions.assertThat(userResponse.distance.toInt()).isEqualTo(expectedDistance)
            Assertions.assertThat(userResponse.direction.toDirection()).isEqualTo(expectedDirection)
            Assertions.assertThat(userResponse.percentage.toInt()).isEqualTo(expectedPercentage)
        }
    }

    private fun thereIsTodayByteArray(): ByteArray = districtFacade.getTodayDistrictImage()

    private fun thereIsTodayNumber(): Int {
        return numberRepository.save(
            Number(
                number = 55
            )
        ).number
    }

    private fun thereAreDistricts() {
        districtRepository.saveAll(
            mutableListOf(
                District(
                    id = "55",
                    name = "ostrołęka",
                    location = GeoJsonPoint(53.080831791907535, 21.594891412056143)
                ),
                District(
                    id = "230",
                    name = "kluczborski",
                    location = GeoJsonPoint(51.03302415300571, 18.176940546448073)
                ),
                District(
                    id = "310",
                    name = "sławieński",
                    location = GeoJsonPoint(54.323155760952226, 16.581447201823263)
                )
            )
        )
    }
}

fun String.toDirection(): Direction {
    return when (this) {
        "WEST" -> Direction.WEST
        "SOUTH_WEST" -> Direction.SOUTH_WEST
        "SOUTH" -> Direction.SOUTH
        "SOUTH_EAST" -> Direction.SOUTH_EAST
        "EAST" -> Direction.EAST
        "NORTH_EAST" -> Direction.NORTH_EAST
        "NORTH" -> Direction.NORTH
        "NORTH_WEST" -> Direction.NORTH_WEST
        else -> throw IllegalArgumentException("Invalid direction")
    }
}