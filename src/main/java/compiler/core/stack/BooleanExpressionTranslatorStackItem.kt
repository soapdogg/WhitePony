package compiler.core.stack

import compiler.core.nodes.parsed.IParsedExpressionNode

data class BooleanExpressionTranslatorStackItem(
    val location: Int,
    val node: IParsedExpressionNode,
    val trueLabel: String,
    val falseLabel: String
)
