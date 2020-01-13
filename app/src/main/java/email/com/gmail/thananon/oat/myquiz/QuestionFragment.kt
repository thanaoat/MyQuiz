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
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import email.com.gmail.thananon.oat.myquiz.database.Question
import email.com.gmail.thananon.oat.myquiz.database.QuestionRepository
import email.com.gmail.thananon.oat.myquiz.viewModels.QuestionViewModel

private const val TAG = "QuestionFragment"
private const val ARG_QUESTION_ID = "question_id"

class QuestionFragment: Fragment() {

    private lateinit var edtDraftQuestionText: EditText
    private lateinit var btnAddChoice: Button
    private lateinit var btnSave: Button
    private val questionViewModel: QuestionViewModel by lazy {
        ViewModelProviders.of(this).get(QuestionViewModel::class.java)
    }

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
        btnAddChoice = view.findViewById(R.id.btnAddChoice)
        btnSave = view.findViewById(R.id.btnSave)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        questionViewModel.questionLiveData.observe(
            viewLifecycleOwner,
            Observer { question ->
                questionViewModel.updateDraftQuestion(question)
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

        btnAddChoice.setOnClickListener {
            Log.d(TAG, "btnAddChoice clicked")
        }

        btnSave.setOnClickListener {
            questionViewModel.saveDraftQuestion()
        }
    }

    private fun updateUI() {
        edtDraftQuestionText.setText(questionViewModel.draftQuestion?.text)
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
