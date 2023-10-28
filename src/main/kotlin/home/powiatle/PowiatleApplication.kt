package home.powiatle

import home.powiatle.districts.domain.DistrictFacade
import home.powiatle.number.NumberFacade
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PowiatleApplication(
    private val numberFacade: NumberFacade,
    private val districtFacade: DistrictFacade
) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        numberFacade.checkForTodayNumber()
        districtFacade.checkForDistricts()
    }
}

fun main(args: Array<String>) {
    runApplication<PowiatleApplication>(*args)
}
