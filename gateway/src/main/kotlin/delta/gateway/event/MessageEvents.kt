package delta.gateway.event

import delta.entity.MessageData
import delta.entity.SnowflakeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * The base structure for events involving messages.
 */
@Serializable
abstract class GenericMessageEvent(
    /**
     * The ID of the message.
     */
    @Transient open val messageId: Long = 0
) : GatewayEvent()

/**
 * The event dispatched when a message is sent.
 */
@Serializable
data class MessageCreateEvent(
    /**
     * The message that was sent.
     */
    val message: MessageData
) : GenericMessageEvent(message.id)

@Serializable
data class MessageDeleteEvent(
    @SerialName("id") @Serializable(with = SnowflakeSerializer::class)
    override val messageId: Long,
    @SerialName("channel_id") @Serializable(with = SnowflakeSerializer::class)
    val channelId: Long,
    @SerialName("guild_id") @Serializable(with = SnowflakeSerializer::class)
    val guildId: Long
) : GenericMessageEvent(messageId)

/**
 * The event dispatched when a message is edited.
 */
@Serializable
data class MessageUpdateEvent(
    /**
     * The updated information of the message.
     */
    val message: MessageData
) : GenericMessageEvent(message.id)
