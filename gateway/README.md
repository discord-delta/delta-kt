# Gateway Module
The gateway module provides a simple implementation of the Discord [gateway API](https://dicsord.dev/topics/gateway).

## Establishing a Connection
The `GatewaySession` class accepts a [KTOR](https://ktor.io) websocket object, which is then used to connect.
```kt
val httpClient = HttpClient {
    install(WebSockets)
}

val gateway = GatewaySession(httpClient.webSocketSession {
    url("wss://gateway.discord.gg?v=8")
})
gateway.identify(token = "Bot Nzk3NzA1NDI0MDk3NzcxNTMy.X_qXED.AfDDUZHH6zMa8DcZbGLPqLRFzro")
gateway.connect().join()
}
```

## Handling Events
Delta uses Kotlin's flow API to provide a standard event acceptor structure that's easy to use.
> Make sure to subscribe to events before connecting! The `connect` method blocks the thread until the session closes.


```kt
session.events.filterIsInstance<ReadyEvent>().onEach { event ->
    println("Ready on shard ${event.shardInfo.shard}! Username: ${event.user.name}")
}.launchIn(GlobalScope)
```

## Reconnecting
Discord will disconnect connections frequently to enable automatic load balancing. To handle reconnects, a basic loop can
be used.
> Be advised that this is not a perfect implementation. The full gateway flow is more complex, and this code would occasionally
> break. This is merely an example of what the reconnection flow looks like.

```kt
suspend fun main() {
    val client = HttpClient {
        install(WebSockets)
    }
    run(client)
}

suspend tailrec fun run(client: HttpClient) {
    val ws = client.webSocketSession {
        url("wss://gateway.discord.gg?v=8")
    }

    val gateway = GatewaySession(ws)
    gateway.events.filterIsInstance<ReadyEvent>().onEach {
        println("Hello world")
    }.launchIn(GlobalScope)
    
    gateway.connect().await()
    run(client)
}
```