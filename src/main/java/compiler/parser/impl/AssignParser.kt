package compiler.parser.impl

import compiler.core.AssignNode
import compiler.core.Token
import compiler.core.TokenType
import compiler.parser.impl.internal.IAssignParser
import compiler.parser.impl.internal.IExpressionParser
import compiler.parser.impl.internal.ITokenTypeAsserter

internal class AssignParser(
    private val tokenTypeAsserter: ITokenTypeAsserter,
    private val expressionParser: IExpressionParser
): IAssignParser {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<AssignNode, Int> {
        tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.BINARY_ASSIGN)
        val positionAfterAssign = startingPosition + 1
        val (expressionNode, positionAfterExpression) = expressionParser.parse(tokens, positionAfterAssign)
        val assignNode = AssignNode(expressionNode)
        return Pair(assignNode, positionAfterExpression)
    }
}