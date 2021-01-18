package delta.ipc

import java.lang.System.getenv

const val base: String = "discord-ipc-0"
const val windowsPath: String = "\\\\?\\pipe\\$base"
val unixPath: String = "${getenv("XDG_RUNTIME_DIR") ?: getenv("TMPDIR") ?: getenv("TMP") ?: getenv("TEMP") ?: "temp"}/$base"
val path: String = if (System.getProperty("os.name").startsWith("Windows")) windowsPath else unixPath