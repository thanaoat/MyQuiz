package email.com.gmail.thananon.oat.myquiz.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import email.com.gmail.thananon.oat.myquiz.models.Question
import email.com.gmail.thananon.oat.myquiz.database.QuestionRepository
import email.com.gmail.thananon.oat.myquiz.models.Choice
import email.com.gmail.thananon.oat.myquiz.models.QuestionWithChoices

private const val TAG = "QuestionViewModel"

class QuestionViewModel: ViewModel() {

    private val questionRepository = QuestionRepository.get()
    private val questionIdLiveData = MutableLiveData<Int>()

    var questionLiveData: LiveData<QuestionWithChoices?> =
        Transformations.switchMap(questionIdLiveData) { questionId ->
            questionRepository.getQuestionWithChoices(questionId)
        }

    var draftQuestion: Question? = null
    var draftChoices: MutableList<Choice>? = null

    fun loadQuestion(questionId: Int) {
        questionIdLiveData.value = questionId
    }

    private fun saveQuestion(question: Question) {
        if (question.id == 0) {
            questionRepository.insertQuestion(question)
        } else {
            questionRepository.updateQuestion(question)
        }
    }

    fun saveDraftQuestion() {
        val question = draftQuestion
        if (question != null) {
            saveQuestion(question)
        }
    }

    private fun updateDraftQuestion(question: Question?) {
        draftQuestion = draftQuestion ?: question ?: Question()
    }

    private fun updateDraftChoices(choices: MutableList<Choice>?) {
        draftChoices = choices ?: mutableListOf()
    }

    fun updateDraft(questionWithChoices: QuestionWithChoices?) {
        updateDraftQuestion(questionWithChoices?.question)
        updateDraftChoices(questionWithChoices?.choices?.toMutableList())
    }
}
