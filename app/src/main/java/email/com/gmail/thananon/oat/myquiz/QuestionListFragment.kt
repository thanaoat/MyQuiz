package email.com.gmail.thananon.oat.myquiz

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import email.com.gmail.thananon.oat.myquiz.database.Question
import email.com.gmail.thananon.oat.myquiz.viewModels.QuestionListViewModel

private const val TAG = "QuestionListFragment"

class QuestionListFragment : Fragment() {

    interface Callbacks {
        fun onQuestionSelected(questionId: Int)
    }

    private var callbacks: Callbacks? = null

    private lateinit var questionRecyclerView: RecyclerView
    private var adapter: QuestionAdapter = QuestionAdapter(emptyList())
    private val questionListViewModel: QuestionListViewModel by lazy {
        ViewModelProviders.of(this).get(QuestionListViewModel::class.java)

}
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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
        questionListViewModel.questionsLiveData.observe(
            viewLifecycleOwner,
            Observer { questions ->
                questions?.let {
                    Log.d(TAG, "questions.size: ${questions.size}")
                    updateUI(questions)
                }
            }
        )
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_question_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.new_question -> {
                callbacks?.onQuestionSelected(0)
//                questionListViewModel.addQuestion(question)
//                callbacks?.onQuestionSelected()

                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateUI(questions: List<Question>) {
        adapter = QuestionAdapter(questions)
        questionRecyclerView.adapter = adapter
    }

    private inner class QuestionHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var question: Question

        val questionText: TextView = itemView.findViewById(R.id.questionText)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(question: Question, position: Int) {
            this.question = question
            questionText.text = getString(R.string.question_template, position, question.text)
        }

        override fun onClick(v: View?) {
            callbacks?.onQuestionSelected(question.id)
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
            holder.bind(question, position + 1)
        }
    }
}
