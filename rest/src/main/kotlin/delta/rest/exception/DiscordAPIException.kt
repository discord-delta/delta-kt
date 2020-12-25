package delta.rest.exception

import kotlinx.serialization.json.*

fun parseError(body: String): DiscordAPIException {
    val data = Json.parseToJsonElement(body).jsonObject

    return when(val code = data["code"]!!.jsonPrimitive.int) {
        50035 -> {
            val errorMap = mutableMapOf<String, String>()
            accumulateErrors(data["errors"]!!.jsonObject, errorMap)

            DiscordAPIException(code,
                "Error $code:\n${errorMap.map { error -> " - ${error.key}: \"${error.value}\"" }.joinToString("\n")}"
            )
        }
        else -> DiscordAPIException(code, data["message"]!!.jsonPrimitive.content)
    }
}

private fun accumulateErrors(data: JsonObject, map: MutableMap<String, String>, path: String = "") {
    if (data.containsKey("_errors")) {
        val error = data["_errors"]!!.jsonArray[0].jsonObject
        val message = error["message"]!!.jsonPrimitive.content
        val code = error["code"]!!.jsonPrimitive.content

        val errorName = if (path.isEmpty()) code else "$code/$path"
        map[errorName] = message
    } else for (key in data.keys) {
        accumulateErrors(data[key]!!.jsonObject, map, createPath(path, key))
    }
}

private fun createPath(path: String, key: String): String {
    if (path.isEmpty()) return key
    return path + if(key.all { Character.isDigit(it) }) "[$key]" else ".$key"
}

class DiscordAPIException(val code: Int, message: String) : Exception(message)

