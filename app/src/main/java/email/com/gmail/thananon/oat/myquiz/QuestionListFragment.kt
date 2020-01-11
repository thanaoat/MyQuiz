package email.com.gmail.thananon.oat.myquiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import email.com.gmail.thananon.oat.myquiz.database.Question

class QuestionListFragment : Fragment() {

    private lateinit var questionRecyclerView: RecyclerView
    private var adapter: QuestionAdapter = QuestionAdapter(emptyList())

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
