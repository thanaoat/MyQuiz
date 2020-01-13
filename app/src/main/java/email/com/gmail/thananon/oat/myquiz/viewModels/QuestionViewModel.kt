package email.com.gmail.thananon.oat.myquiz.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import email.com.gmail.thananon.oat.myquiz.database.Question
import email.com.gmail.thananon.oat.myquiz.database.QuestionRepository

private const val TAG = "QuestionViewModel"

class QuestionViewModel: ViewModel() {

    private val questionRepository = QuestionRepository.get()
    var draftQuestion: Question? = null

    fun saveQuestion(question: Question) {
        questionRepository.saveQuestion(question)
    }
}
