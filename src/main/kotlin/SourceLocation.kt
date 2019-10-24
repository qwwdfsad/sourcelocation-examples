


inline fun lineNumber(): Int = element().lineNumber
inline fun methodName(): String = element().methodName
inline fun className(): String = element().className.substringAfterLast(".")
inline fun packageName(): String = element().className.substringBeforeLast(".")
inline fun fileName(): String = element().fileName
// TODO I don't know whether we need it in face
inline fun isTopLevelFile(): Boolean = className().endsWith("Kt")

inline fun element() = Exception().stackTrace[2]

// Hand-rolled class
data class SourceLocation(
    val fileName: String = fileName(),
    val packageName: String = packageName(),
    val className: String = className(),
    val methodName: String = methodName(),
    val lineNumber: Int = lineNumber(),
    val isTopLevelFile: Boolean = isTopLevelFile()
)