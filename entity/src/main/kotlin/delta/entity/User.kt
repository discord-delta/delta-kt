package delta.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a serializable implementation of the [Discord User](https://discord.dev/resources/user#user-object) structure.
 */
@Serializable
data class UserData(
    /**
     * The user's [Snowflake ID](https://discord.dev/reference#snowflakes). Serialized as a String.
     */
    @Serializable(with = SnowflakeSerializer::class)
    val id: Long,
    @SerialName("username")
    /**
     * The user's username, excluding their discriminator.
     */
    val name: String,
    /**
     * The user's serialized 4-digit discriminator.
     */
    val discriminator: String,
    /**
     * Whether the user is a bot. If not present, it can be assumed to be false.
     */
    val bot: Boolean? = null,
    /**
     * Whether the user is part of the urgent message system. If not present, it can be assumed to be false.
     */
    val system: Boolean? = null,
    /**
     * The user's [avatar hash](https://discord.dev/reference#image-formatting).
     */
    @SerialName("avatar")
    val avatarHash: String? = null,
    /**
     * The user's internal [account flag](https://discord.dev/user#user-object-user-flags) bitfield.
     */
    val flags: Int = 0,
    /**
     * The user's exposed [account flag](https://discord.dev/user#user-object-user-flags) bitfield.
     */
    @SerialName("public_flags")
    val publicFlags: Int = 0
)