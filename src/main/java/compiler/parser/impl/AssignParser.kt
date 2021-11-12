package compiler.parser.impl

import compiler.core.AssignNode
import compiler.core.Token
import compiler.core.TokenType
import compiler.parser.impl.internal.IAssignParser
import compiler.parser.impl.internal.IExpressionParser

internal class AssignParser(
    private val expressionParser: IExpressionParser
): IAssignParser {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<AssignNode, Int> {
        //ASSIGN
        val (expression, currentPosition) = expressionParser.parse(tokens, startingPosition + 1, setOf(TokenType.COMMA, TokenType.SEMICOLON))
        val assignNode = AssignNode(expression)
        return Pair(assignNode, currentPosition)
    }
}