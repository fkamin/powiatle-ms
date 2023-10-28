package home.powiatle.districts.domain

import com.fasterxml.jackson.databind.ObjectMapper
import home.powiatle.number.NumberFacade
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DistrictConfiguration {

    @Bean
    fun districtFacade(
        repository: DistrictRepository,
        objectMapper: ObjectMapper,
        numberFacade: NumberFacade
    ): DistrictFacade =
        DistrictFacade(
            districtRepository = repository,
            objectMapper = objectMapper,
            numberFacade = numberFacade
        )

}