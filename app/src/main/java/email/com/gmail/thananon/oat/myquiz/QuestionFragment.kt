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
import email.com.gmail.thananon.oat.myquiz.models.Choice
import email.com.gmail.thananon.oat.myquiz.viewModels.QuestionViewModel

private const val TAG = "QuestionFragment"
private const val ARG_QUESTION_ID = "question_id"
private const val DIALOG_CREATE_CHOICE = "DialogCreateChoice"

class QuestionFragment: Fragment() {

    private lateinit var edtDraftQuestionText: EditText
    private lateinit var choiceRecyclerView: RecyclerView
    private lateinit var btnAddChoice: Button
    private lateinit var btnSave: Button
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

        choiceRecyclerView.layoutManager = LinearLayoutManager(context)
        choiceRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        questionViewModel.questionLiveData.observe(
            viewLifecycleOwner,
            Observer { question ->
                questionViewModel.updateDraft(question)
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

        btnAddChoice.apply {
            val question = questionViewModel.draftQuestion
            if (question != null) {
                setOnClickListener {
                    val question = questionViewModel.draftQuestion
                    if (question != null) {
                        NewChoiceDialogFragment.newInstance(question)
                                .show(this@QuestionFragment.requireFragmentManager(), DIALOG_CREATE_CHOICE)
                    }
                }
            } else {
                isEnabled = false
            }
        }

        updateChoiceRecyclerView()
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
        : RecyclerView.ViewHolder(view) {

        private lateinit var choice: Choice

        val choiceText: TextView = itemView.findViewById(R.id.choiceText)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)

        fun bind(choice: Choice) {
            this.choice = choice
            choiceText.text = choice.text
            btnDelete.setOnClickListener {
                Log.d(TAG, "btnDelete clicked")
            }
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
