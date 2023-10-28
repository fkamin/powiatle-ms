package home.powiatle.numberGenerator

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class NumberGeneratorFacade(private val numberGeneratorRepository: NumberGeneratorRepository) {

    fun checkForTodayNumber(): Int {
        val today: LocalDate = LocalDate.now()
        val startOfDay: LocalDateTime = today.atStartOfDay()
        val endOfDay: LocalDateTime = today.plusDays(1).atStartOfDay().minusSeconds(1)

        val existingTimeData: NumberGenerator =
            numberGeneratorRepository.findByGeneratedDateTimeBetween(startOfDay, endOfDay)
                .orElseGet { generateNumberGenerator() }
        return existingTimeData.generatedNumber
    }

    private fun generateNumberGenerator(): NumberGenerator {
        val generatedNumber: Int = generateNumber()

        val timeData = NumberGenerator(generatedNumber = generatedNumber)
        return numberGeneratorRepository.save(timeData)
    }

    fun getGeneratedNumberForToday(): Int = checkForTodayNumber()

    private fun generateNumber(): Int = Random().nextInt(380)
}