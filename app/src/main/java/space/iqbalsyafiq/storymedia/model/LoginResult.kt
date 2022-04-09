package space.iqbalsyafiq.storymedia.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginResult(
    val name: String?,
    val token: String?,
    val userId: String?
) : Parcelable