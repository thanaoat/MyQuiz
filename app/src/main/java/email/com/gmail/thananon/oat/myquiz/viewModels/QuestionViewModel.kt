package email.com.gmail.thananon.oat.myquiz.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import email.com.gmail.thananon.oat.myquiz.database.Question
import email.com.gmail.thananon.oat.myquiz.database.QuestionRepository

private const val TAG = "QuestionViewModel"

class QuestionViewModel: ViewModel() {

    private val questionRepository = QuestionRepository.get()
    private val questionIdLiveData = MutableLiveData<Int>()

    var questionLiveData: LiveData<Question?> =
        Transformations.switchMap(questionIdLiveData) { questionId ->
            questionRepository.getQuestion(questionId)
        }

    var draftQuestion: Question? = null

    fun loadQuestion(questionId: Int) {
        questionIdLiveData.value = questionId
    }

    private fun saveQuestion(question: Question) {
        questionRepository.saveQuestion(question)
    }

    fun saveDraftQuestion() {
        val question = draftQuestion
        if (question != null) {
            saveQuestion(question)
        }
    }
}
