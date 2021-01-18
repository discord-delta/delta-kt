@file:Suppress("RemoveRedundantQualifierName")

package delta.entity

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class MessageData(
    @Serializable(with = SnowflakeSerializer::class)
    val id: Long,
    @SerialName("channel_id") @Serializable(with = SnowflakeSerializer::class)
    val channel: Long,
    @SerialName("guild_id") @Serializable(with = SnowflakeSerializer::class)
    val guild: Long,
    val author: UserData,
    val member: GuildMemberData? = null,
    val content: String,
    val timestamp: String,
    @SerialName("edited_timestamp")
    val editedTimestamp: String?,
    val tts: Boolean,
    @SerialName("mention_everyone")
    val mentionsEveryone: Boolean,
    @SerialName("mentions")
    val mentionedUsers: List<UserData> = listOf(),
    @SerialName("mention_roles")
    val mentionedRoles: List<Long> = listOf(),
    @SerialName("mentioned_channels")
    val mentionedChannels: List<Long> = listOf(),
    val attachments: List<MessageAttachment> = listOf(),
    val embeds: List<MessageEmbed> = listOf(),
    val reactions: List<MessageReaction> = listOf(),
    val pinned: Boolean,
    @SerialName("webhook_id") @Serializable(with = SnowflakeSerializer::class)
    val webhookId: Long? = null,
    val type: MessageType,
    val activity: MessageActivity? = null,
    val application: MessageApplication? = null,
    @SerialName("message_reference")
    val reference: MessageReference? = null,
    val flags: Int = 0,
    @SerialName("referenced_message")
    val referencedMessage: MessageData? = null
)

@Serializable
data class MessageAttachment(
    @Serializable(with = SnowflakeSerializer::class)
    val id: Long,
    val filename: String,
    val size: Int,
    val url: String,
    @SerialName("proxy_url")
    val proxiedUrl: String,
    val width: Int,
    val height: Int
)

@Serializable
data class MessageEmbed(
    val title: String? = null,
    val type: MessageEmbed.Type? = null,
    val description: String? = null,
    val url: String? = null,
    val timestamp: String? = null,
    val color: Int? = null,
    val footer: MessageEmbed.Footer? = null,
    val image: MessageEmbed.Image? = null,
    val thumbnail: MessageEmbed.Image? = null,
    val video: MessageEmbed.Video? = null,
    val provider: MessageEmbed.Provider? = null,
    val author: MessageEmbed.Author? = null,
    val fields: List<MessageEmbed.Footer> = listOf()
) {

    @Serializable(with = Type.Serializer::class)
    sealed class Type(val name: String) {

        object Rich : Type("rich")

        object Image : Type("image")

        object Video : Type("video")

        object GifVideo : Type("gifv")

        object Article : Type("article")

        object Link : Type("Link")

        class Unknown(name: String) : Type(name)

        object Serializer : KSerializer<Type> {

            override val descriptor = PrimitiveSerialDescriptor("Delta.EmbedType", PrimitiveKind.STRING)

            override fun deserialize(decoder: Decoder): Type = when (val name = decoder.decodeString()) {
                "rich" -> Rich
                "image" -> Image
                "video" -> Video
                "gifv" -> GifVideo
                "article" -> Article
                "link" -> Link

                else -> Unknown(name)
            }

            override fun serialize(encoder: Encoder, value: Type) {
                encoder.encodeString(value.name)
            }
        }
    }

    @Serializable
    data class Footer(
        val text: String? = null,
        @SerialName("icon_url")
        val iconUrl: String? = null,
        @SerialName("proxy_icon_url")
        val proxiedIconUrl: String? = null
    )

    @Serializable
    data class Image(
        val url: String? = null,
        @SerialName("proxy_url")
        val proxiedUrl: String? = null,
        val width: Int? = null,
        val height: Int? = null
    )

    @Serializable
    data class Video(val url: String? = null, val width: Int? = null, val height: Int? = null)

    @Serializable
    data class Provider(val name: String? = null, val url: String? = null)

    @Serializable
    data class Author(
        val name: String? = null,
        val url: String? = null,
        @SerialName("icon_url")
        val iconUrl: String? = null,
        @SerialName("proxy_icon_url")
        val proxiedIconUrl: String? = null)

    data class Field(val name: String, val value: String, val inline: Boolean = false)
}

@Serializable
data class MessageReaction(val count: Int, val me: Boolean, val emoji: EmojiData)

@Serializable(with = MessageType.Serializer::class)
sealed class MessageType(val id: Int) {

    object Default : MessageType(0)
    object RecipientAdd : MessageType(1)
    object RecipientRemove : MessageType(2)
    object Call : MessageType(3)
    object ChannelNameChange : MessageType(4)
    object ChannelIconChange : MessageType(5)
    object ChannelPinnedMessage : MessageType(6)
    object GuildMemberJoin : MessageType(7)
    object UserPremiumGuildSubscription : MessageType(8)
    object UserPremiumGuildSubscriptionTier1 : MessageType(9)
    object UserPremiumGuildSubscriptionTier2 : MessageType(10)
    object UserPremiumGuildSubscriptionTier3 : MessageType(11)
    object ChannelFollowAdd : MessageType(12)
//    @Deprecated(message = "Removed from Discord")
//    object GuildStream : MessageType(13)
    object GuildDiscoveryDisqualified : MessageType(14)
    object GuildDiscoveryRequalified : MessageType(15)
    object GuildDiscoveryGracePeriodInitialWarning : MessageType(16)
    object GuildDiscoveryGracePeriodFinalWarning : MessageType(17)
    object ThreadCreated : MessageType(18)
    object Reply : MessageType(19)
    object ApplicationCommand : MessageType(20)

    class Unknown(id: Int) : MessageType(id)

    object Serializer : KSerializer<MessageType> {

        override val descriptor = PrimitiveSerialDescriptor("Delta.MessageType", PrimitiveKind.INT)

        override fun deserialize(decoder: Decoder): MessageType = when(val id = decoder.decodeInt()) {
            0 -> Default
            1 -> RecipientAdd
            2 -> RecipientRemove
            3 -> Call
            4 -> ChannelNameChange
            5 -> ChannelIconChange
            6 -> ChannelPinnedMessage
            7 -> GuildMemberJoin
            8 -> UserPremiumGuildSubscription
            9 -> UserPremiumGuildSubscriptionTier1
            10 -> UserPremiumGuildSubscriptionTier2
            11 -> UserPremiumGuildSubscriptionTier3
            12 -> ChannelFollowAdd
//          13 -> GuildStream
            14 -> GuildDiscoveryDisqualified
            15 -> GuildDiscoveryRequalified
            16 -> GuildDiscoveryGracePeriodInitialWarning
            17 -> GuildDiscoveryGracePeriodFinalWarning
            18 -> ThreadCreated
            19 -> Reply
            20 -> ApplicationCommand

            else -> Unknown(id)
        }

        override fun serialize(encoder: Encoder, value: MessageType) {
            encoder.encodeInt(value.id)
        }
    }
}

@Serializable
data class MessageActivity(val type: MessageActivity.Type, @SerialName("party_id") val party: String) {

    @Serializable(with = Type.Serializer::class)
    sealed class Type(val id: Int) {

        object Join : Type(1)
        object Spectate : Type(2)
        object Listen : Type(3)
        object JoinRequest : Type(4)

        class Unknown(id: Int) : Type(id)

        object Serializer : KSerializer<Type> {

            override val descriptor = PrimitiveSerialDescriptor("Delta.MessageActivity.Type", PrimitiveKind.INT)

            override fun deserialize(decoder: Decoder): Type = when(val id = decoder.decodeInt()) {
                1 -> Join
                2 -> Spectate
                3 -> Listen
                4 -> JoinRequest

                else -> Unknown(id)
            }

            override fun serialize(encoder: Encoder, value: Type) {
                encoder.encodeInt(value.id)
            }
        }
    }
}

@Serializable
data class MessageApplication(
    @Serializable(with = SnowflakeSerializer::class)
    val id: Long,
    @SerialName("cover_image")
    val coverImage: String? = null,
    val description: String,
    val icon: String?,
    val name: String
)

@Serializable
data class MessageReference(
    @SerialName("message_id") @Serializable(with = SnowflakeSerializer::class)
    val messageId: Long,
    @SerialName("channel_id") @Serializable(with = SnowflakeSerializer::class)
    val channelId: Long,
    @SerialName("guild_id") @Serializable(with = SnowflakeSerializer::class)
    val guildId: Long,
)