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

private const val TAG = "QuestionFragment"

class QuestionFragment: Fragment() {

    private var draftQuestionText = ""
    private lateinit var edtDraftQuestionText: EditText
    private lateinit var btnAddChoice: Button
    private lateinit var btnSave: Button

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

    override fun onStart() {
        super.onStart()

        edtDraftQuestionText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                draftQuestionText = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        btnAddChoice.setOnClickListener {
            Log.d(TAG, "btnAddChoice clicked")
        }

        btnSave.setOnClickListener {
            Log.d(TAG, "btnSave clicked")
        }
    }
}
