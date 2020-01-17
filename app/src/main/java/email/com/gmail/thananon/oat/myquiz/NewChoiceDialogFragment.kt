package email.com.gmail.thananon.oat.myquiz

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import email.com.gmail.thananon.oat.myquiz.database.QuestionRepository
import email.com.gmail.thananon.oat.myquiz.models.Choice
import email.com.gmail.thananon.oat.myquiz.models.Question

private const val ARG_QUESTION = "question"

class NewChoiceDialogFragment: DialogFragment() {

    private lateinit var edtChoiceText: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val inflater = requireActivity().layoutInflater
            val layout = inflater.inflate(R.layout.fragment_new_choice_dialog, null)
            edtChoiceText = layout.findViewById(R.id.edtChoiceText)

            val builder = AlertDialog.Builder(it)
            builder.setTitle(getString(R.string.new_choice))
                    .setView(layout)
                    .setPositiveButton(getString(R.string.done)) { _, _ ->
                        val text = edtChoiceText.text
                        val choice = Choice(questionId = 0, text = text.toString())
                        QuestionRepository.get().insertChoice(choice)
                    }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        fun newInstance(question: Question): NewChoiceDialogFragment {
            val args = Bundle().apply {
                putParcelable(ARG_QUESTION, question)
            }

            return NewChoiceDialogFragment().apply {
                arguments = args
            }
        }
    }
}
