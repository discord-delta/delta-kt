package delta.gateway

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an outgoing command.
 */
@Serializable
sealed class GatewayCommand(val op: Int) {

    /**
     * Represents an outgoing heartbeat command.
     */
    @Serializable
    class HeartbeatCommand(@SerialName("d") val sequence: Int) : GatewayCommand(1)

    /**
     * Represents an outgoing identify command.
     */
    @Serializable
    class IdentifyCommand(@SerialName("d") val data: IdentifyData) : GatewayCommand(2)

    /**
     * Represents an outgoing resume command.
     */
    @Serializable
    class ResumeCommand(@SerialName("d") val data: ResumeData) : GatewayCommand(6)
}

/**
 * The data used for identification.
 */
@Serializable
data class IdentifyData(val token: String, val properties: ConnectionProperties, val shard: ShardInfo, val intents: Int)

/**
 * The data used for resuming.
 */
@Serializable
data class ResumeData(val token: String, @SerialName("session_id") val sessionId: String, @SerialName("seq") val sequence: Int)