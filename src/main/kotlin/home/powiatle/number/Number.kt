package home.powiatle.number

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "numbers")
data class Number(
    @Id
    val id: String = ObjectId().toString(),
    val number: Int,
    val numberGenerationDate: LocalDateTime = LocalDateTime.now()
)
