package email.com.gmail.thananon.oat.myquiz.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "questions")
data class Question(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "text") var text: String? = "",
    @ColumnInfo(name = "correct_choice") var correctChoice: Int? = null
) : Parcelable
