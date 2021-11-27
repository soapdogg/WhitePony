package compiler.translator.impl.internal

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.stack.BooleanExpressionTranslatorStackItem
import compiler.core.stack.Stack

internal interface IBooleanExpressionTranslatorStackPusher {
    fun push(
        location: Int,
        node: IParsedExpressionNode,
        trueLabel: String,
        falseLabel: String,
        stack: Stack<BooleanExpressionTranslatorStackItem>
    )
}