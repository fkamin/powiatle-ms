package home.powiatle.numberGenerator

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class NumberGeneratorConfig {

    @Bean
    fun numberGenerationFacade(numberGeneratorRepository: NumberGeneratorRepository): NumberGeneratorFacade =
        NumberGeneratorFacade(numberGeneratorRepository = numberGeneratorRepository)
}