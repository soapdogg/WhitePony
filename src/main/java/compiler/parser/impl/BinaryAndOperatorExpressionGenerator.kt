package compiler.parser.impl

import compiler.core.IParsedExpressionNode
import compiler.core.ParsedBinaryAndOperatorNode
import compiler.core.Stack
import compiler.core.Token
import compiler.parser.impl.internal.IExpressionGenerator

internal class BinaryAndOperatorExpressionGenerator: IExpressionGenerator {
    override fun generateExpression(
        resultStack: Stack<IParsedExpressionNode>,
        topToken: Token
    ) {
        val rightExpression = resultStack.pop()
        val leftExpression = resultStack.pop()
        val binaryAndExpression = ParsedBinaryAndOperatorNode(leftExpression, rightExpression)
        resultStack.push(binaryAndExpression)
    }
}