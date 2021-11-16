package compiler.parser.impl

import compiler.core.ParsedExpressionStatementNode
import compiler.core.Token
import compiler.core.TokenType
import compiler.parser.impl.internal.IExpressionParser
import compiler.parser.impl.internal.IExpressionStatementParser
import compiler.parser.impl.internal.ITokenTypeAsserter

internal class ExpressionStatementParser(
    private val tokenTypeAsserter: ITokenTypeAsserter,
    private val expressionParser: IExpressionParser
): IExpressionStatementParser {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<ParsedExpressionStatementNode, Int> {
        val (expressionNode, positionAfterExpression) = expressionParser.parse(tokens, startingPosition)
        tokenTypeAsserter.assertTokenType(tokens, positionAfterExpression, TokenType.SEMICOLON)
        val positionAfterSemicolon = positionAfterExpression + 1
        val expressionStatementNode = ParsedExpressionStatementNode(expressionNode)
        return Pair(expressionStatementNode, positionAfterSemicolon)
    }
}