package email.com.gmail.thananon.oat.myquiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class QuestionListFragment : Fragment() {

    private lateinit var questionRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_question_list, container, false)

        questionRecyclerView = view.findViewById(R.id.questionRecyclerView)
        questionRecyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }
}
