package email.com.gmail.thananon.oat.myquiz.viewModels

import androidx.lifecycle.ViewModel
import email.com.gmail.thananon.oat.myquiz.database.QuestionRepository

class QuestionListViewModel: ViewModel() {

    private val questionRepository = QuestionRepository.get()
    val questionsLiveData = questionRepository.getQuestions()
}
