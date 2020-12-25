package delta.rest

import delta.common.Authorizer
import delta.common.DELTA_VERSION
import delta.common.DELTA_WEBSITE
import delta.common.TokenType
import delta.rest.exception.parseError
import delta.rest.feature.RateLimiting
import delta.rest.feature.TokenAuth
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.statement.*

class RestClient(token: String, tokenType: TokenType = TokenType.BOT) {

    internal val client = HttpClient(CIO) {
        install(UserAgent) {
            this.agent = "DiscordBot ($DELTA_WEBSITE, $DELTA_VERSION)"
        }

        install(TokenAuth) {
            this.authorizer = Authorizer(token, tokenType)
        }

        install(RateLimiting)

        HttpResponseValidator {
            validateResponse { response ->
                when(response.status.value) {
                    in 300..399 -> throw RedirectResponseException(response)
                    400 -> throw parseError(String(response.readBytes()))
                    in 401..499 -> throw ClientRequestException(response)
                    in 500..599 -> throw ServerResponseException(response)
                }
            }
        }
    }
}

