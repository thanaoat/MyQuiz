package email.com.gmail.thananon.oat.myquiz

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import email.com.gmail.thananon.oat.myquiz.viewModels.QuestionListViewModel
import email.com.gmail.thananon.oat.myquiz.viewModels.QuizViewModel

private const val TAG = "QuizFragment"

class QuizFragment: Fragment() {

    private lateinit var tvQuestion: TextView
    private lateinit var choiceRecyclerView: RecyclerView
    private lateinit var btnNext: Button
    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_quiz, container, false)

        tvQuestion = view.findViewById(R.id.tvQuestion)
        choiceRecyclerView = view.findViewById(R.id.choiceRecyclerView)
        btnNext = view.findViewById(R.id.btnNext)

        btnNext.setOnClickListener {
            quizViewModel.quiz.toNextQuestion()
            updateUI()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         quizViewModel.questionsLiveData.observe(
             viewLifecycleOwner,
             Observer { questions ->
                 quizViewModel.initQuiz(questions)
                 updateUI()
             }
         )
    }

    fun updateUI() {
        val questionWithChoices = quizViewModel.currentQuestion()
        if (questionWithChoices != null) {
            tvQuestion.text = questionWithChoices.question.text
        } else {
            tvQuestion.text = ""
        }

        btnNext.isEnabled = !quizViewModel.quiz.isLastQuestion()
    }
}
