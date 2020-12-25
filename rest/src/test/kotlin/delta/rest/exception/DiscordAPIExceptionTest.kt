package delta.rest.exception

import kotlin.test.Test
import kotlin.test.assertEquals

internal class DiscordAPIExceptionTest {

    val objectError = """
                {
                    "code": 50035,
                    "errors": {
                        "access_token": {
                            "_errors": [
                                {
                                    "code": "BASE_TYPE_REQUIRED",
                                    "message": "This field is required"
                                }
                            ]
                        }
                    },
                    "message": "Invalid Form Body"
                }
            """
    val arrayError = """
        {
            "code": 50035,
            "errors": {
                "activities": {
                    "0": {
                        "platform": {
                            "_errors": [
                                {
                                    "code": "BASE_TYPE_CHOICES",
                                    "message": "Value must be one of ('desktop', 'android', 'ios')."
                                }
                            ]
                        },
                        "type": {
                            "_errors": [
                                {
                                    "code": "BASE_TYPE_CHOICES",
                                    "message": "Value must be one of (0, 1, 2, 3, 4, 5)."
                                }
                            ]
                        }
                    }
                }
            },
            "message": "Invalid Form Body"
        }
    """.trimIndent()

    @Test
    internal fun objectErrorTest() {
        assertEquals("""
            Error 50035:
             - BASE_TYPE_REQUIRED/access_token: "This field is required"
        """.trimIndent(), parseError(objectError).message)
    }

    @Test
    internal fun arrayErrorTest() {
        assertEquals("""
            Error 50035:
             - BASE_TYPE_CHOICES/activities[0].platform: "Value must be one of ('desktop', 'android', 'ios')."
             - BASE_TYPE_CHOICES/activities[0].type: "Value must be one of (0, 1, 2, 3, 4, 5)."
        """.trimIndent(), parseError(arrayError).message)
    }
}