package space.iqbalsyafiq.storymedia.model

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Story(
    val createdAt: String?,
    val description: String?,
    val id: String?,
    val lat: Double?,
    val lon: Double?,
    val name: String?,
    val photoUrl: String?
) : Parcelable