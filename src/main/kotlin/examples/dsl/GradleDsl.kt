package examples.dsl

import SourceLocation
import kotlin.reflect.*
import kotlin.reflect.jvm.*

fun project(builder: DependenciesBuilder.() -> Unit): Project {
    val b = DependenciesBuilder()
    b.builder()
    return Project(b._dependencies)
}

class Project(private val dependencies: List<Dependency>) {
    fun evaluate() {
        dependencies.forEach {
            throw object :
                RuntimeException("Cannot resolve dependency, version ${it.version} does not exist.\n " +
                        "The first frame will point to the declaration site") {
                override fun fillInStackTrace(): Throwable {
                    super.fillInStackTrace()
                    val frame = with(it.location) {
                        StackTraceElement("$packageName.$className", methodName, fileName, lineNumber)
                    }
                    val st = stackTrace
                    val array = Array(st.size + 1) {
                        if (it == 0) frame else st[it - 1]
                    }
                    stackTrace = array
                    return this
                }
            }
        }
    }
}

class DependenciesBuilder {

    val _dependencies = ArrayList<Dependency>()

    inline fun richDependency(block: DependencyBuilder.() -> Unit) {
        with(DependencyBuilder()) {
            block()
            // Forgive me, Father, for I have sinned
            val sid = ::version.also { it.isAccessible = true }.getDelegate() as SourceLocationDelegate<String>
            _dependencies += Dependency(namespace, name, version, sid.source!!)
        }
    }
}

class Dependency(val namespace: String, val name: String, val version: Int, val location: SourceLocation)

class DependencyBuilder {
    var namespace: String by SourceLocationDelegate()
    var name: String by SourceLocationDelegate()
    var version: Int by SourceLocationDelegate()
}

class SourceLocationDelegate<T>(var value: T? = null, var source: SourceLocation? = null)

public inline operator fun <T> SourceLocationDelegate<T>.getValue(thisRef: Any?, property: KProperty<*>): T = value!!
public inline operator fun <T> SourceLocationDelegate<T>.setValue(thisRef: Any?, property: KProperty<*>, value: T) {
    this.value = value
    this.source = SourceLocation()
}
