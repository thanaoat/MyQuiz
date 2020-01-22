package email.com.gmail.thananon.oat.myquiz.Quiz

import email.com.gmail.thananon.oat.myquiz.models.QuestionWithChoices

class Quiz(questions: List<QuestionWithChoices>) {
    val questions: List<QuestionWithChoices>
    var questionIndex = 0

    init {
        this.questions = questions.shuffled()
    }

    val points = 0
    val totalPoints = this.questions.size

    fun isLastQuestion(): Boolean {
        return questionIndex == questions.size - 1
    }

    fun currentQuestion(): QuestionWithChoices? {
        return try {
            questions[questionIndex]
        } catch (e: ArrayIndexOutOfBoundsException) {
            null
        }
    }

    fun toNextQuestion() {
        if (questionIndex < questions.size - 1) {
            questionIndex++
        }
    }
}
