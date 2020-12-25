package delta.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val id: Long,
    @SerialName("username")
    val name: String,
    val discriminator: String,
    val bot: Boolean = false,
    val system: Boolean = false,
    @SerialName("avatar")
    val avatarHash: String? = null,
    val flags: Int = 0,
    @SerialName("public_flags")
    val publicFlags: Int = 0
)