package email.com.gmail.thananon.oat.myquiz

import android.app.Application
import email.com.gmail.thananon.oat.myquiz.database.QuestionRepository

class MyQuizApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        QuestionRepository.initialize(this)
    }
}
