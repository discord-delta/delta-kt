package delta.entity

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class ChannelData(
    @Serializable(with = SnowflakeSerializer::class)
    val id: Long,
    val type: ChannelType,
    @SerialName("guild_id") @Serializable(with = SnowflakeSerializer::class)
    val guildId: Long? = null,
    val position: Int? = null,
    @SerialName("permission_overwrites")
    val overwrites: List<PermissionOverwrite> = listOf(),
    val name: String? = null,
    val topic: String? = null,
    val nsfw: Boolean? = false,
    @SerialName("last_message_id") @Serializable(with = SnowflakeSerializer::class)
    val lastMessageId: Long? = null,
    val bitrate: Int? = null,
    @SerialName("user_limit")
    val userLimit: Int? = null,
    @SerialName("rate_limit_per_user")
    val rateLimit: Int? = null,
    val recipients: List<UserData> = listOf(),
    @SerialName("icon")
    val iconHash: String? = null,
    @SerialName("owner_id") @Serializable(with = SnowflakeSerializer::class)
    val ownerId: Long? = null, @Serializable(with = SnowflakeSerializer::class)
    @SerialName("parent_id")
    val parentId: Long? = null,
    @SerialName("last_pin_timestamp")
    val pinTimestamp: String? = null
) {

    override fun equals(other: Any?): Boolean {
        return this === other || this.id == (other as? ChannelData)?.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

@Serializable(with = ChannelType.Serializer::class)
sealed class ChannelType(val id: Int) {

    object GuildText : ChannelType(0)
    object DirectMessage : ChannelType(1)
    object GuildVoice : ChannelType(2)
    object GroupDirectMessage : ChannelType(3)
    object GuildCategory : ChannelType(4)
    object GuildNews : ChannelType(5)
    object GuildStore : ChannelType(6)

    // Unreleased thread channel types
    object AnnouncementThread : ChannelType(10)
    object PublicThread : ChannelType(11)
    object PrivateThread : ChannelType(12)

    class Unknown(id: Int) : ChannelType(id)

    override fun toString(): String {
        return this::class.simpleName!!
    }

    object Serializer : KSerializer<ChannelType> {

        override val descriptor = PrimitiveSerialDescriptor("Delta.ChannelType", PrimitiveKind.INT)

        override fun serialize(encoder: Encoder, value: ChannelType) {
            encoder.encodeInt(value.id)
        }

        override fun deserialize(decoder: Decoder): ChannelType = when(val id = decoder.decodeInt()) {
            0 -> GuildText
            1 -> DirectMessage
            2 -> GuildVoice
            3 -> GroupDirectMessage
            4 -> GuildCategory
            5 -> GuildNews
            6 -> GuildStore
            10 -> AnnouncementThread
            11 -> PublicThread
            12 -> PrivateThread
            else -> Unknown(id)
        }
    }
}