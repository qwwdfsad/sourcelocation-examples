package examples.logging

val logger = Logger()

fun bar() {
    logger.log("top level fun")
    logger.detailedLog("top level fun, detailed")
}

class Bar {
    val logger = Logger()

    companion object {
        // NB: Note that "Bar" name is captured! Probably should be designed
        val LOGGER = Logger()
    }

    fun foo() {
        logger.log("Bar fun")
        logger.detailedLog("Bar fun, detailed")
        bar()
        LOGGER.log("Bar fun, logged by companion")
    }
}

fun main() {
    Bar().foo()
}