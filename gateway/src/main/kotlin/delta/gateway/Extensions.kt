package delta.gateway

import delta.common.DELTA_LIBRARY_NAME
import delta.gateway.GatewayCommand.*

/**
 * Sends an `identify` command to the session.
 *
 * @param token The token used to identify. <b>Must be prefixed with "Bot " for bot accounts.</b>
 * @param os The current operating system.
 * @param browser The browser name. Bots are supposed to send the library name, but can send "Discord iOS" to get a
 * mobile status indicator.
 * @param device The device name. Bots are supposed to send the library name, but this property has no effect.
 * @param shard The current shard ID.
 * @param shardCount The total shard count.
 * @param intents The [gateway intents](https://discord.dev/topics/gateway#gateway-intents) bitfield.
 *
 */
suspend fun GatewaySession.identify(token: String, os: String = System.getProperty("os.name"),
                                    browser: String = DELTA_LIBRARY_NAME, device: String = DELTA_LIBRARY_NAME,
                                    shard: Int = 0, shardCount: Int = 1, intents: Int = 0) {
    identify(token, ConnectionProperties(os, browser, device), ShardInfo(shard, shardCount), intents)
}

/**
 * Sends an `identify` command to the session.
 *
 * @param token The token used to identify. <b>Must be prefixed with "Bot " for bot accounts.</b>
 * @param properties The connection properties to use.
 * @param shard The shard information to use.
 * @param intents The [gateway intents](https://discord.dev/topics/gateway#gateway-intents) bitfield.
 */
suspend fun GatewaySession.identify(token: String, properties: ConnectionProperties = ConnectionProperties.DEFAULT,
                                    shard: ShardInfo = ShardInfo(0, 1), intents: Int = 0
) {
    send(IdentifyCommand(IdentifyData(token, properties, shard, intents)))
}

/**
 * Sends a resume command to the session, backfilling missed events.
 *
 * @param token The token used to identify. <b>Must be prefixed with "Bot " for bot accounts.</b>
 * @param sessionId The session ID.
 * @param sequence The last received sequence number.
 */
suspend fun GatewaySession.resume(token: String, sessionId: String, sequence: Int) {
    send(ResumeCommand(ResumeData(token, sessionId, sequence)))
}

/**
 * Sends a heartbeat command to the session.
 *
 * @param sequence The last received sequence number.
 */
suspend fun GatewaySession.heartbeat(sequence: Int?) {
    send(HeartbeatCommand(sequence))
}
