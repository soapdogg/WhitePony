package compiler.parser.impl

import compiler.core.*
import compiler.parser.impl.internal.IExpressionGenerator

internal class UnaryExpressionGenerator: IExpressionGenerator {
    override fun generateExpression(
        resultStack: Stack<IParsedExpressionNode>,
        topToken: Token
    ) {
        val insideExpression = resultStack.pop()
        val unaryExpression = when (topToken.type) {
            TokenType.PLUS_MINUS, TokenType.BIT_NEGATION -> {
                ParsedUnaryOperatorNode(insideExpression, topToken.value)
            }
            TokenType.UNARY_NOT -> {
                ParsedUnaryNotOperatorNode(insideExpression)
            }
            else -> {
                ParsedUnaryPreOperatorNode(insideExpression, topToken.value[0].toString())
            }
        }
        resultStack.push(unaryExpression)
    }
}