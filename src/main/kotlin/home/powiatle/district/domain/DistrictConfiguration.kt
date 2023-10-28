package home.powiatle.district.domain

import com.fasterxml.jackson.databind.ObjectMapper
import home.powiatle.numberGenerator.NumberGeneratorFacade
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DistrictConfiguration {

    @Bean
    fun provinceFacade(
        mongoDistrictRepository: MongoDistrictRepository,
        objectMapper: ObjectMapper,
        numberGeneratorFacade: NumberGeneratorFacade
    ): DistrictFacade =
        DistrictFacade(
            mongoDistrictRepository = mongoDistrictRepository,
            objectMapper = objectMapper,
            numberGeneratorFacade = numberGeneratorFacade)
}