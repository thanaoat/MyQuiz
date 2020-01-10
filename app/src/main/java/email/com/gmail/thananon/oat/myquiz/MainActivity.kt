package email.com.gmail.thananon.oat.myquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import email.com.gmail.thananon.oat.myquiz.database.DatabaseManager
import email.com.gmail.thananon.oat.myquiz.database.Question
import java.util.concurrent.Executors

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val executor = Executors.newSingleThreadExecutor()
        val questionDao = DatabaseManager.get().questionDao()

        executor.execute {
            questionDao.insertQuestion(
                Question(1, "What name is assigned to unknown male murder victim", null)
            )
            questionDao.insertQuestion(
                Question(2, "What name is assigned to unknown female murder victim", null)
            )
            val questions = questionDao.getQuestions()
            Log.d(TAG, "questions.size: ${questions.size}")
            Log.d(TAG, "questions: $questions")
        }

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = QuestionListFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }
}
