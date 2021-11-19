package compiler.parser.impl.internal

import compiler.core.IParsedExpressionNode
import compiler.core.Stack
import compiler.core.Token

internal interface IExpressionGenerator {
    fun generateExpression(
        resultStack: Stack<IParsedExpressionNode>,
        topToken: Token
    )
}