package email.com.gmail.thananon.oat.myquiz.viewModels

import androidx.lifecycle.ViewModel
import email.com.gmail.thananon.oat.myquiz.Quiz.Quiz
import email.com.gmail.thananon.oat.myquiz.database.QuestionRepository
import email.com.gmail.thananon.oat.myquiz.models.Choice
import email.com.gmail.thananon.oat.myquiz.models.QuestionWithChoices

class QuizViewModel: ViewModel() {

    var quiz = Quiz(emptyList())
    private val questionRepository = QuestionRepository.get()
    val questionsLiveData = questionRepository.getQuestionsWithChoices()

    fun initQuiz(questions: List<QuestionWithChoices>) {
        quiz = Quiz(questions)
    }

    fun currentQuestion(): QuestionWithChoices? {
        return quiz.currentQuestion()
    }

    fun setCurrentQuestionAnswer(choice: Choice) {
        quiz.setCurrentQuestionAnswer(choice)
    }

    fun getCurrentQuestionAnswer(): String? {
        return quiz.getCurrentQuestionAnswer()
    }
}
