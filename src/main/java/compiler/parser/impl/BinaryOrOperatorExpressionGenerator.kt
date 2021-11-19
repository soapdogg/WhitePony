package compiler.parser.impl

import compiler.core.IParsedExpressionNode
import compiler.core.ParsedBinaryOrOperatorNode
import compiler.core.Stack
import compiler.core.Token
import compiler.parser.impl.internal.IExpressionGenerator

internal class BinaryOrOperatorExpressionGenerator: IExpressionGenerator {
    override fun generateExpression(
        resultStack: Stack<IParsedExpressionNode>,
        topToken: Token
    ) {
        val rightExpression = resultStack.pop()
        val leftExpression = resultStack.pop()
        val binaryOrExpression = ParsedBinaryOrOperatorNode(leftExpression, rightExpression)
        resultStack.push(binaryOrExpression)
    }
}