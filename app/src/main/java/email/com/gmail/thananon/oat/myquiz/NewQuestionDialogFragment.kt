package email.com.gmail.thananon.oat.myquiz

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import email.com.gmail.thananon.oat.myquiz.database.QuestionRepository
import email.com.gmail.thananon.oat.myquiz.models.Question

private const val TAG = "NewQuestion"

class NewQuestionDialogFragment: DialogFragment() {

    private lateinit var edtQuestionText: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val inflater = requireActivity().layoutInflater
            val layout = inflater.inflate(R.layout.fragment_new_question_dialog, null)
            edtQuestionText = layout.findViewById(R.id.edtQuestionText)

            val builder = AlertDialog.Builder(it)
            builder.setTitle(getString(R.string.new_question))
                .setView(layout)
                .setPositiveButton(getString(R.string.done)) { _, _ ->
                    val text = edtQuestionText.text
                    val question = Question(text = text.toString())
                    QuestionRepository.get().insertQuestion(question)
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
