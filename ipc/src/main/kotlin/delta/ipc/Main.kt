package delta.ipc

import pw.aru.utils.ipc.client.builder.socket.SocketClientBuilder

fun main() {
    SocketClientBuilder().port(25050).address(unixPath).build()
}