package email.com.gmail.thananon.oat.myquiz.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import email.com.gmail.thananon.oat.myquiz.models.Choice
import email.com.gmail.thananon.oat.myquiz.models.Question
import email.com.gmail.thananon.oat.myquiz.models.QuestionWithChoices
import java.util.concurrent.Executors

private const val DB_NAME = "MyQuiz"

class QuestionRepository private constructor(context: Context){
    private val database : AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        DB_NAME
    ).addMigrations(migration_1_to_2)
        .build()

    private val questionDao = database.questionDao()
    private val executors = Executors.newSingleThreadExecutor()

    fun getQuestions(): LiveData<List<Question>> = questionDao.getQuestions()

    fun getQuestion(id: Int): LiveData<Question?> = questionDao.getQuestion(id)

    fun getQuestionsWithChoices(): LiveData<List<QuestionWithChoices>> = questionDao.getQuestionsWithChoices()
    fun getQuestionWithChoices(id: Int): LiveData<QuestionWithChoices?> = questionDao.getQuestionWithChoices(id)

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

    fun insertChoice(choice: Choice) {
        executors.execute {
            questionDao.insertChoice(choice)
        }
    }

    fun deleteChoice(choice: Choice) {
        executors.execute {
            questionDao.deleteChoice(choice)
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
