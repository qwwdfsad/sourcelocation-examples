package examples.dsl


fun main() {
    val projectModel = project {
        richDependency {
            namespace = "org.jetbrains.kotlinx"
            name = "kotlinx.coroutines"
            version = 42
        }
    }

    projectModel.evaluate()
}

