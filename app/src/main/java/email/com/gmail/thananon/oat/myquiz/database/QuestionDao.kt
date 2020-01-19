package email.com.gmail.thananon.oat.myquiz.database

import androidx.lifecycle.LiveData
import androidx.room.*
import email.com.gmail.thananon.oat.myquiz.models.Choice
import email.com.gmail.thananon.oat.myquiz.models.Question
import email.com.gmail.thananon.oat.myquiz.models.QuestionWithChoices

@Dao
interface QuestionDao {
    @Query("SELECT * FROM questions")
    fun getQuestions() : LiveData<List<Question>>

    @Query("SELECT * FROM questions WHERE id = :id LIMIT 1")
    fun getQuestion(id: Int): LiveData<Question?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuestion(question: Question)

    @Delete
    fun deleteQuestion(question: Question)

    @Update
    fun updateQuestion(question: Question)

    @Transaction
    @Query("SELECT * FROM questions WHERE id = :id LIMIT 1")
    fun getQuestionWithChoices(id: Int): LiveData<QuestionWithChoices?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChoice(choice: Choice)

    @Delete
    fun deleteChoice(choice: Choice)
}
