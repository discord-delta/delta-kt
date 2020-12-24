package delta.rest.feature

import delta.common.Authorizer
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.util.*

class TokenAuth internal constructor(val authorizer: Authorizer) {

    class Configuration(var authorizer: Authorizer = Authorizer(""))

    companion object Feature : HttpClientFeature<Configuration, TokenAuth> {

        override val key = AttributeKey<TokenAuth>("TokenAuth")

        override fun prepare(block: Configuration.() -> Unit): TokenAuth {
            return TokenAuth(Configuration().apply(block).authorizer)
        }

        override fun install(feature: TokenAuth, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.Before) {
                context.header("Authorization", feature.authorizer.token)
            }
        }
    }
}