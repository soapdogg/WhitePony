package compiler.core

data class ParsedArrayNode(
    override val indexExpressionNode: IParsedExpressionNode?
): IArrayNode
