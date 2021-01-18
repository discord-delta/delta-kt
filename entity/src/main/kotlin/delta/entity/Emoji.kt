package delta.entity

import kotlinx.serialization.Serializable

/**
 * Represents a serializable implementation of the [Discord Emoji](https://discord.dev/resources/emoji#emoji-object) structure.
 */
@Serializable
data class EmojiData(
    /**
     * The emoji's [Snowflake ID](https://discord.dev/reference#snowflakes). Serialized as a String.
     */
    @Serializable(with = SnowflakeSerializer::class)
    val id: Long,
    /**
     * The emoji's name. Null if the emoji is from a reaction and is a unicode emoji.
     */
    val name: String?,
    /**
     * The list of roles required to use this emoji.
     */
    val roles: List<Long> = listOf(),
    /**
     * The user who created this emoji.
     */
    val user: UserData? = null,
    /**
     * Whether this emoji requires colons to be used.
     */
    val requiresColons: Boolean? = null,
    /**
     * Whether this emoji is managed by an integration.
     */
    val managed: Boolean? = null,
    /**
     * Whether this emoji is animated.
     */
    val animated: Boolean? = null,
    /**
     * Whether this emoji is available. False is the guild lost boosts and the emoji limit is exceeded.
     */
    val available: Boolean? = null
)