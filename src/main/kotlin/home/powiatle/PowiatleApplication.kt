package home.powiatle

import home.powiatle.district.domain.DistrictFacade
import home.powiatle.numberGenerator.NumberGeneratorFacade
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PowiatleApplication(
    private val numberGeneratorFacade: NumberGeneratorFacade,
    private val districtFacade: DistrictFacade
) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        numberGeneratorFacade.checkForTodayNumber()
        districtFacade.checkForDistricts()
    }
}

fun main(args: Array<String>) {
    runApplication<PowiatleApplication>(*args)
}
