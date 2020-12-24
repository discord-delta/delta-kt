package delta.rest.feature

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.util.*

class RateLimiting internal constructor() {

    companion object Feature : HttpClientFeature<Unit, RateLimiting> {

        override val key = AttributeKey<RateLimiting>("RateLimiting")

        override fun prepare(block: Unit.() -> Unit): RateLimiting = RateLimiting()

        override fun install(feature: RateLimiting, scope: HttpClient) {

        }
    }
}