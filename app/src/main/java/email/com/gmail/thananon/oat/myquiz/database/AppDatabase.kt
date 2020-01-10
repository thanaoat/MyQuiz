package email.com.gmail.thananon.oat.myquiz.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Question::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
}
