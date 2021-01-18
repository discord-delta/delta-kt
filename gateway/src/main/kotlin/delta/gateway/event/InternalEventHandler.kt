package delta.gateway.event

import delta.entity.GuildData
import delta.entity.MessageData
import delta.gateway.GatewaySession
import delta.gateway.GatewayStatus
import delta.gateway.heartbeat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive

internal fun GatewaySession.initializeListeners() {
    // Event processor
    events.filterIsInstance<RawGatewayEvent>().onEach {
        // Update sequence #
        if (it.sequence != null && it.sequence > it.session.sequence) it.session.sequence = it.sequence

        try {
            val event = when (it.opcode) {
                0 -> createGatewayEvent(it)
                9 -> decodeEvent(InvalidSessionEvent.serializer(), it)

                10 -> decodeEvent(HelloEvent.serializer(), it)
                11 -> HeartbeatAckEvent(it.payload.jsonPrimitive.intOrNull)

                else -> null
            }

            if (event != null) {
                event.session = it.session
                events.emit(event)
            }
        } catch(e: Throwable) {
            println("Failed to parse raw event: ${it.opcode} ${it.eventName}\n${it.payload}")
            e.printStackTrace()
        }
    }.launchIn(GlobalScope)

    // Heartbeat handler
    @Suppress("EXPERIMENTAL_API_USAGE") // We don't want to require the GatewaySession class to have an opt in
    events.filterIsInstance<HelloEvent>().onEach {
        val session = it.session

        session.status = GatewayStatus.CONNECTED
        session.ticker = ticker(it.heartbeatInterval.toLong())

        GlobalScope.launch {
            for (event in session.ticker) {
                heartbeat(session.sequence)
            }
        }
    }.launchIn(GlobalScope)

    // Session ID setter
    events.filterIsInstance<ReadyEvent>().onEach {
        it.session.id = it.sessionId
    }.launchIn(GlobalScope)
}


private fun createGatewayEvent(event: RawGatewayEvent): GatewayEvent? {
    val json = event.session.json

    return when(event.eventName) {
        "READY" -> decodeEvent(ReadyEvent.serializer(), event)

        "GUILD_CREATE" -> GuildJoinEvent(json.decodeFromJsonElement(GuildData.serializer(), event.payload))

        "MESSAGE_CREATE" -> MessageCreateEvent(json.decodeFromJsonElement(MessageData.serializer(), event.payload))
        "MESSAGE_UPDATE" -> MessageUpdateEvent(json.decodeFromJsonElement(MessageData.serializer(), event.payload))
        "MESSAGE_DELETE" -> decodeEvent(MessageDeleteEvent.serializer(), event)

        else -> null
    }
}

private fun <T : GatewayEvent> decodeEvent(decoder: KSerializer<T>, event: RawGatewayEvent): T {
    return event.session.json.decodeFromJsonElement(decoder, event.payload)
}