package compiler.translator.impl.internal

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack

internal interface IExpressionTranslatorStackPusher {
    fun push(
        location: ExpressionTranslatorLocation,
        expression1: IParsedExpressionNode,
        expression2: IParsedExpressionNode,
        stack: Stack<ExpressionTranslatorStackItem>
    )
}