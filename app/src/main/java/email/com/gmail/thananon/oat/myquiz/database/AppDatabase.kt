package email.com.gmail.thananon.oat.myquiz.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import email.com.gmail.thananon.oat.myquiz.models.Choice
import email.com.gmail.thananon.oat.myquiz.models.Question

@Database(entities = [Question::class, Choice::class], version = 2, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
}

val migration_1_to_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS `choices` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `question_id` INTEGER NOT NULL, `text` TEXT NOT NULL)"
        )
    }
}
