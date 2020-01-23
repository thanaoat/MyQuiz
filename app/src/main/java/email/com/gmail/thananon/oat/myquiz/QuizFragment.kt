package email.com.gmail.thananon.oat.myquiz

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import email.com.gmail.thananon.oat.myquiz.models.Choice
import email.com.gmail.thananon.oat.myquiz.models.QuestionWithChoices
import email.com.gmail.thananon.oat.myquiz.viewModels.QuestionListViewModel
import email.com.gmail.thananon.oat.myquiz.viewModels.QuizViewModel

private const val TAG = "QuizFragment"

class QuizFragment: Fragment() {

    private lateinit var tvQuestion: TextView
    private lateinit var tvAnswer: TextView
    private lateinit var choiceRecyclerView: RecyclerView
    private lateinit var btnNext: Button
    private lateinit var btnCheckScore: Button
    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }
    private var adapter = ChoiceAdapter(emptyList())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_quiz, container, false)

        tvQuestion = view.findViewById(R.id.tvQuestion)
        tvAnswer = view.findViewById(R.id.tvAnswer)
        choiceRecyclerView = view.findViewById(R.id.choiceRecyclerView)
        btnNext = view.findViewById(R.id.btnNext)
        btnCheckScore = view.findViewById(R.id.btnCheckScore)

        btnNext.setOnClickListener {
            quizViewModel.quiz.toNextQuestion()
            updateUI()
        }

        btnCheckScore.setOnClickListener {
            showScore()
        }

        choiceRecyclerView.layoutManager = LinearLayoutManager(context)
        choiceRecyclerView.adapter = adapter

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

    private fun updateUI() {
        val questionWithChoices = quizViewModel.currentQuestion()
        if (questionWithChoices != null) {
            tvQuestion.text = questionWithChoices.question.text
            tvAnswer.text = getString(R.string.answer, quizViewModel.getCurrentQuestionAnswer() ?: "")
            adapter = ChoiceAdapter(questionWithChoices.choices)
            choiceRecyclerView.adapter = adapter
        } else {
            tvQuestion.text = ""
        }

        val isLastQuestion = quizViewModel.quiz.isLastQuestion()
        btnNext.isEnabled = !isLastQuestion
        if (isLastQuestion) {
            btnNext.visibility = View.GONE
            btnCheckScore.visibility = View.VISIBLE
        } else {
            btnNext.visibility = View.VISIBLE
            btnCheckScore.visibility = View.GONE
        }
    }

    private fun showScore() {
        val points = quizViewModel.quiz.points
        val totalPoint = quizViewModel.quiz.totalPoints

        Toast.makeText(
            context,
            getString(R.string.score, points.toString(), totalPoint.toString()),
            Toast.LENGTH_SHORT
        ).show()
    }

    private inner class ChoiceHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {

        private val btnChoice: Button = itemView.findViewById(R.id.btnChoice)
        private var choice: Choice? = null

        init {
            btnChoice.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val choice = this.choice
            if (choice != null) {
                quizViewModel.setCurrentQuestionAnswer(choice)
                updateUI()
            }
        }

        fun bind(choice: Choice) {
            this.choice = choice
            btnChoice.text = this.choice?.text
        }
    }

    private inner class ChoiceAdapter(private val choices: List<Choice>): RecyclerView.Adapter<ChoiceHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChoiceHolder {
            val view = layoutInflater.inflate(R.layout.list_item_choice_button, parent, false)
            return ChoiceHolder(view)
        }

        override fun getItemCount(): Int = choices.size

        override fun onBindViewHolder(holder: ChoiceHolder, position: Int) {
            val choice = choices[position]
            holder.bind(choice)
        }
    }
}
