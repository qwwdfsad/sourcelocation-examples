package examples.logging

import SourceLocation
import java.text.SimpleDateFormat
import java.util.*

interface Logger {
    fun log(message: String, location: SourceLocation)
    fun detailedLog(message: String, location: SourceLocation)
}

inline fun Logger.log(message: String) = log(message, SourceLocation())
inline fun Logger.detailedLog(message: String) = detailedLog(message, SourceLocation())

inline fun Logger(): Logger = LoggerImpl()

public class LoggerImpl(location: SourceLocation = SourceLocation()) : Logger {

    private val clazz = if (location.isTopLevelFile) location.fileName else location.className

    override fun log(message: String, location: SourceLocation) {

        val pckg = location.packageName
        val prefix = "$pckg.$clazz:${location.lineNumber}"
        print(prefix, message)
    }

    override fun detailedLog(message: String, location: SourceLocation) {
        val pckg = location.packageName
        val method = location.methodName
        val filePrefix = if (location.fileName.replace(".kt", "Kt") == location.className) location.fileName + "." else ""
        val prefix = "$pckg.$filePrefix$clazz:$method:${location.lineNumber}"
        print(prefix, message)
    }

    private fun print(prefix: String, message: String) {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
        val s = sdf.format(Date())
        val length = 55 - prefix.length
        val spaces = " ".repeat(length)
        println("$s INFO $prefix$spaces$message")
    }
}