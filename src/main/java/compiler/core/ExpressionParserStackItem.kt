package compiler.core

data class ExpressionParserStackItem(
    val location: Int,
    val token: Token?,
    val expression: IParsedExpressionNode?
) {
    constructor(location: Int, token: Token?) : this(location, token, null)
}
