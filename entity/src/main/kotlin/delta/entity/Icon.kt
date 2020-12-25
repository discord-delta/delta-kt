package delta.entity

class Icon(val path: String, val hash: String) {

    val isAnimated: Boolean = hash.startsWith("a_")
    val format: String = if (isAnimated) "gif" else "png"
    val url: String = "https://cdn.discordapp.com/$path/$hash.$format"
}