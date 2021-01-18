package delta.gateway.event

import delta.gateway.GatewaySession
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonElement

/**
 * Represents an incoming Discord event.
 */
@Serializable
abstract class GatewayEvent {
    /**
     * The gateway session the event was sourced from.
     */
    @Transient
    lateinit var session: GatewaySession
        internal set
}

/**
 * Represents the raw data of an incoming event.
 */
data class RawGatewayEvent(
    /**
     * The [opcode](https://discord.dev/topics/opcodes-and-status-codes#gateway-opcodes) of the payload.
     */
    val opcode: Int,
    /**
     * The name of the event. Only present for opcode 0.
     */
    val eventName: String?,
    /**
     * The sequence number of the event. Only present for opcode 0.
     */
    val sequence: Int?,
    /**
     * The payload of the event.
     */
    val payload: JsonElement
) : GatewayEvent()