package compiler.parser.impl

import compiler.core.IParsedExpressionNode
import compiler.core.ParsedBinaryOperatorNode
import compiler.core.Stack
import compiler.core.Token
import compiler.parser.impl.internal.IExpressionGenerator

internal class BinaryOperatorExpressionGenerator: IExpressionGenerator {
    override fun generateExpression(
        resultStack: Stack<IParsedExpressionNode>,
        topToken: Token
    ) {
        val rightExpression = resultStack.pop()
        val leftExpression = resultStack.pop()
        val binaryOperatorExpression = ParsedBinaryOperatorNode(leftExpression, rightExpression, topToken.value)
        resultStack.push(binaryOperatorExpression)
    }

}