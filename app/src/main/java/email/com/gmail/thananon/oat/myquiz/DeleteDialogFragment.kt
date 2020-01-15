package email.com.gmail.thananon.oat.myquiz

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import email.com.gmail.thananon.oat.myquiz.models.Question

private const val ARG_QUESTION = "question"

class DeleteDialogFragment: DialogFragment() {

    interface Callbacks {
        fun onConfirmDelete(question: Question)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val question =arguments?.getParcelable<Question>(ARG_QUESTION)
            question?.let {question ->
                builder.setTitle(getString(R.string.confirm_delete_question))
                    .setMessage(question.text)
                    .setPositiveButton("Yes") { _, _ ->
                        targetFragment?.let { fragment ->
                            if (question != null) {
                                (fragment as Callbacks).onConfirmDelete(question)
                            }
                        }
                    }
                    .setNegativeButton("No") { _, _ ->  }
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        fun newInstance(question: Question): DeleteDialogFragment {
            val args = Bundle().apply {
                putParcelable(ARG_QUESTION, question)
            }

            return DeleteDialogFragment().apply {
                arguments = args
            }
        }
    }
}
