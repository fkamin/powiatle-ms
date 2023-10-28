package home.powiatle.number

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class NumberConfiguration {

    @Bean
    fun numberFacade(numberRepository: NumberRepository): NumberFacade =
        NumberFacade(numberRepository = numberRepository)

}