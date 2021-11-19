package compiler.core

data class ExpressionParserStackItem(
    val location: Int,
    val token: Token?,
) {
    constructor(location: Int): this(location, null)
}
