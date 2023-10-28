package home.powiatle.numberGenerator

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "todayNumber")
data class NumberGenerator(
    @Id
    val id: String? = null,
    val generatedNumber: Int,
    val generatedDateTime: LocalDateTime = LocalDateTime.now()
)
