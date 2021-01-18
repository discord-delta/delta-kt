package delta.gateway

import delta.common.DELTA_LIBRARY_NAME
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A serializable representation of [gateway identify properties](https://discord.dev/gateway#identify-identify-connection-properties).
 */
@Serializable
data class ConnectionProperties(
    /**
     * The name of the current operating system.
     */
    @SerialName("\$os") val os: String,
    /**
     * The name of the current browser.
     */
    @SerialName("\$browser") val browser: String,
    /**
     * The name of the current device.
     */
    @SerialName("\$device") val device: String
) {

    companion object {

        val DEFAULT = ConnectionProperties(System.getProperty("os.name"), DELTA_LIBRARY_NAME, DELTA_LIBRARY_NAME)
    }
}
