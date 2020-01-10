package email.com.gmail.thananon.oat.myquiz.database

import androidx.room.*

@Dao
interface QuestionDao {
    @Query("SELECT * FROM questions")
    fun getQuestions() : List<Question>

    @Query("SELECT * FROM questions WHERE id = :id LIMIT 1")
    fun getQuestion(id: Int): Question

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuestion(question: Question)

    @Delete
    fun deleteQuestion(question: Question)

    @Update
    fun updateQuestion(question: Question)
}
