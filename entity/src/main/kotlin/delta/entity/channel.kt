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
    val id: Long,
    val type: ChannelType,
    @SerialName("guild_id")
    val guild: Long?,
    val position: Int?
)

@Serializable(with = ChannelType.Serializer::class)
enum class ChannelType(val id: Int) {

    GUILD_TEXT(0),
    DM(1),
    GUILD_VOICE(2),
    GROUP_DM(3),
    GUILD_CATEGORY(4),
    GUILD_NEWS(5),
    GUILD_STORE(6);

    object Serializer : KSerializer<ChannelType> {

        override val descriptor = PrimitiveSerialDescriptor("Delta.ChannelType", PrimitiveKind.INT)

        override fun serialize(encoder: Encoder, value: ChannelType) {
            encoder.encodeInt(value.id)
        }

        override fun deserialize(decoder: Decoder): ChannelType {
            val id = decoder.decodeInt()
            return values().find { it.id == id }!!
        }
    }
}