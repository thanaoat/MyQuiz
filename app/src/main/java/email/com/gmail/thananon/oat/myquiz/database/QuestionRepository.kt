package email.com.gmail.thananon.oat.myquiz.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.util.concurrent.Executors

private const val DB_NAME = "MyQuiz"

class QuestionRepository private constructor(context: Context){
    private val database : AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        DB_NAME
    ).build()

    private val questionDao = database.questionDao()
    private val executors = Executors.newSingleThreadExecutor()

    fun getQuestions(): LiveData<List<Question>> = questionDao.getQuestions()

    fun getQuestion(id: Int): LiveData<Question?> = questionDao.getQuestion(id)

    fun updateQuestion(question: Question) {
        executors.execute {
            questionDao.updateQuestion(question)
        }
    }

    fun insertQuestion(question: Question) {
        executors.execute {
            questionDao.insertQuestion(question)
        }
    }

    fun deleteQuestion(question: Question) {
        executors.execute {
            questionDao.deleteQuestion(question)
        }
    }

    companion object {
        private  var INSTANCE: QuestionRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = QuestionRepository(context)
            }
        }

        fun get(): QuestionRepository {
            return INSTANCE ?:
            throw IllegalStateException("QuestionRepository must be initialized")
        }
    }
}
