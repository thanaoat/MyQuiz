package email.com.gmail.thananon.oat.myquiz

import android.app.Application
import email.com.gmail.thananon.oat.myquiz.database.DatabaseManager

class MyQuizApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DatabaseManager.init(applicationContext)
    }
}
