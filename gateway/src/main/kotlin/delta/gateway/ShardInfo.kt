package delta.gateway

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.IntArraySerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Represents the serializable shard information array used by Discord.
 * Serialized into an array in the format `[shard, shardCount]`.
 */
@Serializable(with = ShardInfo.Serializer::class)
data class ShardInfo(
    /**
     * The shard ID to be used.
     */
    val shard: Int,
    /**
     * The total shard count.
     */
    val shardCount: Int) {

    object Serializer : KSerializer<ShardInfo> {

        override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Delta.ShardInfo") {
            element<IntArray>("data")
        }

        override fun serialize(encoder: Encoder, value: ShardInfo) {
            encoder.encodeSerializableValue(IntArraySerializer(), intArrayOf(value.shard, value.shardCount))
        }

        override fun deserialize(decoder: Decoder): ShardInfo {
            val data = decoder.decodeSerializableValue(IntArraySerializer())
            return ShardInfo(data[0], data[1])
        }
    }
}
