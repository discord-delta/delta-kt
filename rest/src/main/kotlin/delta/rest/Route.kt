package delta.rest

val majorParameters = listOf("channel_id", "webhook_id", "message_id")

class Route(path: String) {

    val path: List<PathElement> = path.split('/').map { string ->
        if (string.isEmpty()) return@map PathElement.Blank

        if (string[0] == '{') {
            val argument = string.substring(1, string.length - 1)

            if (majorParameters.contains(argument)) {
                return@map PathElement.MajorArgument(argument)
            }
            return@map PathElement.MinorArgument(argument)
        }

        return@map PathElement.Literal(string)
    }.filterNot { it is PathElement.Blank }

    companion object {

        val selfUser = Route("users/@me")
        val user = Route("users/{user_id}")

        val channel = Route("channels/{channel_id}")
        val channelMessages = Route("channels/{channel_id}/messages/{message_id}")
    }
}

fun main() {
    println(Route.selfUser.path)
    println(Route.user.path)
    println(Route.channel.path)
    println(Route.channelMessages.path)
}

sealed class PathElement {

    object Blank : PathElement()

    data class Literal(val path: String) : PathElement()

    data class MajorArgument(val name: String) : PathElement()

    data class MinorArgument(val name: String) : PathElement()
}