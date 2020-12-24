package delta.gateway

import java.net.URI
import java.net.http.HttpClient
import java.net.http.WebSocket

const val DISCORD_WS_URL = "wss://gateway.discord.gg?encoding=json"

class GatewayConnection internal constructor(client: HttpClient) : WebSocket.Listener {

    private val socket: WebSocket = client.newWebSocketBuilder().buildAsync(URI(DISCORD_WS_URL), this).get()

    fun connect() {
        
    }
}