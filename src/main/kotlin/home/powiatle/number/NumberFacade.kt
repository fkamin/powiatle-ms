package home.powiatle.number

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class NumberFacade(private val numberRepository: NumberRepository) {

    fun checkForTodayNumber(): Int {
        val today: LocalDate = LocalDate.now()
        val startOfDay: LocalDateTime = today.atStartOfDay()
        val endOfDay: LocalDateTime = today.plusDays(1).atStartOfDay().minusSeconds(1)

        val existingTimeData: Number =
            numberRepository.findByNumberGenerationDateBetween(startOfDay, endOfDay)
                .orElseGet { generateNumber() }
        return existingTimeData.number
    }

    private fun generateNumber(): Number {
        val generatedNumber: Int = Random().nextInt(380)

        val number = Number(number = generatedNumber)
        return numberRepository.save(number)
    }

    fun getGeneratedNumberForToday(): Int = checkForTodayNumber()

}