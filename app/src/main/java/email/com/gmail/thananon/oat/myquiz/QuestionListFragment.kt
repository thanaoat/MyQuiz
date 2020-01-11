package email.com.gmail.thananon.oat.myquiz

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import email.com.gmail.thananon.oat.myquiz.database.Question
import email.com.gmail.thananon.oat.myquiz.database.QuestionRepository

private const val TAG = "QuestionListFragment"

class QuestionListFragment : Fragment() {

    private lateinit var questionRecyclerView: RecyclerView
    private var adapter: QuestionAdapter = QuestionAdapter(emptyList())
    private var questionsLiveData: LiveData<List<Question>> = QuestionRepository.get().getQuestions()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_question_list, container, false)

        questionRecyclerView = view.findViewById(R.id.questionRecyclerView)
        questionRecyclerView.layoutManager = LinearLayoutManager(context)
        questionRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        questionsLiveData.observe(
            viewLifecycleOwner,
            Observer { questions ->
                questions?.let {
                    Log.d(TAG, "questions.size: ${questions.size}")
                    updateUI(questions)
                }
            }
        )
    }

    fun updateUI(questions: List<Question>) {
        adapter = QuestionAdapter(questions)
        questionRecyclerView.adapter = adapter
    }

    private inner class QuestionHolder(view: View)
        : RecyclerView.ViewHolder(view) {
        private lateinit var question: Question

        val questionText: TextView = itemView.findViewById(R.id.questionText)

        fun bind(question: Question) {
            this.question = question
            questionText.text = question.text
        }
    }

    private inner class QuestionAdapter(var questions: List<Question>)
        : RecyclerView.Adapter<QuestionHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionHolder {
            val view = layoutInflater.inflate(R.layout.list_item_question, parent, false)
            return QuestionHolder(view)
        }

        override fun getItemCount(): Int {
            return questions.size
        }

        override fun onBindViewHolder(holder: QuestionHolder, position: Int) {
            val question = questions[position]
            holder.bind(question)
        }
    }
}
