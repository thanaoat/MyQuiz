package email.com.gmail.thananon.oat.myquiz

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import email.com.gmail.thananon.oat.myquiz.database.QuestionRepository
import email.com.gmail.thananon.oat.myquiz.models.Choice
import email.com.gmail.thananon.oat.myquiz.viewModels.QuestionViewModel

private const val TAG = "QuestionFragment"
private const val ARG_QUESTION_ID = "question_id"
private const val DIALOG_CREATE_CHOICE = "DialogCreateChoice"
private const val REQUEST_ADD_CHOICE = 0

class QuestionFragment: Fragment(), NewChoiceDialogFragment.Callbacks {

    private lateinit var edtDraftQuestionText: EditText
    private lateinit var choiceRecyclerView: RecyclerView
    private lateinit var btnAddChoice: Button
    private lateinit var btnSave: Button
    private lateinit var tvCorrectChoice: TextView
    private val questionViewModel: QuestionViewModel by lazy {
        ViewModelProviders.of(this).get(QuestionViewModel::class.java)
    }
    private var adapter = ChoiceAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val questionId = arguments?.getInt(ARG_QUESTION_ID)
        questionViewModel.loadQuestion(questionId ?: 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_question, container, false)

        edtDraftQuestionText = view.findViewById(R.id.edtDraftQuestionText)
        choiceRecyclerView = view.findViewById(R.id.choiceRecyclerView)
        btnAddChoice = view.findViewById(R.id.btnAddChoice)
        btnSave = view.findViewById(R.id.btnSave)
        tvCorrectChoice = view.findViewById(R.id.tvCorrectChoice)

        choiceRecyclerView.layoutManager = LinearLayoutManager(context)
        choiceRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        questionViewModel.questionLiveData.observe(
            viewLifecycleOwner,
            Observer { questionWithChoices ->
                questionViewModel.updateDraft(questionWithChoices)
                updateUI()
            }
        )
    }

    override fun onStart() {
        super.onStart()

        edtDraftQuestionText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                questionViewModel.draftQuestion?.text = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        btnSave.setOnClickListener {
            questionViewModel.saveDraftQuestion()
        }
    }

    private fun updateUI() {
        edtDraftQuestionText.setText(questionViewModel.draftQuestion?.text)
        tvCorrectChoice.text = getCorrectChoiceText()

        btnAddChoice.apply {
            val question = questionViewModel.draftQuestion
            val choices = questionViewModel.draftChoices
            if (question != null) {
                setOnClickListener {
                    val question = questionViewModel.draftQuestion
                    if (question != null) {
                        NewChoiceDialogFragment.newInstance(question).apply {
                            setTargetFragment(this@QuestionFragment, REQUEST_ADD_CHOICE)
                            show(this@QuestionFragment.requireFragmentManager(), DIALOG_CREATE_CHOICE)
                        }
                    }
                }
            }

            isEnabled = when {
                question == null -> false
                choices != null && choices.size >= 4 -> false
                else -> true
            }
        }

        updateChoiceRecyclerView()
    }

    private fun getCorrectChoiceText(): String {
        var text = ""
        val question = questionViewModel.draftQuestion
        val choices = questionViewModel.draftChoices

        val correctChoice = choices?.find { choice ->
            choice.id == question?.correctChoice
        }

        correctChoice?.apply {
            text = correctChoice.text
        }

        return getString(
                R.string.correct_choice,
                text
        )
    }

    private fun updateChoiceRecyclerView() {
        val choices = questionViewModel.draftChoices
        Log.d(TAG, "choices: $choices")

        if (choices != null) {
            adapter = ChoiceAdapter(choices)
            choiceRecyclerView.adapter = adapter
        }
    }

    private inner class ChoiceHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var choice: Choice

        val choiceText: TextView = itemView.findViewById(R.id.choiceText)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(choice: Choice) {
            this.choice = choice

            choiceText.text = choice.text
            btnDelete.setOnClickListener {
                QuestionRepository.get().deleteChoice(choice)
                val draftQuestion = questionViewModel.draftQuestion

                if (draftQuestion?.correctChoice == choice.id) {
                    draftQuestion.correctChoice = null
                    questionViewModel.draftQuestion = draftQuestion
                }
                questionViewModel.saveDraftQuestion()
            }
        }

        override fun onClick(v: View?) {
            questionViewModel.draftQuestion?.correctChoice = choice.id
            questionViewModel.saveDraftQuestion()
        }
    }

    private inner class ChoiceAdapter(var choices: List<Choice>)
        : RecyclerView.Adapter<ChoiceHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChoiceHolder {
            val view = layoutInflater.inflate(R.layout.list_item_choice, parent, false)
            return ChoiceHolder(view)
        }

        override fun getItemCount(): Int {
            return choices.size
        }

        override fun onBindViewHolder(holder: ChoiceHolder, position: Int) {
            val choice = choices[position]
            holder.bind(choice)
        }
    }

    override fun onPositiveCallback() {
        updateChoiceRecyclerView()
    }

    companion object {
        fun newInstance(questionId: Int): QuestionFragment {
            val args = Bundle().apply {
                putInt(ARG_QUESTION_ID, questionId)
            }

            return QuestionFragment().apply {
                arguments = args
            }
        }
    }
}
