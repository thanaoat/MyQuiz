package email.com.gmail.thananon.oat.myquiz.database

import android.content.Context
import androidx.room.Room
import java.util.concurrent.Executors

private const val DB_NAME = "MyQuiz"

object DatabaseManager {

    private lateinit var appDatabase: AppDatabase
    private val executor = Executors.newSingleThreadExecutor()

    fun init(context: Context) {
        appDatabase = Room
            .databaseBuilder(context, AppDatabase::class.java, DB_NAME)
            .build()
    }

    fun get(): AppDatabase = appDatabase
}
