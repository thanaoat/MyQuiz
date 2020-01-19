package email.com.gmail.thananon.oat.myquiz

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import email.com.gmail.thananon.oat.myquiz.models.Question
import email.com.gmail.thananon.oat.myquiz.viewModels.QuestionListViewModel

private const val TAG = "QuestionListFragment"
private const val REQUEST_DELETE = 0
private const val DIALOG_DELETE = "DialogDelete"
private const val DIALOG_CREATE = "DialogCreate"

class QuestionListFragment : Fragment(), DeleteDialogFragment.Callbacks {

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
                NewQuestionDialogFragment().show(
                    this@QuestionListFragment.requireFragmentManager(),
                    DIALOG_CREATE
                )
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateUI(questions: List<Question>) {
        adapter = QuestionAdapter(questions)
        questionRecyclerView.adapter = adapter
    }

    override fun onConfirmDelete(question: Question) {
        questionListViewModel.deleteQuestion(question)
    }

    private inner class QuestionHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var question: Question

        val questionText: TextView = itemView.findViewById(R.id.questionText)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(question: Question, position: Int) {
            this.question = question
            questionText.text = getString(R.string.question_template, position, question.text)
            btnDelete.setOnClickListener {
                DeleteDialogFragment.newInstance(this.question).apply {
                    setTargetFragment(this@QuestionListFragment, REQUEST_DELETE)
                    show(this@QuestionListFragment.requireFragmentManager(), DIALOG_DELETE)
                }
            }
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
