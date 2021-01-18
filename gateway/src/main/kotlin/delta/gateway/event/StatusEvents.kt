package delta.gateway.event

import delta.entity.UnavailableGuildData
import delta.entity.UserData
import delta.gateway.ShardInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
abstract class GenericConnectionEvent : GatewayEvent()

@Serializable
data class HelloEvent(
    @SerialName("heartbeat_interval")
    val heartbeatInterval: Int
) : GenericConnectionEvent()

@Serializable
data class ReadyEvent(
    @SerialName("v")
    val gatewayVersion: Int,
    val user: UserData,
    @SerialName("guilds")
    val unavailableGuilds: List<UnavailableGuildData>,
    @SerialName("session_id")
    val sessionId: String,
    @SerialName("shard")
    val shardInfo: ShardInfo
) : GenericConnectionEvent()

@Serializable
class ResumedEvent : GenericConnectionEvent()

@Serializable
class ReconnectEvent : GenericConnectionEvent()

@Serializable
class InvalidSessionEvent : GenericConnectionEvent()

@Serializable
class HeartbeatEvent : GenericConnectionEvent()

@Serializable
data class HeartbeatAckEvent(val sequence: Int?) : GenericConnectionEvent()