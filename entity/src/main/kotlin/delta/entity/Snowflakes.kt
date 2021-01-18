package delta.entity

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Serializes snowflakes to and from longs.
 */
object SnowflakeSerializer : KSerializer<Long> {

    override val descriptor = PrimitiveSerialDescriptor("Delta.Snowflake", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Long) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Long {
        return decoder.decodeString().toLong()
    }
}