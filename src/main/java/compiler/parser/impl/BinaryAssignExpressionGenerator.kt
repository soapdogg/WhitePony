package compiler.parser.impl

import compiler.core.*
import compiler.core.constants.ExpressionParserConstants
import compiler.parser.impl.internal.IExpressionGenerator

internal class BinaryAssignExpressionGenerator: IExpressionGenerator {
    override fun generateExpression(
        resultStack: Stack<IParsedExpressionNode>,
        topToken: Token
    ) {
        val rightExpression = resultStack.pop()
        val leftExpression = resultStack.pop()
        val resultNode = if(topToken.type == TokenType.BINARY_ASSIGN) {
            ParsedBinaryAssignNode(leftExpression, rightExpression)
        } else {
            ParsedBinaryAssignOperatorNode(leftExpression, rightExpression, topToken.value.replace(ExpressionParserConstants.ASSIGN_OPERATOR, ExpressionParserConstants.EMPTY))
        }
        resultStack.push(resultNode)
    }
}