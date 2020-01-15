package email.com.gmail.thananon.oat.myquiz.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "choices")
data class Choice(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "question_id") val questionId: Int = 0,
    @ColumnInfo(name = "text") var text: String = ""
): Parcelable
