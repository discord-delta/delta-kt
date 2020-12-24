@file:Suppress("unused")

package delta.entity

import delta.entity.data.Icon
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Represents a user's serializable data.
 */
@Serializable
data class UserData(
    /**
     * The user's snowflake ID.
     */
    val id: Long,
    /**
     * The user's name.
     */
    val username: String,
    /**
     * The user's 4 digit discriminator.
     */
    val discriminator: String,
    @SerialName("avatar") private val avatarHash: String?,
    private val bot: Boolean?, private val system: Boolean?,
    @Transient private val webhook: Boolean = false,
    @SerialName("public_flags") private val flagField: Int) : Mentionable {

    /**
     * The user's type.
     */
    val type: AccountType
            get() { return when {
        this.system == true -> AccountType.SYSTEM
        this.webhook -> AccountType.WEBHOOK
        this.bot == true -> AccountType.BOT
        else -> AccountType.USER
    } }

    /**
     * The user's default avatar.
     */
    val defaultAvatar by lazy { Icon("embed/avatars/${discriminator.toInt() % 5}") }

    /**
     * The user's avatar.
     */
    val avatar by lazy { if (avatarHash != null) Icon("avatars/$id/${avatarHash}") else null }
    val tag: String
        get() = "$username#$discriminator"

    val flags: Set<AccountFlag> by lazy {
        setOf()
    }

    /**
     * Gets a string representation of this user as a mention in the form `<@id>`
     */
    override fun toString(): String {
        return "<@$id>"
    }
}

enum class AccountType {

    USER,
    BOT,
    SYSTEM,
    WEBHOOK
}

enum class AccountFlag(val offset: Int) {

    DISCORD_EMPLOYEE(0),
    SYSTEM(12),

    PARTNERED_SERVER_OWNER(1),

    HYPESQUAD_EVENTS(2),
    HYPESQUAD_BRAVERY(6),
    HYPESQUAD_BRILLIANCE(7),
    HYPESQUAD_BALANCE(8),

    BUG_HUNTER_LVL1(3),
    BUG_HUNTER_LVL2(14);
}