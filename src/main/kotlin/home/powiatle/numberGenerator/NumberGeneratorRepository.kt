package home.powiatle.numberGenerator

import org.springframework.data.mongodb.repository.MongoRepository
import java.time.LocalDateTime
import java.util.*

interface NumberGeneratorRepository : MongoRepository<NumberGenerator, String> {
    fun findByGeneratedDateTimeBetween(startOfDay: LocalDateTime, endOfDay: LocalDateTime): Optional<NumberGenerator>
}