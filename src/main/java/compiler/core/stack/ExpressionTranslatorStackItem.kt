package compiler.core.stack

import compiler.core.nodes.parsed.IParsedExpressionNode

data class ExpressionTranslatorStackItem(
    val location : Int,
    val node: IParsedExpressionNode
)
