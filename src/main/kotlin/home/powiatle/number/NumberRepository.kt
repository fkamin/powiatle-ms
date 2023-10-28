package home.powiatle.number

import org.springframework.data.mongodb.repository.MongoRepository
import java.time.LocalDateTime
import java.util.*

interface NumberRepository : MongoRepository<Number, String> {
//    fun findByNumberGenerationDateBetween(startOfDay: LocalDateTime, endOfDay: LocalDateTime): Optional<Number>

    fun findByNumberGenerationDateBetween(startOfDay: LocalDateTime, endOfDay: LocalDateTime): Optional<Number>

}