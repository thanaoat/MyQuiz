package email.com.gmail.thananon.oat.myquiz.models

import androidx.room.Embedded
import androidx.room.Relation

data class QuestionWithChoices(
    @Embedded val question: Question,
    @Relation(
        parentColumn = "id",
        entityColumn = "question_id"
    )
    val choices: List<Choice>
)
