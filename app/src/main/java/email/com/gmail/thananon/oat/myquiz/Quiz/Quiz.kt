package email.com.gmail.thananon.oat.myquiz.Quiz

import android.util.Log
import email.com.gmail.thananon.oat.myquiz.models.Choice
import email.com.gmail.thananon.oat.myquiz.models.QuestionWithChoices


private const val TAG = "Quiz"
private const val MAX_QUESTIONS = 10

class Quiz(questions: List<QuestionWithChoices>) {
    val questions: List<QuestionWithChoices>
    val answers: MutableList<Int?>
    var currentQuestionIndex = 0

    init {
        this.questions = questions.shuffled()
        val tempAnswers: MutableList<Int?> = mutableListOf()
        this.questions.forEachIndexed { index, questionWithChoices ->
            tempAnswers.add(index, null)
        }
        answers = tempAnswers
    }

    val points: Int
        get() {
            var tempPoints = 0
            questions.forEachIndexed { index, questionWithChoices ->
                if (answers[index] == questionWithChoices.question.correctChoice) {
                    tempPoints++
                }
            }

            return tempPoints
        }
    val totalPoints = this.questions.size

    fun isLastQuestion(): Boolean {
        return currentQuestionIndex == questions.size - 1
    }

    fun currentQuestion(): QuestionWithChoices? {
        return try {
            questions[currentQuestionIndex]
        } catch (e: ArrayIndexOutOfBoundsException) {
            null
        }
    }

    fun toNextQuestion() {
        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
        }
    }

    fun setCurrentQuestionAnswer(choice: Choice) {
        answers[currentQuestionIndex] = choice.id
    }

    fun getCurrentQuestionAnswer(): String? {
        val selectedChoice = answers[currentQuestionIndex]
        val choices = questions[currentQuestionIndex].choices
        val temp = if (selectedChoice != null) {
            choices.find { choice ->
                choice.id == selectedChoice
            }?.text
        } else {
            null
        }

        Log.d(TAG, "temp: $temp")

        return temp
    }
}
