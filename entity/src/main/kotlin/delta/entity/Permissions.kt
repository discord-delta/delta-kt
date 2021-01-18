package delta.entity

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

enum class Permission(val value: Int) {

    CREATE_INSTANT_INVITE(0x00000001),
    KICK_MEMBERS(0x00000002),
    BAN_MEMBERS(0x00000004),
    ADMINISTRATOR(0x00000008),
    MANAGE_CHANNELS(0x0000010),
    MANAGE_GUILD(0x0000020),
    ADD_REACTIONS(0x0000040),
    VIEW_AUDIT_LOG(0x0000080),
    PRIORITY_SPEAKER(0x00000100),
    STREAM(0x00000200),
    VIEW_CHANNEL(0x00000400),
    SEND_MESSAGES(0x00000800),
    SEND_TTS_MESSAGES(0x00001000),
    MANAGE_MESSAGES(0x00002000),
    EMBED_LINKS(0x00004000),
    ATTACH_FILES(0x00008000),
    READ_MESSAGE_HISTORY(0x00010000),
    MENTION_EVERYONE(0x00020000),
    USE_EXTERNAL_EMOJIS(0x00040000),
    VIEW_GUILD_INSIGHTS(0x00080000),
    CONNECT(0x00100000),
    SPEAK(0x00200000),
    MUTE_MEMBERS(0x00400000),
    DEAFEN_MEMBERS(0x00800000),
    MOVE_MEMBERS(0x01000000),
    USE_VAD(0x0200000),
    CHANGE_NICKNAME(0x0400000),
    MANAGE_NICKNAMES(0x0800000),
    MANAGE_ROLES(0x1000000),
    MANAGE_WEBHOOKS(0x2000000),
    MANAGE_EMOJIS(0x4000000)
}

@Serializable
data class PermissionOverwrite(val id: Long, val type: PermissionOverwriteType, val allow: Int, val deny: Int)

@Serializable(with = PermissionOverwriteType.Serializer::class)
enum class PermissionOverwriteType(val id: Int) {

    ROLE(0),
    USER(1);

    object Serializer : KSerializer<PermissionOverwriteType> {

        override val descriptor = PrimitiveSerialDescriptor("Delta.PermissionOverwrite", PrimitiveKind.INT)

        override fun serialize(encoder: Encoder, value: PermissionOverwriteType) {
            encoder.encodeInt(value.id)
        }

        override fun deserialize(decoder: Decoder): PermissionOverwriteType {
            val id = decoder.decodeInt()
            return values().find { it.id == id }!!
        }
    }
}