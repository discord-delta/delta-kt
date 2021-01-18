package delta.gateway.event

import delta.entity.GuildData
import delta.entity.UnavailableGuildData
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * The base structure for events involving guilds.
 */
@Serializable
abstract class GenericGuildEvent(
    /**
     * The ID of the guild.
     */
    @Transient val guildId: Long = 0
) : GatewayEvent()

/**
 * The event dispatched when the user is added to a guild, when a guild becomes
 * available, or when the user initially connects.
 */
@Serializable
data class GuildJoinEvent(
    /**
     * The guild that was joined.
     */
    val guild: GuildData
) : GenericGuildEvent(guild.id)

/**
 * The event dispatched when the user is removed from a guild, or when a guild
 * becomes unavailable.
 */
@Serializable
data class GuildLeaveEvent(
    /**
     * The guild that was left.
     */
    val guild: UnavailableGuildData
) : GenericGuildEvent(guild.id)

/**
 * The event dispatched when a guild is modified.
 */
@Serializable
data class GuildUpdateEvent(
    /**
     * The updated information of the guild.
     */
    val guild: GuildData
) : GenericGuildEvent(guild.id)