package delta.common

class Authorizer(token: String, type: TokenType = TokenType.BOT) {

    val token: String = type.tokenPrefix + token
}

enum class TokenType(internal val tokenPrefix: String) {

    USER(""),
    BEARER("Bearer "),
    BOT("Bot ")
}