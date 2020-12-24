package delta.rest

import delta.common.Authorizer
import delta.common.DELTA_VERSION
import delta.common.DELTA_WEBSITE
import delta.common.TokenType
import delta.rest.feature.RateLimiting
import delta.rest.feature.TokenAuth
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*

class RestClient(token: String, tokenType: TokenType = TokenType.BOT) {

    internal val client = HttpClient(CIO) {
        install(UserAgent) {
            this.agent = "DiscordBot ($DELTA_WEBSITE, $DELTA_VERSION)"
        }

        install(TokenAuth) {
            this.authorizer = Authorizer(token, tokenType)
        }

        install(RateLimiting)
    }
}

