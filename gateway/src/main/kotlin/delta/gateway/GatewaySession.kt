package delta.gateway

import delta.common.AtomicReference
import delta.gateway.event.GatewayEvent
import delta.gateway.event.RawGatewayEvent
import delta.gateway.event.initializeListeners
import io.ktor.client.features.websocket.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import kotlin.concurrent.thread

/**
 * Represents a connection to the gateway. Sessions last as long as their contained sockets,
 * and do not directly handle any reconnect logic.
 */
open class GatewaySession(private val socket: DefaultClientWebSocketSession) {

    internal val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    private val shutdownHook = thread(start = false) {
        runBlocking { close(unregisterHook = false) }
    }
    @ObsoleteCoroutinesApi
    internal lateinit var ticker: ReceiveChannel<Unit>

    /**
     * The current event sequence number from Discord used for resuming.
     */
    var sequence: Int by AtomicReference(0)
        internal set

    /**
     * The current connection status.
     */
    var status: GatewayStatus by AtomicReference(GatewayStatus.PENDING_CONNECTION)
        internal set

    /**
     * The Discord assigned session ID used for resuming.
     */
    lateinit var id: String
        internal set

    /**
     * The consumable flow which forwards all events sent from Discord.
     */
    val events = MutableSharedFlow<GatewayEvent>(extraBufferCapacity = Int.MAX_VALUE)

    init {
        initializeListeners()
        Runtime.getRuntime().addShutdownHook(shutdownHook)
    }

    /**
     * Connects to the gateway, accepting incoming events and changing its status to `CONNECTED`.
     *
     * @throws IllegalArgumentException if the session is already connected.
     */
    suspend fun connectAsync() = GlobalScope.async {
        require(status == GatewayStatus.PENDING_CONNECTION) { "Session $id has already been connected! Initialize a new connection." }

        status = GatewayStatus.CONNECTING
        socket.incoming.consumeAsFlow().collect {
            process(it)
        }

        return@async withTimeout(1500) {
            socket.closeReason.await()
        }
    }

    /**
     * Sends the given command.
     *
     * @param command The command to send.
     * @throws IllegalArgumentException if the connection is not open.
     */
    suspend fun send(command: GatewayCommand) {
        require(status.isOpen) { "Session $id is not active! Send failed." }
        socket.send(Frame.Text(json.encodeToString(command)))
    }

    /**
     * Closes this session, changing its status to `TERMINATED`.
     *
     * @param reason The reason for closure. Defaults to 1000: Terminate.
     * @param unregisterHook Whether the shutdown hook responsible for closing the gateway will be removed. Defaults to true.
     */
    suspend fun close(reason: CloseReason = CloseReason(CloseReason.Codes.NORMAL, "Terminated"), unregisterHook: Boolean = true) {
        // No status precondition in case the socket is open and we for some reason need to terminate it w/o connecting
        status = GatewayStatus.SHUTTING_DOWN
        socket.close(reason)
        status = GatewayStatus.TERMINATED

        if (unregisterHook) Runtime.getRuntime().removeShutdownHook(shutdownHook)
    }

    private suspend fun process(frame: Frame) {
        val data = json.parseToJsonElement(String(frame.readBytes())).jsonObject

        val opcode = data["op"]!!.jsonPrimitive.int
        val eventName = data["t"]!!.jsonPrimitive.contentOrNull
        val sequence = data["s"]!!.jsonPrimitive.intOrNull
        val payload = data["d"]!!

        val event = RawGatewayEvent(opcode, eventName, sequence, payload)
        event.session = this

        events.emit(event)
    }
}

/**
 * Represents the current status of a gateway session.
 */
enum class GatewayStatus(val isOpen: Boolean) {

    /**
     * The session is waiting to be connected.
     */
    PENDING_CONNECTION(false),

    /**
     * The session is open, but has not received any events.
     */
    CONNECTING(true),

    /**
     * The session is fully connected and receiving events.
     */
    CONNECTED(true),

    /**
     * The session is shutting down.
     */
    SHUTTING_DOWN(false),

    /**
     * The session has been terminated and cannot be reopened.
     */
    TERMINATED(false)
}